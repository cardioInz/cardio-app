package cardio_app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import cardio_app.R;
import cardio_app.service.SetAlarmService;

public class SystemStartup extends BroadcastReceiver {
    public SystemStartup() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, SetAlarmService.class);
        service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startService(service);

        Toast.makeText(context, R.string.enable_service, Toast.LENGTH_SHORT).show();
    }
}
