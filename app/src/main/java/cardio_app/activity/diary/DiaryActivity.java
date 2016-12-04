package cardio_app.activity.diary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

import cardio_app.R;
import cardio_app.activity.filter.FilterActivity;
import cardio_app.activity.statistics.ChartActivity;
import cardio_app.db.DbHelper;
import cardio_app.db.model.PressureData;
import cardio_app.filtering.DataFilter;
import cardio_app.filtering.DataFilterModeEnum;
import cardio_app.util.Defaults;

public class DiaryActivity extends AppCompatActivity {

    private static final String TAG = DiaryActivity.class.getName();
    private DbHelper dbHelper;
    private PressureDataAdapter pressureDataAdapter;
    private DataFilter dataFilter = Defaults.getDefaultDataFilter();

    public void addPressureData(View view) {
        Intent intent = new Intent(this, AddDiaryActivity.class);
        startActivity(intent);
    }

    public void refreshListView() {
        // TODO maybe we could do the same simpler
        assignDataToListView();
    }


    public void assignDataToListView() {
        try {
            List<PressureData> pressureDataList;
            if (dataFilter.getMode().equals(DataFilterModeEnum.NO_FILTER)) {
                pressureDataList = getHelper().getAllOrderedPressureData();
            } else {
                pressureDataList = getHelper().getFilteredAndOrderedByDatePressureData(dataFilter.getDateFrom(), dataFilter.getDateTo());
            }
            ListView listView = (ListView) findViewById(R.id.diary_list_view);
            pressureDataAdapter = new PressureDataAdapter(DiaryActivity.this, pressureDataList);
            listView.setAdapter(pressureDataAdapter);
            listView.invalidateViews();
            Log.i(TAG, "assignDataToListView: Filter applied\n" + dataFilter.getFilterMsgForLogger());
        } catch (SQLException e) {
            Log.e(TAG, "Can't get pressure data records from sql dao", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // floating button - to add new records
        FloatingActionButton addBtn = (FloatingActionButton) findViewById(R.id.add_button);
        addBtn.setOnClickListener(this::addPressureData);

        // list view - for record list
        ListView listView = (ListView) findViewById(R.id.diary_list_view);

        listView.setOnItemClickListener(((adapterView, view, i, l) -> {
            PressureData pressureData = (PressureData) adapterView.getItemAtPosition(i);
            Intent intent = new Intent(DiaryActivity.this, AddDiaryActivity.class);
            intent.putExtra("pressuredata", pressureData);
            startActivity(intent);
        }));

        assignDataToListView();
    }


    @Override
    protected void onResume() {
        refreshListView();
        super.onResume();
        ;
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
            case R.id.menu_item_filter_data: {
                onFilterDataClick();
                return true;
            }
            case R.id.menu_item_chart: {
                onChartClick();
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

    private void onChartClick() {
        Intent intent = new Intent(this, ChartActivity.class);
        intent.putExtra("filterdata", dataFilter);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                DataFilter dataFilter = data.getParcelableExtra("filterdata");
                if (dataFilter != null)
                    this.dataFilter = dataFilter;
                Log.i(TAG, "onActivityResult: " + this.dataFilter.getFilterMsgForLogger());
                refreshListView();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                refreshListView();
            }
        }
    }
}
