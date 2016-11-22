package cardio_app.pdf_creation.param_models;

import android.util.Log;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import cardio_app.db.DbHelper;
import cardio_app.db.model.Event;
import cardio_app.db.model.PressureData;

/**
 * Created by kisam on 20.11.2016.
 */

public class PdfRecordsContainer {
    private static final String TAG = PdfRecordsContainer.class.toString();
    private List<PressureData> pressureDatas;
    private List<Event> eventsDatas;

    public PdfRecordsContainer(DbHelper dbHelper, Date dateFrom, Date dateTo){
        try {
            pressureDatas = dbHelper.getFilteredAndOrderedByDatePressureData(dateFrom, dateTo);
        } catch (SQLException e) {
            Log.e(TAG, "PdfRecordsContainer: can't get pressureData", e);
            e.printStackTrace();
        }
        try {
            eventsDatas = dbHelper.getFilteredAndOrderedByDateEvents(dateFrom, dateTo);
        } catch (SQLException e) {
            Log.e(TAG, "PdfRecordsContainer: can't get eventsData", e);
            e.printStackTrace();
        }
    }

    public List<PressureData> getPressureDatas() {
        return pressureDatas;
    }

    public List<Event> getEventsDatas() {
        return eventsDatas;
    }
}
