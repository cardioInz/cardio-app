package cardio_app.util;

import android.annotation.SuppressLint;

import org.joda.time.LocalDateTime;

import java.text.SimpleDateFormat;

import cardio_app.db.model.TimeUnit;


public class DateTimeUtil {

    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("HH:mm");
    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat DATETIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static LocalDateTime increaseDate(LocalDateTime dateTime, TimeUnit timeUnit, int delta) {
        if (delta <= 0) {
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
}
