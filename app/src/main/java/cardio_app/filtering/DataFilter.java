package cardio_app.filtering;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import static cardio_app.util.DateTimeUtil.DATE_FORMATTER;


public class DataFilter implements Parcelable {

    private static final String TAG = DataFilter.class.getName();
    private DataFilterModeEnum mode;
    private Date dateFrom;
    private Date dateTo;
    private int xDays;


    public void copyValues(DataFilter dataFilter) {
        this.mode = dataFilter.getMode();
        this.dateFrom = dataFilter.getDateFrom();
        this.dateTo = dataFilter.getDateTo();
        this.xDays = dataFilter.getXDays();
    }

    public DataFilter(Date dateFrom, Date dateTo) {
        setCustomDatesFilterMode(dateFrom, dateTo);
    }

    public DataFilter(DataFilterModeEnum mode) {
        setMode(mode);
    }

    public DataFilter(int xDays) {
        setLastXDaysFilterMode(xDays);
    }


    protected DataFilter(Parcel in) {
        try {
            DataFilterModeEnum mode = DataFilterModeEnum.valueOf(in.readString());
            switch (mode) {
                case LAST_X_DAYS:
                    int xDays = in.readInt();
                    setLastXDaysFilterMode(xDays);
                    break;
                case CUSTOM_DATES:
                    Date dateFrom = DATE_FORMATTER.parse(in.readString());
                    Date dateTo = DATE_FORMATTER.parse(in.readString());
                    setCustomDatesFilterMode(dateFrom, dateTo);
                    break;
                default:
                    setMode(mode);
                    break;
            }
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
        switch (mode) {
            case CUSTOM_DATES:
                parcel.writeString(DATE_FORMATTER.format(dateFrom));
                parcel.writeString(DATE_FORMATTER.format(dateTo));
                break;
            case LAST_X_DAYS:
                parcel.writeInt(xDays);
                break;
            default:
                break;
        }
    }

    public void setLastXDaysFilterMode(int xDays) {
        this.xDays = xDays;
        this.mode = DataFilterModeEnum.LAST_X_DAYS;

        Calendar calendar = Calendar.getInstance();
        this.dateTo = calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH, -this.xDays);
        dateFrom = calendar.getTime();
    }

    public void setThisMonthFilterMode() {
        this.mode = DataFilterModeEnum.THIS_MONTH;

        Calendar calendar = Calendar.getInstance();
        this.dateTo = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        this.dateFrom = calendar.getTime();
    }

    public void setThisYearFilterMode() {
        this.mode = DataFilterModeEnum.THIS_YEAR;

        Calendar calendar = Calendar.getInstance();
        this.dateTo = calendar.getTime();

        calendar.set(calendar.get(Calendar.YEAR), 1, 1);
        this.dateFrom = calendar.getTime();
    }

    public void setNoFilterMode() {
        this.mode = DataFilterModeEnum.NO_FILTER;
        this.dateTo = null;
        this.dateFrom = null;
    }

    public void setCustomDatesFilterMode(Date dateFrom, Date dateTo) {
        this.mode = DataFilterModeEnum.CUSTOM_DATES;
        this.dateTo = dateTo;
        this.dateFrom = dateFrom;
    }

    public DataFilterModeEnum getMode() {
        return mode;
    }

    public String getDateFromStr() {
        if (dateFrom == null)
            return "-";
        return DATE_FORMATTER.format(dateFrom);
    }

    public String getDateToStr() {
        if (dateTo == null)
            return "-";
        return DATE_FORMATTER.format(dateTo);
    }

    public void setMode(DataFilterModeEnum mode) {
        switch (mode) {
            case LAST_X_DAYS:
                setLastXDaysFilterMode(xDays);
                Log.w(TAG, "setMode: " + mode.name() + ", with 'default' x = " + xDays);
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

    // just for development
    public String getFilterMsgForLogger() {
        if (mode == DataFilterModeEnum.NO_FILTER)
            return "Mode: " + mode.name();
        else
            return String.format("     Mode : %s\nDate from : %s\n  Date to : %s",
                    getModeStr(), getDateFromStr(), getDateToStr());
    }

    public String getModeStr() {
        if (mode != null) {
            String modeName = mode.name();
            if (mode == DataFilterModeEnum.LAST_X_DAYS)
                modeName = modeName.replace("X", String.valueOf(xDays));
            return modeName;
        } else {
            return "null";
        }
    }

    public int getXDays() {
        return xDays;
    }

    private void setXDays(int xDays) {
        this.xDays = xDays;
    }
}
