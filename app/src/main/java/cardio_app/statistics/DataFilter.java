package cardio_app.statistics;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by kisam on 04.11.2016.
 */

public class DataFilter {
    static final int DEFAULT_FILTER_LAST_X_DAYS = 14; // == X
    private static final String TAG = DataFilter.class.getName();

    private DataFilterModeEnum mode;
    private int xDays;
    private Date dateFrom;
    private Date dateTo;

    public DataFilter(){
        // DEFAULT FILTER
        setLastXDaysFilterMode(DEFAULT_FILTER_LAST_X_DAYS);
    }

    public DataFilter(Date dateFrom, Date dateTo) {
        setCustomDatesFilterMode(dateFrom, dateTo);
    }

    public DataFilter(DataFilterModeEnum mode){
        switch (mode){

            case THIS_MONTH:
                setThisMonthFilterMode();
                break;
            case THIS_YEAR:
                setThisYearFilterMode();
                break;
            case NO_FILTER:
                setNoFilterMode();
                break;
            default:
                setLastXDaysFilterMode(DEFAULT_FILTER_LAST_X_DAYS);
                Log.e(TAG, "Unexpected invocation: DataFielter initialized with param " + mode.toString());
                break;
        }
    }

    public DataFilter(int xDays){
        setLastXDaysFilterMode(xDays);
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
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, 1);
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
        this.mode = mode;
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

    public int getXDays() {
        return xDays;
    }

    public void setXDays(int xDays) {
        this.xDays = xDays;
    }
}
