package cardio_app.viewmodel.date_time;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by kisam on 02.11.2016.
 */

public class PickedTimeViewModel extends BaseObservable {
    private int hourOfDay;
    private int minute;

//    public PickedTimeViewModel() {
//        this(Calendar.getInstance());
//    }
//
//    public PickedTimeViewModel(Calendar calendar) {
//        initFromCalendar(calendar);
//    }

    public PickedTimeViewModel(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        initFromCalendar(calendar);
    }

    private void initFromCalendar(Calendar calendar) {
        hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
    }

    @Bindable
    public int getHourOfDay() {
        return this.hourOfDay;
    }

    public void setHourOfDay(int hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    @Bindable
    public int getMinute() {
        return this.minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void timeChanged(TimePicker view, int hourOfDay, int minute) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
    }

    public void setTime(Date dateTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateTime);
        initFromCalendar(cal);
    }
}
