package cardio_app.activity.drug;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cardio_app.R;
import cardio_app.databinding.ActivityAddDrugBinding;
import cardio_app.db.DbHelper;
import cardio_app.db.model.Alarm;
import cardio_app.db.model.AlarmDrug;
import cardio_app.db.model.Drug;
import cardio_app.service.SetAlarmService;
import cardio_app.viewmodel.AlarmInDrugViewModel;
import cardio_app.viewmodel.DrugViewModel;

public class AddDrugActivity extends AppCompatActivity {
    private static final String TAG = AddDrugActivity.class.getName();

    private DbHelper dbHelper;
    private final DrugViewModel drugViewModel = new DrugViewModel();
    private final List<AlarmInDrugViewModel> alarms = new ArrayList<>();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private boolean isActivityOnExistingItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Drug drug = intent.getParcelableExtra("drug");
        isActivityOnExistingItem = drug != null;

        if (isActivityOnExistingItem) {
            drugViewModel.setDrug(drug);
        }
        ActivityAddDrugBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_drug);
        binding.setDrug(drugViewModel);

        try {
            dbHelper = getHelper();
            Dao<Alarm, Integer> dao = dbHelper.getDao(Alarm.class);

            List<Alarm> checkedAlarms = new ArrayList<>();
            if (drug != null) {
                Dao<AlarmDrug, Integer> alarmDrugDao = dbHelper.getDao(AlarmDrug.class);
                QueryBuilder<AlarmDrug, Integer> builder = alarmDrugDao.queryBuilder();

                builder.where().eq("drug_id", drug);
                builder.selectColumns("alarm_id");
                Log.d(TAG, builder.prepareStatementString());

                for (AlarmDrug alarmDrug : builder.query()) {
                    checkedAlarms.add(alarmDrug.getAlarm());
                }
            }

            for (Alarm alarm : dao.queryForAll()) {
                Comparator<Alarm> comparator = (a1, a2) -> a1.getId() - a2.getId();

                Collections.sort(checkedAlarms, comparator);
                if (Collections.binarySearch(checkedAlarms, alarm, comparator) >= 0) {
                    alarms.add(new AlarmInDrugViewModel(alarm, true));
                } else {
                    alarms.add(new AlarmInDrugViewModel(alarm, false));
                }
            }
        } catch (SQLException e) {
            Log.e(TAG, "Cannot fetch data from database", e);
            throw new RuntimeException(e);
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (dbHelper != null) {
            OpenHelperManager.releaseHelper();
            dbHelper = null;
        }
    }

    public DbHelper getHelper() {
        if (dbHelper == null) {
            dbHelper = OpenHelperManager.getHelper(this, DbHelper.class);
        }

        return dbHelper;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_item, menu);
        if (!isActivityOnExistingItem) {
            MenuItem menuItem = menu.findItem(R.id.delete_item);
            menuItem.setEnabled(false);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_item: {
                onSaveClick();
                return true;
            }
            case R.id.delete_item: {
                onDeleteClick();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void onDeleteClick() {
        Drug drug = drugViewModel.getDrug();

        if (drug != null) {
            try {
                Dao<Drug, Integer> drugDao = getHelper().getDao(Drug.class);
                Dao<AlarmDrug, Integer> alarmDrugDao = getHelper().getDao(AlarmDrug.class);

                DeleteBuilder<AlarmDrug, Integer> builder = alarmDrugDao.deleteBuilder();
                builder.where().eq("drug_id", drug);
                int delete = builder.delete();
                Log.d(TAG, "Deleted " + delete + " alarms");

                int drugDelete = drugDao.delete(drug);
                Log.d(TAG, "Deleted " + drugDelete + " drugs");
            } catch (SQLException e) {
                Log.e(TAG, "Can't save drug", e);
                throw new RuntimeException(e);
            }
        }

        Toast.makeText(this, "Drug deleted successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, SetAlarmService.class);
        startService(intent);

        onBackPressed();
    }

    private void onSaveClick() {
        try {
            Dao<Drug, Integer> drugDao = getHelper().getDao(Drug.class);
            Dao<AlarmDrug, Integer> alarmDrugDao = getHelper().getDao(AlarmDrug.class);

            Drug drug = drugViewModel.getDrug();

            if (drug.getId() == 0) {
                drugDao.create(drug);
            } else {
                drugDao.update(drug);
            }

            for (AlarmInDrugViewModel alarmInDrugViewModel : alarms) {
                if (alarmInDrugViewModel.checkedChanged()) {
                    if (alarmInDrugViewModel.isChecked()) {
                        alarmDrugDao.create(new AlarmDrug(alarmInDrugViewModel.getAlarm(), drug));
                        Log.d(TAG, "Set drug to alarm: " + alarmInDrugViewModel.getAlarm());
                    } else {
                        DeleteBuilder<AlarmDrug, Integer> builder = alarmDrugDao.deleteBuilder();
                        builder.where().eq("drug_id", drug).and().eq("alarm_id", alarmInDrugViewModel.getAlarm());
                        builder.delete();
                        Log.d(TAG, "Unset drug to alarm: " + alarmInDrugViewModel.getAlarm());
                    }
                }
            }
        } catch (SQLException e) {
            Log.e(TAG, "Can't save drug", e);
            throw new RuntimeException(e);
        }

        Toast.makeText(this, "Drug saved successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, SetAlarmService.class);
        startService(intent);

        onBackPressed();
    }
}
