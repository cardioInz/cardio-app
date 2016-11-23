package cardio_app.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.GridLayout;

import cardio_app.R;
import cardio_app.activity.alert.AlertsActivity;
import cardio_app.activity.diary.AddDiaryActivity;
import cardio_app.activity.diary.DiaryActivity;
import cardio_app.activity.drug.DrugsActivity;
import cardio_app.activity.pdf_creation.CreatePdfReportActivity;
import cardio_app.activity.events.AddEventActivity;
import cardio_app.activity.events.EventActivity;
import cardio_app.activity.statistics.StatisticsActivity;
import cardio_app.activity.synchro.ExportActivity;
import cardio_app.activity.synchro.ImportActivity;

public class MainActivity extends AppCompatActivity{
    private GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridLayout = (GridLayout) findViewById(R.id.menu_grid_layout);
        gridLayout.setColumnCount(3);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // TODO why the code below doesn't change number of

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayout.setColumnCount(3);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayout.setColumnCount(4);
        } else if (newConfig.orientation == Configuration.ORIENTATION_UNDEFINED) {
            return;
        }

        gridLayout.invalidate();
        gridLayout.refreshDrawableState();
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

    public void showAlertsList(View view) {
        Intent intent = new Intent(this, AlertsActivity.class);
        startActivity(intent);
    }

    public void showStatistics(View view) {
        Intent intent = new Intent(this, StatisticsActivity.class);
        startActivity(intent);
    }

    public void showCreatePdfReport(View view) {
        Intent intent = new Intent(this, CreatePdfReportActivity.class);
        startActivity(intent);
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
}