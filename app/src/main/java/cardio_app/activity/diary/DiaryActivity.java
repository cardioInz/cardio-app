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
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import cardio_app.R;
import cardio_app.activity.filter.FilterActivity;
import cardio_app.db.DbHelper;
import cardio_app.db.model.PressureData;
import cardio_app.filtering_and_statistics.DataFilter;

public class DiaryActivity extends AppCompatActivity {

    private static final String TAG = DiaryActivity.class.getName();
    private DbHelper dbHelper;
    PressureDataAdapter pressureDataAdapter;
    Date dateFrom = null;
    Date dateTo = null;

    public void addPressureData(View view) {
        Intent intent = new Intent(this, AddDiaryActivity.class);
        startActivity(intent);
    }

    public void refreshListView() {
        // TODO maybe we could do the same simpler
        assignDataToListView(dateFrom, dateTo);
    }


    public void assignDataToListView(Date dateFrom, Date dateTo) {
        try {
            List<PressureData> pressureDataList;
            if (dateFrom == null || dateTo == null) {
                pressureDataList = getHelper().getOrderedPressureData();
            } else {
                pressureDataList = getHelper().getFilteredAndOrderedByDatePressureData(dateFrom, dateTo);
            }
            ListView listView = (ListView) findViewById(R.id.diary_list_view);
            pressureDataAdapter = new PressureDataAdapter(DiaryActivity.this, pressureDataList);
            listView.setAdapter(pressureDataAdapter);
            listView.invalidateViews();
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

        listView.setOnItemLongClickListener(((adapterView, view, i, l) -> {
            PressureData pressureData = (PressureData) adapterView.getItemAtPosition(i);
            Intent intent = new Intent(DiaryActivity.this, AddDiaryActivity.class);
            intent.putExtra("pressuredata", pressureData);
            startActivity(intent);
            return true;
        }));

        assignDataToListView(dateFrom, dateTo);
    }


    @Override
    protected void onResume() {
        refreshListView();
        super.onResume();;
    }

    @Override
    protected void onRestart() {
        refreshListView();
        super.onRestart();;
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
        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String dateFromStr =data.getStringExtra("dateFrom");
                String dateToStr =data.getStringExtra("dateTo");
                try {
                    dateFrom = DataFilter.DATE_FORMATTER.parse(dateFromStr);
                    dateTo = DataFilter.DATE_FORMATTER.parse(dateToStr);
                    Toast.makeText(this, "Date from: " + dateFromStr + "\nDate to: " + dateToStr, Toast.LENGTH_LONG).show();
                    // TODO load data with dates <dateFrom; dateTo>
                    refreshListView();
                } catch (ParseException e) {
                    Log.e(TAG, "Error while trying to posses filter dates", e);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                dateFrom = null;
                dateTo = null;
                refreshListView();
            }
        }
    }
}
