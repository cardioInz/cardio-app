package cardio_app.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import cardio_app.activity.MainActivity;

import static cardio_app.util.IntentContentUtil.*;

public class AlarmNotification extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        String title = intent.getStringExtra(NOTIFICATION_TITLE);
        String text = intent.getStringExtra(NOTIFICATION_TEXT);

        Log.wtf("DWW", "Id alarmu: " + id);
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent returnToMainActivity = new Intent(context, MainActivity.class);

        NotificationCompat.Builder  builder = new NotificationCompat.Builder(context)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setContentTitle(title)
                .setContentText(text)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setContentIntent(PendingIntent.getActivity(context, 0, returnToMainActivity, 0));

        Log.wtf("Notify", "Set new notification");
        nm.notify(id, builder.build());
    }
}
