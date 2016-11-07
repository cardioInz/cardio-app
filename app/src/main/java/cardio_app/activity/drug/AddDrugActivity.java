package cardio_app.activity.drug;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
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
import cardio_app.databinding.AlarmListItemBinding;
import cardio_app.db.DbHelper;
import cardio_app.db.model.Alarm;
import cardio_app.db.model.AlarmDrug;
import cardio_app.db.model.Drug;
import cardio_app.service.SetAlarmService;
import cardio_app.viewmodel.AlarmViewModel;
import cardio_app.viewmodel.DrugViewModel;

public class AddDrugActivity extends AppCompatActivity {
    private static final String TAG = AddDrugActivity.class.getName();

    private DbHelper dbHelper;
    private final DrugViewModel drugViewModel = new DrugViewModel();
    private final List<AlarmViewModel> alarms = new ArrayList<>();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Drug drug = intent.getParcelableExtra("drug");

        if (drug != null) {
            drugViewModel.setDrug(drug);
        }
        ActivityAddDrugBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_drug);
        binding.setDrug(drugViewModel);

        ListView alarmsListView = (ListView) findViewById(R.id.alarms);

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
                    alarms.add(new AlarmViewModel(alarm, true));
                } else {
                    alarms.add(new AlarmViewModel(alarm, false));
                }
            }

            alarmsListView.setAdapter(new AlarmAdapter(alarms));
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
        inflater.inflate(R.menu.add_drug, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save: {
                onSaveClick();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
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

            for (AlarmViewModel alarmViewModel : alarms) {
                if (alarmViewModel.checkedChanged()) {
                    if (alarmViewModel.isChecked()) {
                        alarmDrugDao.create(new AlarmDrug(alarmViewModel.getAlarm(), drug));
                        Log.d(TAG, "Set drug to alarm: " + alarmViewModel.getAlarm());
                    } else {
                        DeleteBuilder<AlarmDrug, Integer> builder = alarmDrugDao.deleteBuilder();
                        builder.where().eq("drug_id", drug).and().eq("alarm_id", alarmViewModel.getAlarm());
                        builder.delete();
                        Log.d(TAG, "Unset drug to alarm: " + alarmViewModel.getAlarm());
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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("AddDrug Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private class AlarmAdapter extends ArrayAdapter<AlarmViewModel> {

        AlarmAdapter(List<AlarmViewModel> data) {
            super(AddDrugActivity.this, R.layout.alarm_list_item, data);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            AlarmListItemBinding alarmBinding;
            if (convertView != null) {
                alarmBinding = DataBindingUtil.bind(convertView);
            } else {
                alarmBinding = DataBindingUtil.inflate(inflater, R.layout.alarm_list_item, parent, false);
            }

            convertView = alarmBinding.getRoot();

            //TODO: This piece of code generates binding issues but is necessary to avoid drawing animation when scrolling listview
//            final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.alarm_enable);
//            checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
//                checkBox.jumpDrawablesToCurrentState();
//                checkBox.setOnCheckedChangeListener(null);
//            });

            alarmBinding.setAlarm(getItem(position));

            return convertView;
        }
    }

}
