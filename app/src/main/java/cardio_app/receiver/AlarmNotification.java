package cardio_app.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import cardio_app.activity.MainActivity;
import cardio_app.service.SetAlarmService;

import static cardio_app.util.IntentContentUtil.NOTIFICATION_ID;
import static cardio_app.util.IntentContentUtil.NOTIFICATION_TEXT;
import static cardio_app.util.IntentContentUtil.NOTIFICATION_TITLE;

public class AlarmNotification extends BroadcastReceiver {
    private static final String TAG = AlarmNotification.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        String title = intent.getStringExtra(NOTIFICATION_TITLE);
        String text = intent.getStringExtra(NOTIFICATION_TEXT);

        Log.wtf(TAG, "Id alarmu: " + id);
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent returnToMainActivity = new Intent(context, MainActivity.class);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setContentTitle(title)
                .setContentText(text)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setContentIntent(PendingIntent.getActivity(context, 0, returnToMainActivity, 0));

        Log.wtf(TAG, "Set new notification");
        nm.notify(id, builder.build());

        Intent returnIntent = new Intent(context, SetAlarmService.class);
        intent.setAction(SetAlarmService.UPDATE);
        intent.putExtra(SetAlarmService.EVENT_ID, id);

        context.startService(returnIntent);
    }
}
