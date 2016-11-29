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

import java.sql.SQLException;

import cardio_app.R;
import cardio_app.databinding.ActivityAddDrugBinding;
import cardio_app.db.DbHelper;
import cardio_app.db.model.Drug;
import cardio_app.service.SetAlarmService;
import cardio_app.viewmodel.DrugViewModel;

public class AddDrugActivity extends AppCompatActivity {
    private static final String TAG = AddDrugActivity.class.getName();

    private DbHelper dbHelper;
    private final DrugViewModel drugViewModel = new DrugViewModel();
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

            Drug drug = drugViewModel.getDrug();

            if (drug.getId() == 0) {
                drugDao.create(drug);
            } else {
                drugDao.update(drug);
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
