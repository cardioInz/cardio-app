package cardio_app.viewmodel.date_time;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.Calendar;
import java.util.Date;

import cardio_app.util.DateTimeUtil;


public class DateTimeViewModel extends BaseObservable {


    private final PickedDateViewModel pickedDateViewModel;
    private final PickedTimeViewModel pickedTimeViewModel;

    public DateTimeViewModel() {
        this(new Date());
    }

    public DateTimeViewModel(Date dateTime) {
        pickedDateViewModel = new PickedDateViewModel(dateTime);
        pickedTimeViewModel = new PickedTimeViewModel(dateTime);
    }

    private static String makeDateStr(Date date) {
        return DateTimeUtil.DATE_FORMATTER.format(date);
    }

    private static String makeTimeStr(Date date) {
        return DateTimeUtil.TIME_FORMATTER.format(date);
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

    public void setMonth(int month) {
        pickedDateViewModel.setMonth(month);
    }

    @Bindable
    public int getDay() {
        return pickedDateViewModel.getDay();
    }

    public void setDay(int day) {
        pickedDateViewModel.setDay(day);
    }

    @Bindable
    public int getYear() {
        return pickedDateViewModel.getYear();
    }

    public void setYear(int year) {
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

    @Bindable
    public String getDateStr() {
        return makeDateStr(this.getDateTime());
    }

    @Bindable
    public String getTimeStr() {
        return makeTimeStr(this.getDateTime());
    }
}