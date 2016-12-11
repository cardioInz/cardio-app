package cardio_app.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.sql.SQLException;

import cardio_app.R;
import cardio_app.activity.MainActivity;
import cardio_app.db.DbHelper;
import cardio_app.db.model.Event;
import cardio_app.receiver.AlarmNotification;
import cardio_app.util.DateTimeUtil;

import static cardio_app.util.IntentContentUtil.NOTIFICATION_ID;
import static cardio_app.util.IntentContentUtil.NOTIFICATION_TEXT;
import static cardio_app.util.IntentContentUtil.NOTIFICATION_TITLE;

/**
 * This service enables or disables alarms for Event stored in app. There are 3 modes:
 * <ul>
 * <li>UPDATE - updates single event (event has to be in database)</li>
 * <li>CANCEL - disables single event (event does not have to be in database);
 * this should be called after delete an event</li>
 * <li>UPDATE_ALL - updates all events from the database (default mode)</li>
 * </ul>
 * For modes with single event, intent should have an extra with event id.
 */
public class SetAlarmService extends Service {
    public static final String CANCEL = "cancel";
    public static final String UPDATE = "update";
    public static final String EVENT_ID = "event_id";
    private static final String TAG = SetAlarmService.class.getName();
    private static final int SERVICE_NOTIFICATION_ID = 101;
    private DbHelper dbHelper;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent foregroundIntent = new Intent(this, MainActivity.class);
        PendingIntent pForegroundIntent = PendingIntent.getActivity(this, 0, foregroundIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.alarms_have_been_set_successfully))
                .setSmallIcon(android.R.drawable.btn_plus)
                .setOngoing(true)
                .setContentIntent(pForegroundIntent);
        startForeground(SERVICE_NOTIFICATION_ID, builder.build());

        try {
            Dao<Event, Integer> eventDao = getDbHelper().getDao(Event.class);

            String action = null;
            if (intent != null) {
                action = intent.getAction();
            }
            if (action == null) {
                action = "";
            }
            switch (action) {
                case UPDATE: {
                    Event element = eventDao.queryForId(intent.getIntExtra(EVENT_ID, 0));
                    setAlarm(element);

                    break;
                }
                case CANCEL: {
                    int eventId = intent.getIntExtra(EVENT_ID, 0);
                    Uri uri = new Uri.Builder().path(String.valueOf(eventId)).build();
                    cancelAlarm(uri);

                    break;
                }
                default: {
                    for (Event event : eventDao.queryForAll()) {
                        setAlarm(event);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Log.wtf("SetAlarm", "Alarm was set");

        return START_STICKY;
    }

    private void setAlarm(Event event) {
        Log.d(TAG, "call 'setAlarm' for event with id: " + event.getId());
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Uri uri = new Uri.Builder().path(String.valueOf(event.getId())).build();
        cancelAlarm(uri);

        if (!event.isAlarmSet()) {
            return;
        }

        LocalDateTime startDate = LocalDateTime.fromDateFields(event.getStartDate());
        LocalDate endDate = LocalDate.fromDateFields(event.getEndDate());
        LocalDateTime actualDate = LocalDateTime.now();

        if (!endDate.isBefore(actualDate.toLocalDate())) {
            if (event.isRepeatable()) {
                while (startDate.isBefore(actualDate)) {
                    startDate = DateTimeUtil.increaseDate(startDate, event.getTimeUnit(), event.getTimeDelta());
                    if (startDate.toLocalDate().isAfter(endDate)) {
                        return;
                    }
                }
            }
            if (startDate.isBefore(actualDate)) {
                return;
            }

            Intent intentToSend = new Intent(this, AlarmNotification.class);
            intentToSend.setData(uri);

            intentToSend.putExtra(NOTIFICATION_ID, event.getId());
            intentToSend.putExtra(NOTIFICATION_TITLE, getResources().getString(R.string.event_alert_title));
            intentToSend.putExtra(NOTIFICATION_TEXT, event.getDescription());

            PendingIntent notificationIntent = PendingIntent.getBroadcast(this, 0, intentToSend, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.set(AlarmManager.RTC_WAKEUP, startDate.toDateTime().getMillis(), notificationIntent);
            Log.d(TAG, "Alarm was set for event with id: " + event.getId() + " on time: " + startDate);
        }
    }

    private void cancelAlarm(Uri uri) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intentToCancel = new Intent(this, AlarmNotification.class);
        intentToCancel.setData(uri);
        alarmManager.cancel(PendingIntent.getBroadcast(this, 0, intentToCancel, 0));
    }

    private DbHelper getDbHelper() {
        if (dbHelper == null) {
            dbHelper = OpenHelperManager.getHelper(this, DbHelper.class);
        }

        return dbHelper;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.wtf("WTF", "Why do you kill me?!");
    }
}
