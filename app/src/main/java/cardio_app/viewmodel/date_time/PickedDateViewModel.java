package cardio_app.viewmodel.date_time;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;


public class PickedDateViewModel extends BaseObservable {

    private int year;
    private int month;
    private int day;


    public PickedDateViewModel(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        initFromCalendar(calendar);
    }


    private void initFromCalendar(Calendar calendar) {
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public void dateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;
    }

    @Bindable
    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Bindable
    public int getMonth() {
        return this.month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    @Bindable
    public int getDay() {
        return this.day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setDate(Date dateTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateTime);
        initFromCalendar(cal);
    }

}
