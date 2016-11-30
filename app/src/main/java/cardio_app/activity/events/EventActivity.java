package cardio_app.activity.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cardio_app.R;
import cardio_app.db.DbHelper;
import cardio_app.db.model.DoctorsAppointment;
import cardio_app.db.model.Event;
import cardio_app.db.model.OtherSymptomsRecord;

public class EventActivity extends AppCompatActivity {

    private DbHelper dbHelper;
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);


        ListView eventListView = (ListView) findViewById(R.id.event_list_view);


        eventListView.setOnItemLongClickListener(((adapterView, view, i, l) -> {
            Event event = (Event) adapterView.getItemAtPosition(i);
            Intent intent = new Intent(EventActivity.this, AddEventActivity.class);
            intent.putExtra("event", event);
//            intent.putExtra("timeUnit", event.getTimeUnit());
            startActivity(intent);
            return true;
        }));

        assignDataToListView();
    }


    private void assignDataToListView() {
        List<Event> events = new ArrayList<>();
        try {
//            Dao<Event, Integer> dao = getDbHelper().getDao(Event.class);

            for (Event event : getDbHelper().getAllOrderedEventData()) {
                Dao<OtherSymptomsRecord, Integer> osrDao = getDbHelper().getDao(OtherSymptomsRecord.class);
                event.setOtherSymptomsRecord(osrDao.queryForId(event.getOtherSymptomsRecord().getId()));
                Dao<DoctorsAppointment, Integer> daDao = getDbHelper().getDao(DoctorsAppointment.class);
                event.setDoctorsAppointment(daDao.queryForId(event.getDoctorsAppointment().getId()));
                events.add(event);
            }

            ListView eventListView = (ListView) findViewById(R.id.event_list_view);
            eventAdapter = new EventAdapter(EventActivity.this, events);
            eventListView.setAdapter(eventAdapter);
            eventListView.invalidateViews();

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

    @Override
    protected void onResume() {
        assignDataToListView();
        super.onResume();
    }

}
