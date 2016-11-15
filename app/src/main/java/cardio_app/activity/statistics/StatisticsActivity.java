package cardio_app.activity.statistics;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

public class StatisticsActivity extends AppCompatActivity {
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        LineChartView lineChartView = (LineChartView) findViewById(R.id.pressure_chart);
//        lineChartView.setInteractive(true);

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
//            lines.add(systoleLine);
//            lines.add(diastoleLine);

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
