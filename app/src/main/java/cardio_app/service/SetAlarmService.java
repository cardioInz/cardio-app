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

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cardio_app.R;
import cardio_app.activity.MainActivity;
import cardio_app.db.DbHelper;
import cardio_app.db.model.Event;
import cardio_app.db.model.TimeUnit;
import cardio_app.receiver.AlarmNotification;

import static cardio_app.util.IntentContentUtil.NOTIFICATION_ID;
import static cardio_app.util.IntentContentUtil.NOTIFICATION_TEXT;
import static cardio_app.util.IntentContentUtil.NOTIFICATION_TITLE;

public class SetAlarmService extends Service {
    private static final int SERVICE_NOTIFICATION_ID = 101;

    private DbHelper dbHelper;

    private static int convertToCalendarUnit(TimeUnit timeUnit) {
        switch (timeUnit) {
            case DAY:
                return Calendar.DAY_OF_YEAR;
            case WEEK:
                return Calendar.WEEK_OF_YEAR;
            case MONTH:
                return Calendar.MONTH;
            default:
                return Calendar.DAY_OF_YEAR;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent foregroundIntent = new Intent(this, MainActivity.class);
        PendingIntent pForegroundIntent = PendingIntent.getActivity(this, 0, foregroundIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle("Cardio App")
                .setContentText("ustawiono alarmy")
                .setSmallIcon(android.R.drawable.btn_plus)
                .setOngoing(true)
                .setContentIntent(pForegroundIntent);
        startForeground(SERVICE_NOTIFICATION_ID, builder.build());

        try {
            Dao<Event, Integer> eventDao = getDbHelper().getDao(Event.class);

            if ("UPDATE".equals(intent.getAction())) {
                Event element = eventDao.queryForId(intent.getIntExtra("eventId", 0));

                setAlarm(element);
            }

            List<Event> allEvents = eventDao.queryForAll();

            for (Event event : allEvents) {
                setAlarm(event);
            }

//            Dao<AlarmDrug, Integer> dao = getDbHelper().getDao(AlarmDrug.class);
//
//            List<AlarmDrug> list = dao.queryForAll();
//
//            if (list.isEmpty()) {
//                stopSelf();
//                return START_NOT_STICKY;
//            }
//
//            Comparator<BaseModel> comparator = (m1, m2) -> m1.getId() - m2.getId();
//
//            List<Alarm> alarmList = getDbHelper().getDao(Alarm.class).queryForAll();
//            Collections.sort(alarmList, comparator);
//            List<Drug> drugList = getDbHelper().getDao(Drug.class).queryForAll();
//            Collections.sort(drugList, comparator);
//
//            Map<Alarm, List<Drug>> map = new HashMap<>();
//            List<Drug> value;
//            Alarm currentAlarm;
//            Drug currentDrug;
//            for (AlarmDrug alarmDrug : list) {
//                int alarmId = Collections.binarySearch(alarmList, alarmDrug.getAlarm(), comparator);
//                currentAlarm = alarmList.get(alarmId);
//                int drugId = Collections.binarySearch(drugList, alarmDrug.getDrug(), comparator);
//                currentDrug = drugList.get(drugId);
//                if (!map.containsKey(currentAlarm)) {
//                    value = new ArrayList<>();
//                    value.add(currentDrug);
//                    map.put(currentAlarm, value);
//                } else {
//                    value = map.get(currentAlarm);
//                    value.add(currentDrug);
//                }
//            }

//            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//            for (Map.Entry<Alarm, List<Drug>> entry : map.entrySet()) {
//                int id = entry.getKey().getId();
//                String title = entry.getKey().getName();
//                StringBuilder text = new StringBuilder();
//                for (Drug drug : entry.getValue()) {
//                    text.append(drug.getName()).append("\n");
//                }
//
//                Calendar timeOfNotification = Calendar.getInstance();
//                timeOfNotification.setTimeInMillis(System.currentTimeMillis());
//                timeOfNotification.set(Calendar.HOUR_OF_DAY, entry.getKey().getHour());
//                timeOfNotification.set(Calendar.MINUTE, entry.getKey().getMinute());
//                timeOfNotification.set(Calendar.SECOND, 0);
//
//                if (timeOfNotification.getTimeInMillis() < System.currentTimeMillis()) {
//                    timeOfNotification.add(Calendar.DATE, 1);
//                }
//
//                Intent notifyIntent = new Intent(this, AlarmNotification.class);
//                Uri uri = new Uri.Builder().path("/" + entry.getKey().getId()).build();
//                notifyIntent.setData(uri);
//                notifyIntent.putExtra(NOTIFICATION_ID, id);
//                notifyIntent.putExtra(NOTIFICATION_TITLE, title);
//                notifyIntent.putExtra(NOTIFICATION_TEXT, text.toString());
//
//                PendingIntent notify = PendingIntent.getBroadcast(this, 0, notifyIntent, 0);
//
//                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeOfNotification.getTimeInMillis(), AlarmManager.INTERVAL_DAY, notify);
//                Log.wtf("WTF", "Czas aktualny: " + System.currentTimeMillis() + " ustawiony: " + timeOfNotification.getTimeInMillis());
//            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Log.wtf("SetAlarm", "Alarm was set");

        return START_STICKY;
    }

    private void setAlarm(Event event) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent intentToCancel = new Intent(this, AlarmNotification.class);
        Uri uri = new Uri.Builder().path(String.valueOf(event.getId())).build();
        intentToCancel.setData(uri);

        alarmManager.cancel(PendingIntent.getBroadcast(this, 0, intentToCancel, 0));

        //if event has set alarm
        if (event.getEndDate().after(new Date())) {
            Calendar calendar = Calendar.getInstance();
            if (event.isRepeatable()) {
                calendar.setTime(event.getStartDate());

                int timeUnit = convertToCalendarUnit(event.getTimeUnit());

                while (calendar.before(Calendar.getInstance())) {
                    calendar.add(timeUnit, event.getTimeDelta());
                }

                Calendar endDate = Calendar.getInstance();
                endDate.setTime(event.getEndDate());

                if (calendar.after(endDate)) {
                    return;
                }
            } else if (event.getStartDate().after(new Date())) {
                calendar.setTime(event.getStartDate());
            } else {
                return;
            }
            Intent intentToSend = new Intent(this, AlarmNotification.class);
            intentToSend.setData(uri);

            intentToSend.putExtra(NOTIFICATION_ID, event.getId());
            intentToSend.putExtra(NOTIFICATION_TITLE, getResources().getString(R.string.event_alert_title));
            intentToSend.putExtra(NOTIFICATION_TEXT, event.getDescription());

            PendingIntent notificationIntent = PendingIntent.getBroadcast(this, 0, intentToSend, 0);

            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), notificationIntent);
        }
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
