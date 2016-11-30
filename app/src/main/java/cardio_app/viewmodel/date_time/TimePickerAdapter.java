package cardio_app.viewmodel.date_time;

import android.databinding.BindingAdapter;
import android.os.Build;
import android.widget.TimePicker;


public class TimePickerAdapter {
    @BindingAdapter({"android:hour", "android:minute", "android:onTimeChanged"})
    public static void setTime(TimePicker view, int hour, int minute,
                               TimePicker.OnTimeChangedListener listener) {
        setTime(view, hour, minute);
        view.setOnTimeChangedListener(listener);
    }

    @BindingAdapter({"android:hour", "android:minute"})
    public static void setTime(TimePicker view, int hour, int minute) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setHour(hour);
            view.setMinute(minute);
        } else {
            view.setCurrentMinute(hour);
            view.setCurrentMinute(minute);
        }
    }
}
