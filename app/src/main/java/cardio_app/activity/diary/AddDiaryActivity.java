package cardio_app.activity.diary;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Calendar;

import cardio_app.R;
import cardio_app.databinding.ActivityAddDiaryBinding;
import cardio_app.db.DbHelper;
import cardio_app.db.model.PressureData;
import cardio_app.viewmodel.date_time.PickedDateViewModel;
import cardio_app.viewmodel.date_time.PickedTimeViewModel;
import cardio_app.viewmodel.PressureDataViewModel;


public class AddDiaryActivity extends AppCompatActivity {
    private static final String TAG = AddDiaryActivity.class.getName();
    private DbHelper dbHelper;
    private final PressureDataViewModel pressureDataViewModel = new PressureDataViewModel();
    private final PickedDateViewModel pickedDateViewModel =
            new PickedDateViewModel(pressureDataViewModel.getPressureData().getDateTime());
    private final PickedTimeViewModel pickedTimeViewModel =
            new PickedTimeViewModel(pressureDataViewModel.getPressureData().getDateTime());
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);

        Intent intent = getIntent();
        PressureData pressuredata = intent.getParcelableExtra("pressuredata");

        if (pressuredata != null) {
            pressureDataViewModel.setPressureData(pressuredata);
            pickedDateViewModel.setDate(pressuredata.getDateTime());
            pickedTimeViewModel.setTime(pressuredata.getDateTime());
        }

        ActivityAddDiaryBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_diary);
        binding.setPressuredata(pressureDataViewModel);
        binding.setPickedDate(pickedDateViewModel);
        binding.setPickedTime(pickedTimeViewModel);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void updatePressureDataDateTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(
                pickedDateViewModel.getYear(),
                pickedDateViewModel.getMonth(),
                pickedDateViewModel.getDay(),
                pickedTimeViewModel.getHourOfDay(),
                pickedTimeViewModel.getMinute()
        );
        pressureDataViewModel.getPressureData().setDateTime(cal.getTime());
    }


    private void editPressureData(PressureData pressureData) {
        try {
            Dao<PressureData, Integer> pressureDao = getHelper().getDao(PressureData.class);
            updatePressureDataDateTime();
            if (pressureData.getId() <= 0) {
                pressureDao.create(pressureData);
            } else {
                pressureDao.update(pressureData);
            }
        } catch (SQLException e) {
            Log.e(TAG, "Can't perform create/update action on PressureData record", e);
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

    public DbHelper getHelper() {
        if (dbHelper == null) {
            dbHelper = OpenHelperManager.getHelper(this, DbHelper.class);
        }
        return dbHelper;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_pressure, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_pressure: {
                onSaveClick();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void onSaveClick() {
        PressureData pressureData = pressureDataViewModel.getPressureData();
        editPressureData(pressureData);
//        Toast.makeText(this, R.string.after_pressure_save, Toast.LENGTH_SHORT).show();
        Toast.makeText(
                    getApplicationContext(),
                    getResources().getString(R.string.new_record_added_msg)
                            + ", ID: " + String.valueOf(pressureData.getId()),
                    Toast.LENGTH_SHORT
            ).show();
        onBackPressed();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("AddPressure Page") // TODO: Define a title for the content shown.
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
}
