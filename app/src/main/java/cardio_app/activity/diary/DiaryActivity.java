package cardio_app.activity.diary;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import cardio_app.R;
import cardio_app.db.DbHelper;
import cardio_app.db.model.PressureData;

public class DiaryActivity extends AppCompatActivity {

    private static final String TAG = DiaryActivity.class.getName();
    private DbHelper dbHelper;

    public void addPressureData(View view) {
        Intent intent = new Intent(this, AddDiaryActivity.class);
        startActivity(intent);
    }

    public void refreshListView() {
        ListView listView = (ListView) findViewById(R.id.diary_list_view);
        listView.invalidateViews();
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

        try {
            Dao<PressureData, Integer> dao = getHelper().getDao(PressureData.class);
            List<PressureData> pressureDataList = dao.queryBuilder().orderByRaw("dateTime desc").query();
            listView.setAdapter(new PressureDataAdapter(DiaryActivity.this, pressureDataList));
            listView.invalidateViews();
        } catch (SQLException e) {
            Log.e(TAG, "Can't get pressure data records from sql dao", e);
            throw new RuntimeException(e);
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
