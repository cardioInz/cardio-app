package cardio_app.pdf_creation.param_models;

import android.util.Log;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import cardio_app.db.DbHelper;
import cardio_app.db.model.Event;
import cardio_app.db.model.PressureData;
import cardio_app.db.model.UserProfile;
import cardio_app.util.DateTimeUtil;


public class PdfRecordsContainer {
    private static final String TAG = PdfRecordsContainer.class.toString();
    private DbHelper dbHelper;
    private Date dateFrom;
    private Date dateTo;
    private List<PressureData> pressureDataList;
    private List<Event> eventsDataList;
    private UserProfile userProfile = null;

    public PdfRecordsContainer(DbHelper dbHelper, Date dateFrom, Date dateTo) {
        this.dbHelper = dbHelper;
        setDateFrom(dateFrom);
        setDateTo(dateTo);
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public DbHelper getDbHelper() {
        return dbHelper;
    }

    public void initUserProfileByHelper() {
        try {
            userProfile = dbHelper.getUserProfile();
        } catch (SQLException e) {
            Log.e(TAG, "initUserProfileByHelper: can't get userProfile", e);
            e.printStackTrace();
        }
    }

    public void initPressureDataByHelper() {
        try {
            pressureDataList = dbHelper.getFilteredAndOrderedByDatePressureData(dateFrom, dateTo);
        } catch (SQLException e) {
            Log.e(TAG, "initPressureDataByHelper: can't get pressureData", e);
            e.printStackTrace();
        }
    }

    public void initEventDataByHelper() {
        try {
            List<Event> list = dbHelper.getEventsBetween(dateFrom, dateTo);
            if (list.size() != 0) {
                eventsDataList = Event.multiplyRepeatableEvents(list);
                Collections.sort(eventsDataList, Event.compareStartDate);
            } else {
                eventsDataList = list;
                Log.i(TAG, "initEventDataByHelper: no events has been found between: " +
                        DateTimeUtil.DATE_FORMATTER.format(dateFrom) + " - " + DateTimeUtil.DATE_FORMATTER.format(dateTo));
            }
        } catch (SQLException e) {
            Log.e(TAG, "initEventDataByHelper: can't get eventsData", e);
            e.printStackTrace();
        }
    }

    public List<PressureData> getPressureDataList() {
        return pressureDataList;
    }

    public List<Event> getEventsDataList() {
        return eventsDataList;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setDateFrom(Date dateFrom) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(dateFrom);
            if (this.dateFrom == null)
                this.dateFrom = c.getTime();
            else
                this.dateFrom.setTime(c.getTimeInMillis());
        } catch (Exception e) {
            this.dateFrom = null;
        }
    }

    public void setDateTo(Date dateTo) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(dateTo);
            if (this.dateTo == null)
                this.dateTo = c.getTime();
            else
                this.dateTo.setTime(c.getTimeInMillis());
        } catch (Exception e) {
            this.dateTo = null;
        }
    }

    public String getInfoForLogger() {
        return DateTimeUtil.DATE_FORMATTER.format(dateFrom) + " - " + DateTimeUtil.DATE_FORMATTER.format(dateTo);
    }
}
