package cardio_app.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;

import java.util.Calendar;

import cardio_app.db.model.Alarm;

public class SetAlarmService extends IntentService {

    public SetAlarmService() {
        super(SetAlarmService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Alarm alarm = intent.getParcelableExtra("alarm");

        Calendar timeOfNotification = Calendar.getInstance();
        timeOfNotification.setTimeInMillis(System.currentTimeMillis());
        timeOfNotification.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        timeOfNotification.set(Calendar.MINUTE, alarm.getMinute());

        Intent notifyIntent = new Intent(this, NotifyAlarmService.class);

        PendingIntent notify = PendingIntent.getService(this, 0, notifyIntent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeOfNotification.getTimeInMillis(), AlarmManager.INTERVAL_DAY, notify);
    }
}
