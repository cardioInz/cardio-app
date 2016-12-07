package cardio_app.activity.drug;

import android.content.Intent;
import android.databinding.DataBindingUtil;
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

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import cardio_app.R;
import cardio_app.databinding.ActivityAddDrugBinding;
import cardio_app.db.DbHelper;
import cardio_app.db.model.Drug;
import cardio_app.viewmodel.DrugViewModel;

public class AddDrugActivity extends AppCompatActivity {
    private static final String TAG = AddDrugActivity.class.getName();
    private final DrugViewModel drugViewModel = new DrugViewModel();
    private DbHelper dbHelper;
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
                onSaveClick(null);
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

        Toast.makeText(this, R.string.drug_deleted_successfully, Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    public void onSaveClick(View un) {
        if (isActivityOnExistingItem) {
            View contextView = findViewById(R.id.activity_add_drug);
            Snackbar
                    .make(contextView, R.string.are_you_sure_to_save_changes, Snackbar.LENGTH_LONG)
                    .setAction(R.string.save, view -> saveDrug())
                    .setActionTextColor(ContextCompat.getColor(this, R.color.brightOnDarkBg))
                    .show();
        } else {
            saveDrug();
        }
    }

    private void saveDrug() {
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

        Toast.makeText(this, R.string.drug_saved_successfully, Toast.LENGTH_SHORT).show();
        onBackPressed();
    }
}
