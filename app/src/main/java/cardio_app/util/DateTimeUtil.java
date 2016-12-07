package cardio_app.util;

import android.annotation.SuppressLint;
import android.util.Log;

import org.joda.time.LocalDateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cardio_app.db.model.TimeUnit;

import static android.content.ContentValues.TAG;


public class DateTimeUtil {

    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("HH:mm");
    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat DATETIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static LocalDateTime increaseDate(LocalDateTime dateTime, TimeUnit timeUnit, int delta) {
        if (delta <= 0) {
            Log.e(TAG, "increaseDate: delta = " + delta);
            throw new RuntimeException("Delta cannot be negative!");
        }
        switch (timeUnit) {
            case DAY:
                return dateTime.plusDays(delta);
            case WEEK:
                return dateTime.plusWeeks(delta);
            case MONTH:
                return dateTime.plusMonths(delta);
            default:
                return dateTime.plusDays(delta);
        }
    }

    public static Date getDate(int day, int month, int year) {
        Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(year, month, day);
        return myCalendar.getTime();
    }
}
