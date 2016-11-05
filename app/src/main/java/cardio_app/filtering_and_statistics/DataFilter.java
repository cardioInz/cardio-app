package cardio_app.filtering_and_statistics;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kisam on 04.11.2016.
 */

public class DataFilter implements Parcelable {
    static final int DEFAULT_FILTER_LAST_X_DAYS = 14; // == X
    private static final String TAG = DataFilter.class.getName();

    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    private DataFilterModeEnum mode;
    private Date dateFrom;
    private Date dateTo;
    private int xDays;

    public DataFilter(){
        // DEFAULT FILTER
        setLastXDaysFilterMode(DEFAULT_FILTER_LAST_X_DAYS);
    }

    public DataFilter(Date dateFrom, Date dateTo) {
        setCustomDatesFilterMode(dateFrom, dateTo);
    }

    public DataFilter(DataFilterModeEnum mode){
        setMode(mode);
    }

    public DataFilter(int xDays){
        setLastXDaysFilterMode(xDays);
    }


    protected DataFilter(Parcel in) {
        try {
            mode = DataFilterModeEnum.valueOf(in.readString());
            dateFrom = DATE_FORMATTER.parse(in.readString());
            dateTo = DATE_FORMATTER.parse(in.readString());
            xDays = in.readInt();
        } catch (ParseException e) {
            Log.e(TAG, "DataFilter: cannot parse parcel to create object", e);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DataFilter> CREATOR = new Creator<DataFilter>() {
        @Override
        public DataFilter createFromParcel(Parcel in) {
            return new DataFilter(in);
        }

        @Override
        public DataFilter[] newArray(int size) {
            return new DataFilter[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mode.name());
        parcel.writeString(DATE_FORMATTER.format(dateFrom));
        parcel.writeString(DATE_FORMATTER.format(dateTo));
        parcel.writeInt(xDays);
    }

    public void setLastXDaysFilterMode(int xDays){
        this.xDays = xDays;
        this.mode = DataFilterModeEnum.LAST_X_DAYS;

        Calendar calendar = Calendar.getInstance();
        this.dateTo = calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH, -this.xDays);
        dateFrom = calendar.getTime();
    }

    public void setThisMonthFilterMode(){
        this.mode = DataFilterModeEnum.THIS_MONTH;

        Calendar calendar = Calendar.getInstance();
        this.dateTo = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        this.dateFrom = calendar.getTime();
    }

    public void setThisYearFilterMode(){
        this.mode = DataFilterModeEnum.THIS_YEAR;

        Calendar calendar = Calendar.getInstance();
        this.dateTo = calendar.getTime();

        calendar.set(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        this.dateFrom = calendar.getTime();
    }

    public void setNoFilterMode(){
        this.mode = DataFilterModeEnum.NO_FILTER;
        this.dateTo = null;
        this.dateFrom = null;
    }

    public void setCustomDatesFilterMode(Date dateFrom, Date dateTo){
        this.mode = DataFilterModeEnum.CUSTOM_DATES;
        this.dateTo = dateTo;
        this.dateFrom = dateFrom;
    }

    public DataFilterModeEnum getMode() {
        return mode;
    }

    public void setMode(DataFilterModeEnum mode) {
        switch (mode) {
            case LAST_X_DAYS:
                setLastXDaysFilterMode(DEFAULT_FILTER_LAST_X_DAYS);
                Log.w(TAG, "setMode: " + mode.name() + ", with 'default' x = " + DEFAULT_FILTER_LAST_X_DAYS);
                break;
            case THIS_MONTH:
                setThisMonthFilterMode();
                break;
            case THIS_YEAR:
                setThisYearFilterMode();
                break;
            case CUSTOM_DATES:
                // nothing to do, so sad :(
                this.mode = mode;
                Log.w(TAG, "setMode: " + mode.name() + ", without 'date' parameters");
                break;
            case NO_FILTER:
                setNoFilterMode();
                break;
            default:
                final String modeStr = mode == null ? "null" : mode.name();
                Log.e(TAG, "Unexpected invocation: try to set mode with param " + modeStr);
                break;
        }

        Log.i(TAG, "setMode: " + mode.name()
                + ", dateFrom: " + DATE_FORMATTER.format(dateFrom)
                + ", dateTo: " + DATE_FORMATTER.format(dateTo));
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

//    private int getXDays() {
//        return xDays;
//    }
//
//    private void setXDays(int xDays) {
//        this.xDays = xDays;
//    }
}
