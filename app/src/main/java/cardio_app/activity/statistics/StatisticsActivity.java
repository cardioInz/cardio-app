package cardio_app.activity.statistics;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

import cardio_app.R;
import cardio_app.activity.filter.FilterActivity;
import cardio_app.db.DbHelper;
import cardio_app.db.model.PressureData;
import cardio_app.filtering_and_statistics.DataFilter;
import cardio_app.filtering_and_statistics.DataFilterModeEnum;
import cardio_app.viewmodel.StatisticsViewModel;
import cardio_app.databinding.ContentStatisticsBinding;

public class StatisticsActivity extends AppCompatActivity {

    private static final DataFilterModeEnum DEFAULT_DATA_FILTER = DataFilterModeEnum.NO_FILTER;
    private static final String TAG = StatisticsActivity.class.getName();
    private DbHelper dbHelper;
    private DataFilter dataFilter = new DataFilter(DEFAULT_DATA_FILTER);
    private final StatisticsViewModel statisticsViewModel = new StatisticsViewModel();

    public void assignValuesInStatistics() {
        List<PressureData> list = null;
        try {
            list = getHelper().getFilteredAndOrderedByDatePressureData(dataFilter.getDateFrom(), dataFilter.getDateTo());
            statisticsViewModel.setDataListToStatistics(list);
        } catch (SQLException e) {
            Log.e(TAG, "onCreate: can't get pressure data to analyse", e);
        }


    }

    public void refreshStatisticsView() {
        // statistics view needs refresh
        assignValuesInStatistics();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        assignValuesInStatistics();

        ContentStatisticsBinding binding = DataBindingUtil.setContentView(this, R.layout.content_statistics);
        binding.setStatistics(statisticsViewModel);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filter_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter_data: {
                onFilterDataClick();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void onFilterDataClick() {
        Intent intent = new Intent(this, FilterActivity.class);
        intent.putExtra("filterdata", dataFilter);
        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                DataFilter dataFilter = data.getParcelableExtra("filterdata");
                if (dataFilter != null)
                    this.dataFilter = dataFilter;
                Toast.makeText(this, this.dataFilter.getFilterMsg(), Toast.LENGTH_LONG).show(); // TODO remove it
                refreshStatisticsView();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
//                dataFilter.setMode(DEFAULT_DATA_FILTER);
                refreshStatisticsView();
            }
        }
    }
}
