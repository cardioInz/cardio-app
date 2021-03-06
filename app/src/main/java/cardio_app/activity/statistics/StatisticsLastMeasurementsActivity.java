package cardio_app.activity.statistics;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cardio_app.R;
import cardio_app.db.DbHelper;
import cardio_app.db.model.PressureData;
import cardio_app.filtering.DataFilter;
import cardio_app.statistics.Statistics;
import cardio_app.statistics.analyse.StatisticLastMeasure;
import cardio_app.statistics.analyse.StatisticLastMeasure.TypeEnum;
import cardio_app.util.Defaults;

public class StatisticsLastMeasurementsActivity extends AppCompatActivity {

    private static final String TAG = StatisticsLastMeasurementsActivity.class.getName();
    private final Statistics statistics = new Statistics(false, true);
    private DbHelper dbHelper;
    private DataFilter dataFilter = Defaults.getDefaultDataFilter();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_last_measurements);

        Intent intent = getIntent();
        dataFilter = intent.getParcelableExtra("filterdata");

        listView = (ListView) findViewById(R.id.statistics_measures_list_view);
        assignValuesInStatistics();
    }


    public void assignValuesInStatistics() {
        try {
            List<PressureData> list = getHelper().getFilteredAndOrderedByDatePressureData(dataFilter.getDateFrom(), dataFilter.getDateTo());
            statistics.assignValues(list);
        } catch (SQLException e) {
            Log.e(TAG, "onCreate: can't get pressure data to analyse", e);
            statistics.assignValues(new ArrayList<>());
        }

        HashMap<TypeEnum, StatisticLastMeasure> map = statistics.getStatisticMeasuresMap();
        for (TypeEnum key : map.keySet()) {
            String title = getResources().getString(key.mapToTitleStringId());
            map.get(key).setTitle(title);
        }
        listView.setAdapter(new StatisticMeasureAdapter(this, map));
        listView.invalidateViews();
    }


    public void refreshStatisticsView() {
        // statistics view needs refresh
        assignValuesInStatistics();
    }

    @Override
    protected void onResume() {
        refreshStatisticsView();
        super.onResume();
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
