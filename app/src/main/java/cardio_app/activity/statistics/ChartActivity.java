package cardio_app.activity.statistics;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cardio_app.R;
import cardio_app.db.DbHelper;
import cardio_app.db.model.PressureData;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class ChartActivity extends AppCompatActivity {

    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LineChartView lineChartView = (LineChartView) findViewById(R.id.chart_view);

        try {
            List<PressureData> list = getHelper().getAllOrderedPressureData();
            List<PointValue> systoles = new ArrayList<>();
            List<PointValue> diastoles = new ArrayList<>();

//            int i = 0;
//            for (PressureData data : list) {
//                systoles.add(new PointValue(i, data.getSystole()));
//                diastoles.add(new PointValue(i, data.getDiastole()));
//                i++;
//            }

            Line systoleLine = new Line(systoles).setColor(Color.RED);
            Line diastoleLine = new Line(diastoles).setColor(Color.GREEN);
            diastoleLine.setFilled(true);
            systoleLine.setFilled(true);

            List<Line> lines = new ArrayList<>();

            int i = 0;
            for (PressureData data : list) {
                List<PointValue> once = new ArrayList<>();
                once.add(new PointValue(i, data.getSystole()));
                once.add(new PointValue(i, data.getDiastole()));

                lines.add(new Line(once).setColor(Color.RED));
                i++;
            }

            LineChartData chartData = new LineChartData().setLines(lines);

            lineChartView.setLineChartData(chartData);
            lineChartView.setZoomType(ZoomType.HORIZONTAL);

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
}
