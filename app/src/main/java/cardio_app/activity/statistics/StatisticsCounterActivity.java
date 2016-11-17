package cardio_app.activity.statistics;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cardio_app.R;
import cardio_app.databinding.ContentStatisticsCounterInfoBinding;
import cardio_app.db.DbHelper;
import cardio_app.db.model.PressureData;
import cardio_app.filtering_and_statistics.DataFilter;
import cardio_app.filtering_and_statistics.DataFilterModeEnum;
import cardio_app.filtering_and_statistics.Statistics;
import cardio_app.filtering_and_statistics.statistics_model.StatisticMeasure;
import cardio_app.filtering_and_statistics.statistics_model.StatisticMeasureTypeEnum;
import cardio_app.viewmodel.statistics.StatisticCounterViewModel;

public class StatisticsCounterActivity extends AppCompatActivity {
    private DbHelper dbHelper;
    private static final DataFilterModeEnum DEFAULT_DATA_FILTER = DataFilterModeEnum.NO_FILTER;
    private DataFilter dataFilter = new DataFilter(DEFAULT_DATA_FILTER);
    private final StatisticCounterViewModel statisticCounterViewModel = new StatisticCounterViewModel();
    private static final String TAG = StatisticsCounterActivity.class.getName();
    private final Statistics statistics = new Statistics(true, false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_counter);

        Intent intent = getIntent();
        dataFilter = intent.getParcelableExtra("filterdata");

        assignValuesInStatistics();

        ContentStatisticsCounterInfoBinding binding = DataBindingUtil.setContentView(this, R.layout.content_statistics_counter_info);
        binding.setStatisticCounter(statisticCounterViewModel);
    }

    public void refreshStatisticsView() {
        // statistics view needs refresh
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

        statisticCounterViewModel.setStatisticCounter(statistics.getStatisticCounter());
        TableLayout tableLayout = (TableLayout) findViewById(R.id.statistics_counters_table_layout);
        tableLayout.invalidate();
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