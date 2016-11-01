package cardio_app.activity.diary;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import cardio_app.R;


public class AddDiaryActivity extends AppCompatActivity {

//    private void addTableRowToLayout(PressureData hp, boolean showToast) {
//        editPressureData(hp);
//
//        if (showToast) {
//            Toast.makeText(
//                    getApplicationContext(),
//                    getResources().getString(R.string.new_record_added_msg)
//                            + ", ID: " + String.valueOf(tableRowPD.getId()),
//                    Toast.LENGTH_SHORT
//            ).show();
//        }
//    }


//    private void editPressureData(PressureData pressureData) {
//        try {
//            Dao<PressureData, Integer> pressureDao = getHelper().getDao(PressureData.class);
//            if (pressureData.getId() == 0) {
//                pressureDao.create(pressureData);
//            } else {
//                pressureDao.update(pressureData);
//            }
//        } catch (SQLException e) {
//            Log.e(TAG, "Can't perform create/update action on PressureData record", e);
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.save_diary_btn);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }

}
