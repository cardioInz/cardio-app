package cardio_app.pdf_creation.param_models;

import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
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
    private List<PressureData> pressureDataList;
    private List<Event> eventsDataList;
    private List<BitmapFromChart> bitmapFromChartList = new ArrayList<>();

    public PdfRecordsContainer(DbHelper dbHelper, Date dateFrom, Date dateTo){
        try {
            pressureDataList = dbHelper.getFilteredAndOrderedByDatePressureData(dateFrom, dateTo);
        } catch (SQLException e) {
            Log.e(TAG, "PdfRecordsContainer: can't get pressureData", e);
            e.printStackTrace();
        }
        try {
            eventsDataList = dbHelper.getFilteredAndOrderedByDateEvents(dateFrom, dateTo);
        } catch (SQLException e) {
            Log.e(TAG, "PdfRecordsContainer: can't get eventsData", e);
            e.printStackTrace();
        }
    }

    public List<PressureData> getPressureDataList() {
        return pressureDataList;
    }

    public List<Event> getEventsDataList() {
        return eventsDataList;
    }

    public void addBitmapFromChart(BitmapFromChart bitmapFromChart){
        if (bitmapFromChart == null)
            return;
        bitmapFromChartList.add(bitmapFromChart);
    }

    public List<BitmapFromChart> getBitmapFromChartList() {
        return bitmapFromChartList;
    }

//    public void makeBitmapsAndCleanUseless(){
//        List<BitmapFromChart> toDelete = new ArrayList<>();
//
//        for (BitmapFromChart fromChart : bitmapFromChartList) {
//            if (fromChart.hasCompletedValues()){
//                if (fromChart.getBitmap() == null){
//                    fromChart.m
//                }
//            }
//            else {
//                toDelete.add(fromChart);
//                Log.w(TAG, "makeBitmaps: values not completed\n" + fromChart.infoStrForLogger());
//            }
//        }
//
//        bitmapFromChartList.removeAll(toDelete);
//        toDelete.clear();
//    }
}
