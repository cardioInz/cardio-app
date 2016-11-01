package cardio_app.activity.diary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import cardio_app.R;
import cardio_app.db.DbHelper;
import cardio_app.db.model.PressureData;
import cardio_app.viewmodel.PressureDataViewModel;

public class DiaryActivity extends AppCompatActivity {

    private static final String TAG = DiaryActivity.class.getName();
    private DbHelper dbHelper;

    public void addPressureData(View view) {
        Intent intent = new Intent(this, AddDiaryActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // floating button - to add new records
        FloatingActionButton addBtn = (FloatingActionButton) findViewById(R.id.add_button);
        addBtn.setOnClickListener(this::addPressureData);

        // list view - for record list
        ListView listView = (ListView) findViewById(R.id.diary_list_view);
        listView.setOnItemClickListener(((adapterView, view, i, l) -> Snackbar.make(view,
                "Selected item id: " + String.valueOf(view.getId())
                //"Here we're gonna make some Edit and Delete buttons for selected measurement. " +
                ,
                Snackbar.LENGTH_LONG
        ).setAction("Action", null).show()));

//        listView.setOnItemClickListener(((adapterView, view, i, l) -> {
//            PressureData pressureData = (PressureData) adapterView.getItemAtPosition(i);
//            Intent intent = new Intent(DiaryActivity.this, AddDiaryActivity.class);
//            intent.putExtra("pressuredata", pressureData);
//            startActivity(intent);
//        }));

        try {
            Dao<PressureData, Integer> dao = getHelper().getDao(PressureData.class);
            listView.setAdapter(new PressureDataAdapter(dao.queryForAll()));
        } catch (SQLException e) {
            Log.e(TAG, "Can't get pressure data records from sql dao", e);
            throw new RuntimeException(e);
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

    private DbHelper getHelper() {
        if (dbHelper == null) {
            dbHelper = OpenHelperManager.getHelper(this, DbHelper.class);
        }

        return dbHelper;
    }

    private class PressureDataAdapter extends ArrayAdapter<PressureData> {

        public PressureDataAdapter(List<PressureData> data) {
            super(DiaryActivity.this, R.layout.diary_list_item, data);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

                convertView = inflater.inflate(R.layout.diary_list_item, parent, false);
            }

//            TextView valuesTextView = (TextView) convertView.findViewById(R.id.pressuredata_values);
            TextView systoleTextView = (TextView) convertView.findViewById(R.id.pressuredata_systole);
            TextView diastoleTextView = (TextView) convertView.findViewById(R.id.pressuredata_diastole);
            TextView pulseTextView = (TextView) convertView.findViewById(R.id.pressuredata_pulse);
            TextView conditionTextView = (TextView) convertView.findViewById(R.id.pressuredata_condition);
            TextView arrhythmiaTextView = (TextView) convertView.findViewById(R.id.pressuredata_arrhythmia);
            TextView dateTextView = (TextView) convertView.findViewById(R.id.pressuredata_date);
            TextView timeTextView = (TextView) convertView.findViewById(R.id.pressuredata_time);

            PressureData pressureData = getItem(position);

            if (pressureData != null) {
                PressureDataViewModel viewModel = new PressureDataViewModel(pressureData);
//                valuesTextView.setText(viewModel.getValuesStr());
                systoleTextView.setText(viewModel.getSystoleStr());
                diastoleTextView.setText(viewModel.getDiastoleStr());
                pulseTextView.setText(viewModel.getPulseStr());
                conditionTextView.setText(viewModel.getConditionStr());
                arrhythmiaTextView.setText(viewModel.getArrhythmiaStr());
                dateTextView.setText(viewModel.getDateStr());
                timeTextView.setText(viewModel.getTimeStr());
            }

            return convertView;
        }
    }
}
