package cardio_app.activity.alert;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cardio_app.R;
import cardio_app.activity.drug.AddDrugActivity;
import cardio_app.databinding.AlarmListItemBinding;
import cardio_app.db.DbHelper;
import cardio_app.db.model.Alarm;
import cardio_app.viewmodel.AlarmViewModel;

public class AlertsActivity extends AppCompatActivity {
    private static final String TAG = AlertsActivity.class.getName();

    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        ListView listView = (ListView) findViewById(R.id.item_list);
        listView.setOnItemClickListener(((adapterView, view, i, l) -> {
            AlarmViewModel alarmViewModel = (AlarmViewModel) adapterView.getItemAtPosition(i);
            Intent intent = new Intent(AlertsActivity.this, AddAlarmActivity.class);
            intent.putExtra("alarm", alarmViewModel.getAlarm());
            startActivity(intent);
        }));

        assignDataToListView();
    }

    private void assignDataToListView() {
        try {
            ListView listView = (ListView) findViewById(R.id.item_list);
            Dao<Alarm, Integer> dao = getDbHelper().getDao(Alarm.class);
            List<AlarmViewModel> data = new ArrayList<>();

            for (Alarm alarm : dao.queryForAll()) {
                data.add(new AlarmViewModel(alarm));
            }
            listView.setAdapter(new AlertsActivity.AlarmAdapter(data));
            listView.invalidateViews();
        } catch (SQLException e) {
            Log.e(TAG, "Can't get drugs", e);
            throw new RuntimeException(e);
        }
    }

    public void addItem(View view) {
        Intent intent = new Intent(this, AddAlarmActivity.class);

        startActivity(intent);
    }

    @Override
    protected void onResume() {
        refreshListView();
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

    private void refreshListView() {
        assignDataToListView();
    }

    public DbHelper getDbHelper() {
        if (dbHelper == null) {
            dbHelper = OpenHelperManager.getHelper(this, DbHelper.class);
        }

        return dbHelper;
    }

    private class AlarmAdapter extends ArrayAdapter<AlarmViewModel> {

        AlarmAdapter(List<AlarmViewModel> data) {
            super(AlertsActivity.this, R.layout.alarm_list_item, data);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            AlarmListItemBinding alarmBinding;
            if (convertView != null) {
                alarmBinding = DataBindingUtil.bind(convertView);
            } else {
                alarmBinding = DataBindingUtil.inflate(inflater, R.layout.alarm_list_item, parent, false);
            }

            convertView = alarmBinding.getRoot();

            alarmBinding.setAlarm(getItem(position));

            return convertView;
        }
    }
}
