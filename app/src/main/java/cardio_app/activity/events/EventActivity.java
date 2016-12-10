package cardio_app.activity.events;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import cardio_app.service.SetAlarmService;

public class EventActivity extends AppCompatActivity {
    private static final String TAG = EventActivity.class.getName();
    private DbHelper dbHelper;
    private EventAdapter eventAdapter;
    private Event selectedEvent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        ListView eventListView = (ListView) findViewById(R.id.event_list_view);

        FloatingActionButton addBtn = (FloatingActionButton) findViewById(R.id.add_button);
        addBtn.setOnClickListener(this::addEventData);

        eventListView.setOnItemClickListener(((adapterView, view, i, l) -> {
            Event event = (Event) adapterView.getItemAtPosition(i);
            Intent intent = new Intent(EventActivity.this, AddEventActivity.class);
            intent.putExtra("event", event);
            startActivity(intent);

        }));

        assignDataToListView();
    }

    private void addEventData(View view) {
        Intent intent = new Intent(this, AddEventActivity.class);
        startActivity(intent);
    }

    private void assignDataToListView() {
        List<Event> events = new ArrayList<>();
        try {
            List<Event> eventsFromDb = getDbHelper().getAllOrderedEventData();
            for (Event event : eventsFromDb) {
                Dao<OtherSymptomsRecord, Integer> osrDao = getDbHelper().getDao(OtherSymptomsRecord.class);
                event.setOtherSymptomsRecord(osrDao.queryForId(event.getOtherSymptomsRecord().getId()));
                Dao<DoctorsAppointment, Integer> daDao = getDbHelper().getDao(DoctorsAppointment.class);
                event.setDoctorsAppointment(daDao.queryForId(event.getDoctorsAppointment().getId()));
                events.add(event);
            }

            ListView eventListView = (ListView) findViewById(R.id.event_list_view);
            eventAdapter = new EventAdapter(EventActivity.this, eventsFromDb);
            eventListView.setAdapter(eventAdapter);
            eventListView.invalidateViews();

        } catch (SQLException e) {
            Log.e(TAG, "Cannot get events", e);
            throw new RuntimeException(e);
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
