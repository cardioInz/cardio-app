package cardio_app.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import cardio_app.activity.MainActivity;
import cardio_app.db.model.Alarm;

public class NotifyAlarmService extends IntentService {

    public NotifyAlarmService() {
        super(NotifyAlarmService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Alarm alarm = intent.getParcelableExtra("alarm");

        Intent returnToMainActivity = new Intent(this, MainActivity.class);

        NotificationCompat.Builder  builder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setContentTitle("Title")
                .setContentText(alarm.getDescription())
                .setContentIntent(PendingIntent.getActivity(this, 0, returnToMainActivity, 0));

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(alarm.getId(), builder.build());
    }
}
