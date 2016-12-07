package cardio_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import cardio_app.R;
import cardio_app.activity.diary.AddDiaryActivity;
import cardio_app.activity.diary.DiaryActivity;
import cardio_app.activity.drug.AddDrugActivity;
import cardio_app.activity.drug.DrugsActivity;
import cardio_app.activity.events.AddEventActivity;
import cardio_app.activity.events.EventActivity;
import cardio_app.activity.pdf_creation.CreatePdfReportActivity;
import cardio_app.activity.profile.ProfileActivity;
import cardio_app.activity.statistics.StatisticsActivity;
import cardio_app.activity.synchro.ExportActivity;
import cardio_app.activity.synchro.ImportActivity;
import cardio_app.db.DbHelper;
import cardio_app.db.model.UserProfile;
import cardio_app.statistics.Statistics;
import cardio_app.util.PermissionUtil;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.toString();
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            UserProfile userProfile = getHelper().getUserProfile();
            if (userProfile != null)
                Statistics.setIsMale(!userProfile.getSex().equals("F"));
        } catch (Exception e) {
            Log.e(TAG, "onCreate: ", e);
        }
    }

    public void showMeasuresList(View view) {
        Intent intent = new Intent(this, DiaryActivity.class);
        startActivity(intent);
    }

    public void addMeasure(View view) {
        Intent intent = new Intent(this, AddDiaryActivity.class);
        startActivity(intent);
    }

    public void showDrugsList(View view) {
        Intent intent = new Intent(this, DrugsActivity.class);
        startActivity(intent);
    }

    public void showStatistics(View view) {
        Intent intent = new Intent(this, StatisticsActivity.class);
        startActivity(intent);
    }

    public void showCreatePdfReport(View view) {
        if (PermissionUtil.isStoragePermissionGranted(this)) {
            Intent intent = new Intent(this, CreatePdfReportActivity.class);
            startActivity(intent);
        }
    }

    public void addEvent(View view) {
        Intent intent = new Intent(this, AddEventActivity.class);
        startActivity(intent);
    }

    public void showEventsList(View view) {
        Intent intent = new Intent(this, EventActivity.class);
        startActivity(intent);
    }

    public void showExportData(View view) {
        Intent intent = new Intent(this, ExportActivity.class);
        startActivity(intent);
    }

    public void showImportData(View view) {
        Intent intent = new Intent(this, ImportActivity.class);
        startActivity(intent);
    }

    public void showProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void addDrug(View view) {
        Intent intent = new Intent(this, AddDrugActivity.class);
        startActivity(intent);
    }

    public void showSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
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
