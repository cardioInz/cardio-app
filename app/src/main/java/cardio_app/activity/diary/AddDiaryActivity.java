package cardio_app.activity.diary;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import cardio_app.viewmodel.PressureDataViewModel;
import cardio_app.viewmodel.date_time.PickedDateViewModel;
import cardio_app.viewmodel.date_time.PickedTimeViewModel;


public class AddDiaryActivity extends AppCompatActivity {
    private static final String TAG = AddDiaryActivity.class.getName();
    private final PressureDataViewModel pressureDataViewModel = new PressureDataViewModel();
    private final PickedDateViewModel pickedDateViewModel =
            new PickedDateViewModel(pressureDataViewModel.getPressureData().getDateTime());
    private final PickedTimeViewModel pickedTimeViewModel =
            new PickedTimeViewModel(pressureDataViewModel.getPressureData().getDateTime());
    private DbHelper dbHelper;
    private GoogleApiClient client;
    private boolean isActivityOnExistingItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);

        Intent intent = getIntent();
        PressureData pressuredata = intent.getParcelableExtra("pressuredata");
        isActivityOnExistingItem = pressuredata != null;

        if (isActivityOnExistingItem) {
            pressureDataViewModel.setPressureData(pressuredata);
            pickedDateViewModel.setDate(pressuredata.getDateTime());
            pickedTimeViewModel.setTime(pressuredata.getDateTime());
            isActivityOnExistingItem = true;
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


    private void deletePressureData(PressureData pressureData) {
        if (pressureData.getId() <= 0) {
            return;
        }

        try {
            Dao<PressureData, Integer> pressureDao = getHelper().getDao(PressureData.class);
            pressureDao.deleteById(pressureData.getId());
            Toast.makeText(this, R.string.after_pressure_delete, Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            Log.e(TAG, "Can't perform delete action on PressureData record", e);
        }
    }

    private void editPressureData(PressureData pressureData) {
        try {
            Dao<PressureData, Integer> pressureDao = getHelper().getDao(PressureData.class);
            updatePressureDataDateTime();
            if (pressureData.getId() <= 0) {
                pressureDao.create(pressureData);
                Toast.makeText(this, R.string.after_pressure_save, Toast.LENGTH_SHORT).show();
            } else {
                pressureDao.update(pressureData);
                Toast.makeText(this, R.string.after_pressure_save, Toast.LENGTH_SHORT).show();
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
        inflater.inflate(R.menu.add_item, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
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

    private void onSaveClick() {
        if (isActivityOnExistingItem) {
            View contextView = findViewById(R.id.activity_add_diary);
            Snackbar
                    .make(contextView, R.string.are_you_sure_to_save_changes, Snackbar.LENGTH_LONG)
                    .setAction(R.string.save, view -> {
                        editPressureData(pressureDataViewModel.getPressureData());
                        onBackPressed();
                    })
                    .setActionTextColor(ContextCompat.getColor(this, R.color.brightOnDarkBg))
                    .show();
        } else {
            editPressureData(pressureDataViewModel.getPressureData());
            onBackPressed();
        }
    }


    private void onDeleteClick() {
        if (isActivityOnExistingItem) {
            View contextView = findViewById(R.id.activity_add_diary);
            Snackbar
                    .make(contextView, R.string.are_you_sure_to_delete_item, Snackbar.LENGTH_LONG)
                    .setAction(R.string.delete, view -> {
                        deletePressureData(pressureDataViewModel.getPressureData());
                        onBackPressed();
                    })
                    .setActionTextColor(ContextCompat.getColor(this, R.color.brightOnDarkBg))
                    .show();
        } else {
            deletePressureData(pressureDataViewModel.getPressureData());
            onBackPressed();
        }
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("AddPressureData Page") // TODO: Define a title for the content shown.
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
