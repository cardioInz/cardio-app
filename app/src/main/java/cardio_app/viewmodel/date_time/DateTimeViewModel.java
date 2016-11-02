package cardio_app.viewmodel.date_time;

import android.annotation.SuppressLint;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kisam on 01.11.2016.
 */

public class DateTimeViewModel extends BaseObservable {

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("HH:mm");
    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat DATETIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private final PickedDateViewModel pickedDateViewModel;
    private final PickedTimeViewModel pickedTimeViewModel;

    public DateTimeViewModel() {
        this(new Date());
    }

    public DateTimeViewModel(Date dateTime) {
        pickedDateViewModel = new PickedDateViewModel(dateTime);
        pickedTimeViewModel = new PickedTimeViewModel(dateTime);
    }

    @Bindable
    private Date getDateTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(
                pickedDateViewModel.getYear(),
                pickedDateViewModel.getMonth(),
                pickedDateViewModel.getDay(),
                pickedTimeViewModel.getHourOfDay(),
                pickedTimeViewModel.getMinute(),
                0 // seconds
        );
        return cal.getTime();
    }

    private void setDateTime(Date dateTime) {
        this.pickedDateViewModel.setDate(dateTime);
        this.pickedTimeViewModel.setTime(dateTime);
    }

    @Bindable
    public int getMonth() {
        return pickedDateViewModel.getMonth();
    }

    public void setMonth(int month){
        pickedDateViewModel.setMonth(month);;
    }

    @Bindable
    public int getDay() {
        return pickedDateViewModel.getDay();
    }

    public void setDay(int day){
        pickedDateViewModel.setDay(day);
    }

    @Bindable
    public int getYear() {
        return pickedDateViewModel.getYear();
    }

    public void setYear(int year){
        pickedDateViewModel.setYear(year);
    }

    @Bindable
    public int getHourOfDay() {
        return pickedTimeViewModel.getHourOfDay();
    }

    public void setHourOfDay(int hourOfDay) {
        pickedTimeViewModel.setHourOfDay(hourOfDay);
    }

    @Bindable
    public int getMinute() {
        return pickedTimeViewModel.getMinute();
    }

    public void setMinute(int minute) {
        pickedTimeViewModel.setMinute(minute);
    }

    private static String makeDateStr(Date date) {
        return DATE_FORMATTER.format(date);
    }

    private static String makeTimeStr(Date date) {
        return TIME_FORMATTER.format(date);
    }

    @Bindable
    public String getDateStr() {
        return makeDateStr(this.getDateTime());
    }

    @Bindable
    public String getTimeStr() {
        return makeTimeStr(this.getDateTime());
    }
}