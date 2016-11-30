package cardio_app.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;


public class DateTimeUtil {

    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("HH:mm");
    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat DATETIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm");

}
