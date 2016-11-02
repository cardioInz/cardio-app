package cardio_app.viewmodel.date_time;

import android.databinding.BindingAdapter;
import android.widget.TimePicker;

/**
 * Created by kisam on 02.11.2016.
 */

public class TimePickerAdapter {
    @BindingAdapter({"android:hour", "android:minute", "android:onTimeChanged"})
    public static void setTime(TimePicker view, int hour, int minute,
                               TimePicker.OnTimeChangedListener listener) {
        view.setCurrentHour(hour);
        view.setCurrentMinute(minute);
        view.setOnTimeChangedListener(listener);
    }

    @BindingAdapter({"android:hour", "android:minute"})
    public static void setTime(TimePicker view, int hour, int minute) {
        view.setCurrentHour(hour);
        view.setCurrentMinute(minute);
    }
}
