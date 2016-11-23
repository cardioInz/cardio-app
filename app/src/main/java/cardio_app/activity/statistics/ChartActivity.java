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
import cardio_app.db.model.PressureData;
import cardio_app.filtering.DataFilter;
import cardio_app.filtering.DataFilterModeEnum;
import cardio_app.pdf_creation.param_models.BitmapFromChart;
import cardio_app.util.BitmapUtil;
import cardio_app.util.ChartBuilder;
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
    private static final DataFilterModeEnum DEFAULT_DATA_FILTER = DataFilterModeEnum.NO_FILTER;

    private DataFilter dataFilter = new DataFilter(DEFAULT_DATA_FILTER);
    private DbHelper dbHelper;
    private LineChartView lineChartView;
    List<PressureData> pressureList;
    private ChartBuilder chartBuilder;

    private float minDaysOnScreen = 1;
    private float initialDaysOnScreen = 4;
    private boolean collectedChartsInvoked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        Intent intent = getIntent();
        DataFilter df = intent.getParcelableExtra("filterdata");
        if (df == null){
            Log.e(TAG, "onCreate: filterdata is null, maybe not passed, lol ?");
        } else {
            dataFilter = df;
        }

        try {
            collectedChartsInvoked = intent.getBooleanExtra("collectedChartsInvoked", false);
        } catch (Exception e){
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
                    PressureData data = ((CustomPointValue) pointValue).getPressureData();

                    DataDialog dialog = DataDialog.newInstance(data);

                    dialog.show(getSupportFragmentManager(), "measerument");
                }
            }

            @Override
            public void onValueDeselected() {}
        });

        try {
            if (dataFilter.getMode().equals(DataFilterModeEnum.NO_FILTER)) {
                pressureList = getHelper().getAllOrderedPressureData();
            } else {
                pressureList = getHelper().getFilteredAndOrderedByDatePressureData(dataFilter.getDateFrom(), dataFilter.getDateTo());
            }

            this.chartBuilder = new ChartBuilder(pressureList, getResources());

            changeType(ChartBuilder.ChartMode.DISCRETE);
            lineChartView.setMaxZoom(chartBuilder.getDays()/minDaysOnScreen);
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
                changeType(ChartBuilder.ChartMode.DISCRETE);
                return true;
            }
            case R.id.chart_continuous: {
                changeType(ChartBuilder.ChartMode.CONTINUOUS);
                return true;
            }
            case R.id.menu_chart_item_save_view: {
                if (!PermissionUtil.isStoragePermissionGranted(this))
                    return true;
                if (!collectedChartsInvoked) {
                    BitmapFromChart bitmapFromChart = new BitmapFromChart(lineChartView, this.getResources()); // set bitmap inside
                    bitmapFromChart.setFileName("tmp_chart_from_view");
                    bitmapFromChart.setPath(PermissionUtil.getTmpDir(this));
                    bitmapFromChart.setExt(BitmapUtil.EXT_IMG.PNG);
                    if (BitmapUtil.saveBitmapToFile(bitmapFromChart)){
                        Intent intent = new Intent(this, ChartSaveActivity.class);
                        intent.putExtra("bitmapFromChart", bitmapFromChart);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, R.string.chart_could_not_be_saved, Toast.LENGTH_SHORT);
                    }

                } else {
                    BitmapFromChart bitmapFromChart = new BitmapFromChart(lineChartView, this.getResources()); // set bitmap inside
                    bitmapFromChart.setFileName(FileWalkerUtil.getSomeUniqueImageName());
                    bitmapFromChart.setPath(FileWalkerUtil.getDirectoryToCollectCharts());
                    bitmapFromChart.setExt(BitmapUtil.EXT_IMG.PNG);
                    if (BitmapUtil.saveBitmapToFile(bitmapFromChart)){
                        Toast.makeText(this, R.string.chart_save_successfully, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, R.string.chart_could_not_be_saved, Toast.LENGTH_SHORT);
                    }

                }
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void changeType(ChartBuilder.ChartMode chartMode) {
        LineChartData chartData = chartBuilder.setMode(chartMode).build();
        lineChartView.setLineChartData(chartData);
        Viewport viewport = lineChartView.getCurrentViewport();
        lineChartView.setZoomLevel(viewport.centerX(), viewport.centerY(), chartBuilder.getDays() / initialDaysOnScreen);
    }

    public static class CustomPointValue extends PointValue {
        private PressureData pressureData;

        public CustomPointValue(float x, float y, PressureData pressureData) {
            super(x, y);
            this.pressureData = pressureData;
        }

        public PressureData getPressureData() {
            return pressureData;
        }

        public void setPressureData(PressureData pressureData) {
            this.pressureData = pressureData;
        }
    }

}
