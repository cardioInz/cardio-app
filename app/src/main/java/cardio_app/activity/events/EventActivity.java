package cardio_app.activity.events;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import cardio_app.R;
import cardio_app.db.DbHelper;
import cardio_app.db.model.Event;

public class EventActivity extends AppCompatActivity {

    private DbHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        assignDataToListView();
    }

    private void assignDataToListView() {
        try {
            Dao<Event, Integer> dao = getDbHelper().getDao(Event.class);
//            TableUtils.dropTable(getDbHelper().getConnectionSource(), Event.class, true);
//            TableUtils.createTableIfNotExists(getDbHelper().getConnectionSource(), Event.class);
//            TableUtils.createTableIfNotExists(getDbHelper().getConnectionSource(), OtherSymptomsRecord.class);
//            TableUtils.createTableIfNotExists(getDbHelper().getConnectionSource(), DoctorsAppointment.class);
//            dbHelper.initEventData();
            for (Event event : dao.queryForAll()) {
                Log.i("", "Got 1 record");
            }
        } catch (SQLException e) {
            Log.e("", "Cannot get events");
        }
    }

    public DbHelper getDbHelper() {
        if (dbHelper == null) {
            dbHelper = OpenHelperManager.getHelper(this, DbHelper.class);
        }

        return dbHelper;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (dbHelper != null) {
            OpenHelperManager.releaseHelper();
            dbHelper = null;
        }
    }
}
