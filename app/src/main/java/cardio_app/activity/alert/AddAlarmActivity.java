package cardio_app.activity.alert;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TimePicker;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.sql.Time;

import cardio_app.R;
import cardio_app.activity.drug.AddDrugActivity;
import cardio_app.databinding.ActivityAddAlarmBinding;
import cardio_app.db.DbHelper;
import cardio_app.db.model.Alarm;
import cardio_app.viewmodel.AlarmViewModel;

public class AddAlarmActivity extends AppCompatActivity {
    private static final String TAG = AddDrugActivity.class.getName();

    private DbHelper dbHelper;
    private AlarmViewModel alarmViewModel;
    private boolean isActivityOnExistingItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Alarm alarm = intent.getParcelableExtra("alarm");
        isActivityOnExistingItem = alarm != null;

        if (isActivityOnExistingItem) {
            alarmViewModel = new AlarmViewModel(alarm);
        } else {
            alarmViewModel = new AlarmViewModel(new Alarm());
        }

        ActivityAddAlarmBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_alarm);
        binding.setAlarm(alarmViewModel);

        TimePicker timePicker = (TimePicker) findViewById(R.id.alarm_time);
        timePicker.setCurrentHour(alarmViewModel.getHour());
        timePicker.setCurrentMinute(alarmViewModel.getMinute());
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
        try {
            Dao<Alarm, Integer> dao = getHelper().getDao(Alarm.class);
            dao.delete(alarmViewModel.getAlarm());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        onBackPressed();
    }

    private void onSaveClick() {
        TimePicker timePicker = (TimePicker) findViewById(R.id.alarm_time);

        alarmViewModel.setHour(timePicker.getCurrentHour());
        alarmViewModel.setMinute(timePicker.getCurrentMinute());

        try {
            Dao<Alarm, Integer> dao = getHelper().getDao(Alarm.class);
            if (isActivityOnExistingItem) {
                dao.update(alarmViewModel.getAlarm());
            } else {
                dao.create(alarmViewModel.getAlarm());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        onBackPressed();
    }
}
