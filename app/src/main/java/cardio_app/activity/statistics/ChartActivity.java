package cardio_app.activity.statistics;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

import cardio_app.R;
import cardio_app.db.DbHelper;
import cardio_app.db.model.BaseModel;
import cardio_app.db.model.Event;
import cardio_app.db.model.PressureData;
import cardio_app.filtering.DataFilter;
import cardio_app.filtering.DataFilterModeEnum;
import cardio_app.pdf_creation.SaveBitmapFromChartAsyncWorker;
import cardio_app.pdf_creation.param_models.BitmapFromChart;
import cardio_app.util.ChartBuilder;
import cardio_app.util.Defaults;
import cardio_app.util.FileWalkerUtil;
import cardio_app.util.PermissionUtil;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class ChartActivity extends AppCompatActivity {
    private static final String TAG = ChartActivity.class.getName();
    List<PressureData> pressureList;
    List<Event> eventList;
    private DataFilter dataFilter = Defaults.getDefaultDataFilter();
    private DbHelper dbHelper;
    private LineChartView lineChartView;
    private ChartBuilder chartBuilder;

    private float minDaysOnScreen = 1f;
    private float initialDaysOnScreen = 4f;
    private boolean collectedChartsInvoked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        Intent intent = getIntent();
        DataFilter df = intent.getParcelableExtra("filterdata");
        if (df == null) {
            Log.e(TAG, "onCreate: filterdata is null, maybe not passed, lol ?");
        } else {
            dataFilter = df;
        }

        try {
            collectedChartsInvoked = intent.getBooleanExtra("collectedChartsInvoked", false);
        } catch (Exception e) {
            Log.i(TAG, "onCreate: collectedChartsInvoked not passed");
        }

        if (collectedChartsInvoked)
            setTitle(getText(R.string.title_activity_get_chart_to_pdf));
        lineChartView = (LineChartView) findViewById(R.id.chart_view);

        lineChartView.setZoomType(ZoomType.HORIZONTAL);

        lineChartView.setOnValueTouchListener(new LineChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int i, int i1, PointValue pointValue) {
                if (pointValue instanceof CustomPointValue) {
                    CustomPointValue point = (CustomPointValue) pointValue;
                    if (point.getPressureData() instanceof PressureData) {
                        PressureData data = (PressureData) point.getPressureData();

                        DataDialog dialog = DataDialog.newInstance(data);

                        dialog.show(getSupportFragmentManager(), "measurement");
                    } else if (point.getPressureData() instanceof Event) {
                        Event event = (Event) point.getPressureData();

                        EventDialog dialog = EventDialog.newInstance(event);

                        dialog.show(getSupportFragmentManager(), "measurement");
                    }
                }
            }

            @Override
            public void onValueDeselected() {
            }
        });

        try {
            if (dataFilter.getMode().equals(DataFilterModeEnum.NO_FILTER)) {
                pressureList = getHelper().getAllOrderedPressureData();
                eventList = getHelper().getAllOrderedEventData();
            } else {
                pressureList = getHelper().getFilteredAndOrderedByDatePressureData(dataFilter.getDateFrom(), dataFilter.getDateTo());
                eventList = getHelper().getFilteredAndOrderedByDateEvents(dataFilter.getDateFrom(), dataFilter.getDateTo());
            }

            this.chartBuilder = new ChartBuilder(pressureList, getResources()).setEvents(eventList);

            changeType(ChartBuilder.ChartMode.DISCRETE, false, false);
            lineChartView.setMaxZoom(chartBuilder.getDays() / minDaysOnScreen);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (dbHelper != null) {
            OpenHelperManager.releaseHelper();
            dbHelper = null;
        }
    }

    private DbHelper getHelper() {
        if (dbHelper == null) {
            dbHelper = OpenHelperManager.getHelper(this, DbHelper.class);
        }

        return dbHelper;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.chart_discrete: {
                changeType(ChartBuilder.ChartMode.DISCRETE, true, false);
                return true;
            }
            case R.id.chart_continuous: {
                changeType(ChartBuilder.ChartMode.CONTINUOUS, true, false);
                return true;
            }
            case R.id.has_labels: {
                changeType(chartBuilder.getMode(), true, true);
                return true;
            }
            case R.id.menu_chart_item_save_view: {
                if (!PermissionUtil.isStoragePermissionGranted(this))
                    return true;
                Toast.makeText(this, R.string.chart_is_being_saved, Toast.LENGTH_SHORT).show();
                BitmapFromChart bitmapFromChart = null;
                if (!collectedChartsInvoked) {
                    bitmapFromChart = new BitmapFromChart(lineChartView); // set bitmap inside
                    bitmapFromChart.setFileName("tmp_chart_from_view");
                    bitmapFromChart.setPath(PermissionUtil.getTmpDir(this));
                    bitmapFromChart.setExtPNG();
                } else {
                    bitmapFromChart = new BitmapFromChart(lineChartView); // set bitmap inside
                    bitmapFromChart.setFileName(FileWalkerUtil.getSomeUniqueImageName());
                    bitmapFromChart.setPath(FileWalkerUtil.getDirectoryToCollectCharts());
                    bitmapFromChart.setExtPNG();
                }

                SaveBitmapFromChartAsyncWorker worker = new SaveBitmapFromChartAsyncWorker(this, bitmapFromChart, !collectedChartsInvoked);
                worker.execute();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void changeType(ChartBuilder.ChartMode chartMode, boolean computePosition, boolean toggleLabels) {
        float centerX = 0;
        float centerY = 0;
        float zoom = 0;
        if (computePosition) {
            centerX = lineChartView.getChartComputator().getVisibleViewport().centerX();
            centerY = lineChartView.getChartComputator().getVisibleViewport().centerY();
            zoom = lineChartView.getZoomLevel();
        }
        if (toggleLabels) {
            chartBuilder.setHasLabels(!chartBuilder.isHasLabels());
        }
        LineChartData chartData = chartBuilder.setMode(chartMode).build();
        lineChartView.setLineChartData(chartData);
        if (computePosition) {
            lineChartView.setZoomLevel(centerX, centerY, zoom);
        } else {
            Viewport viewport = lineChartView.getCurrentViewport();
            lineChartView.setZoomLevel(viewport.centerX(), viewport.centerY(), chartBuilder.getDays() / initialDaysOnScreen);
        }
    }

    public static class CustomPointValue extends PointValue {
        private BaseModel pressureData;

        public CustomPointValue(float x, float y, BaseModel pressureData) {
            super(x, y);
            this.pressureData = pressureData;
        }

        public BaseModel getPressureData() {
            return pressureData;
        }

        public void setPressureData(BaseModel pressureData) {
            this.pressureData = pressureData;
        }
    }

}
