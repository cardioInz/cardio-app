package cardio_app.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cardio_app.R;
import cardio_app.activity.drug.DrugsActivity;
import cardio_app.db.DbHelper;
import cardio_app.db.model.PressureData;
import cardio_app.viewmodel.pressure.PressureDataViewModel;
import cardio_app.viewmodel.pressure.TableRowPressureData;
import temporary_package.RandomParams;

public class DiaryActivity extends AppCompatActivity {

    List<TableRowPressureData> rowList = new ArrayList<>();
    AppCompatActivity self = this;
    TableLayout tableLayout;
    private static final String TAG = DrugsActivity.class.getName();
    private DbHelper dbHelper;

    private void addTableRowToLayout(PressureData hp, boolean showToast) {
        TableRowPressureData thr = new TableRowPressureData(self, new PressureDataViewModel(hp));
        rowList.add(thr);
        tableLayout.addView(thr, 0); // always on the top

        if (showToast) {
            Toast.makeText(
                    getApplicationContext(),
                    getResources().getString(R.string.new_record_added_msg)
                            + ", ID: " + String.valueOf(thr.getId()),
                    Toast.LENGTH_SHORT
            ).show();
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
        addBtn.setOnClickListener(view -> {
            PressureData hp = RandomParams.getRandomPressureData();
            addTableRowToLayout(hp, true);
        });

        // table layout - for record list
        tableLayout = (TableLayout) findViewById(R.id.TableLayout);
        tableLayout.setPadding(0,0,15,0);

        try {
            Dao<PressureData, Integer> dao = getHelper().getDao(PressureData.class);
//            List<PressureData> listPressureData = dao.queryForAll();
            List<PressureData> listPressureData = RandomParams.makePressureDataList();
            for (PressureData hp : listPressureData) {
                addTableRowToLayout(hp, false);
            }
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
