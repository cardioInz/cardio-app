package cardio_app.pdf_creation.param_models;

import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cardio_app.db.DbHelper;
import cardio_app.db.model.Event;
import cardio_app.db.model.PressureData;
import cardio_app.db.model.UserProfile;
import cardio_app.util.BitmapUtil;


public class PdfRecordsContainer {
    private static final String TAG = PdfRecordsContainer.class.toString();
    private DbHelper dbHelper;
    private Date dateFrom;
    private Date dateTo;
    private List<PressureData> pressureDataList;
    private List<Event> eventsDataList;
    private List<BitmapFromChart> bitmapFromChartList = new ArrayList<>();
    private List<BitmapFromChart> extraBitmapCharts = new ArrayList<>();
    private UserProfile userProfile = null;

    public PdfRecordsContainer(DbHelper dbHelper, Date dateFrom, Date dateTo){
        this.dbHelper = dbHelper;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
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

    public void initRecordsByHelper(){
        try {
            pressureDataList = dbHelper.getFilteredAndOrderedByDatePressureData(dateFrom, dateTo);
        } catch (SQLException e) {
            Log.e(TAG, "initRecordsByHelper: can't get pressureData", e);
            e.printStackTrace();
        }
        try {
            eventsDataList = dbHelper.getFilteredAndOrderedByDateEvents(dateFrom, dateTo);
        } catch (SQLException e) {
            Log.e(TAG, "initRecordsByHelper: can't get eventsData", e);
            e.printStackTrace();
        }

        try {
            userProfile = dbHelper.getUserProfile();
        } catch (SQLException e) {
            Log.e(TAG, "initRecordsByHelper: can't get userProfile", e);
            e.printStackTrace();
        }
    }

    public List<PressureData> getPressureDataList() {
        return pressureDataList;
    }

    public List<Event> getEventsDataList() {
        return eventsDataList;
    }

    public void addSimple_BitmapFromChart(BitmapFromChart bitmapFromChart){
        if (bitmapFromChart == null)
            return;
        bitmapFromChartList.add(bitmapFromChart);
    }

    public void addExtra_BitmpaFromChart(BitmapFromChart bitmapFromChart){
        if (bitmapFromChart == null)
            return;
        extraBitmapCharts.add(bitmapFromChart);
    }

    public List<BitmapFromChart> getSimple_bitmapFromChartList() {
        return bitmapFromChartList;
    }

    public void setExtra_bitmapFromChartList(List<BitmapFromChart> extraBitmapCharts) {
        this.extraBitmapCharts.clear();;
        this.extraBitmapCharts.addAll(extraBitmapCharts);
    }

    public List<BitmapFromChart> getExtra_bitmapFromChartList() {
        return extraBitmapCharts;
    }

    private static void cleanBitmapWithoutPaths(List<BitmapFromChart> list){
        List<BitmapFromChart> newList = new ArrayList<>();

        for (BitmapFromChart fromChart : list) {
            if (fromChart.hasFilePathExt()){
                if (BitmapUtil.loadBitmapFromFile(fromChart))
                    newList.add(fromChart);
            }
            else {
                Log.w(TAG, "makeBitmaps: values not completed\n" + fromChart.infoStrForLogger());
            }
        }

        list.clear();
        list.addAll(newList);
    }

    public void cleanExtraBitmapWithoutPaths(){
        cleanBitmapWithoutPaths(extraBitmapCharts);
    }

    public void cleanSimpleBitmapWithoutPaths(){
        cleanBitmapWithoutPaths(bitmapFromChartList);
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }
}
