package cardio_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import cardio_app.R;
import cardio_app.activity.alert.AlertsActivity;
import cardio_app.activity.diary.AddDiaryActivity;
import cardio_app.activity.diary.DiaryActivity;
import cardio_app.activity.drug.DrugsActivity;
import cardio_app.activity.statistics.StatisticsActivity;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

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

}