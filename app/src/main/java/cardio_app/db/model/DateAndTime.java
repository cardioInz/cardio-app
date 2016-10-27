package cardio_app.db.model;

import com.j256.ormlite.field.DatabaseField;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kisam on 27.10.2016.
 */

public class DateAndTime extends BaseModel {

    private static int ID_CNT = 0;

    @DatabaseField
    private String timeStr;
    @DatabaseField
    private String dateStr;


    public DateAndTime(String dateStr, String timeStr) {
        super(++ID_CNT);
        this.dateStr = dateStr;
        this.timeStr = timeStr;
    }

    public DateAndTime(Date date) {
        super(++ID_CNT);
        this.dateStr = makeDateStr(date);
        this.timeStr = makeTimeStr(date);
    }

    private static String makeDateStr(Date date) {
        final String DATE_PATTERN = "yyyy-MM-dd";
        return new SimpleDateFormat(DATE_PATTERN).format(date);
    }

    private static String makeTimeStr(Date date) {
        final String TIME_PATTERN = "HH:mm";
        return new SimpleDateFormat(TIME_PATTERN).format(date);
    }

    public String getDateStr() {
        return dateStr;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public int compareToDateTime(DateAndTime that) {
        // do not implement this method as comparable because of unique ID field that occurs in BaseModel
        int result = this.dateStr.compareTo(that.getDateStr());
        if (result == 0) {
            result = this.timeStr.compareTo(that.getTimeStr());
        }
        return result;
    }
}
