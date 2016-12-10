package cardio_app.activity.events;

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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import cardio_app.R;
import cardio_app.databinding.ActivityAddEventBinding;
import cardio_app.db.DbHelper;
import cardio_app.db.model.DoctorsAppointment;
import cardio_app.db.model.Emotion;
import cardio_app.db.model.Event;
import cardio_app.db.model.OtherSymptomsRecord;
import cardio_app.db.model.helpers.DailyActivitiesRecordHelper;
import cardio_app.db.model.helpers.DoctorsAppointmentHelper;
import cardio_app.db.model.helpers.EmotionHelper;
import cardio_app.db.model.helpers.OtherSymptomsRecordHelper;
import cardio_app.service.SetAlarmService;
import cardio_app.viewmodel.EventDataViewModel;
import cardio_app.viewmodel.date_time.PickedDateViewModel;
import cardio_app.viewmodel.date_time.PickedTimeViewModel;


public class AddEventActivity extends AppCompatActivity {
    private static final String TAG = AddEventActivity.class.getName();
    Event currentEvent;
    boolean isEditExistingItem;
    PickedDateViewModel startDateModel, endDateModel;
    PickedTimeViewModel startTimeModel;
    EventDataViewModel currentEventViewModel;
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        Intent intent = getIntent();
        Event event = intent.getParcelableExtra("event");

        isEditExistingItem = event != null;
        ActivityAddEventBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_event);

        if (isEditExistingItem) {
            currentEvent = event;
            initializeButtons();
        } else {
            currentEvent = new Event();
        }

        startDateModel = new PickedDateViewModel(currentEvent.getStartDate());
        endDateModel = new PickedDateViewModel(currentEvent.getEndDate());
        startTimeModel = new PickedTimeViewModel(currentEvent.getStartDate());
        currentEventViewModel = new EventDataViewModel(currentEvent);
        setBindingObjects(binding);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_item, menu);
        if (!isEditExistingItem) {
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

    public DbHelper getDbHelper() {
        if (dbHelper == null) {
            dbHelper = OpenHelperManager.getHelper(this, DbHelper.class);
        }
        return dbHelper;
    }

    private void initializeButtons() {
        initializeEmotionButton();
        initializeOtherEventButton();
        initializeSymptomButtons();
        initializeMedicalAppointmentButtons();
    }

    private void setBindingObjects(ActivityAddEventBinding binding) {
        binding.setEvent(currentEventViewModel);
        binding.setStartDate(startDateModel);
        binding.setEndDate(endDateModel);
        binding.setStartTime(startTimeModel);
    }

    private void initializeEmotionButton() {
        if (!currentEvent.getEmotion().equals(Emotion.NONE)) {
            Integer id = EmotionHelper.getButtonId(currentEvent.getEmotion());
            Button button = (Button) findViewById(id);
            button.setBackgroundResource(R.drawable.event_chosen_option);
        }
    }

    private void initializeSymptomButtons() {
        OtherSymptomsRecordHelper otherSymptomsRecordHelper = new OtherSymptomsRecordHelper(currentEvent.getOtherSymptomsRecord());
        try {
            for (Integer buttonId : otherSymptomsRecordHelper.getSymptomButtons()) {
                boolean isChecked = otherSymptomsRecordHelper.getSymptomFunction(buttonId).call();
                if (isChecked) {
                    Button button = (Button) findViewById(buttonId);
                    button.setBackgroundResource(R.drawable.event_chosen_option);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Cannot initialize symptom buttons", e);
            throw new RuntimeException(e);
        }
    }

    private void initializeMedicalAppointmentButtons() {
        DoctorsAppointmentHelper doctorsAppointmentHelper = new DoctorsAppointmentHelper(currentEvent.getDoctorsAppointment());
        try {
            for (Integer buttonId : doctorsAppointmentHelper.getVisitTypeButtons()) {
                boolean isChecked = doctorsAppointmentHelper.getVisitTypeFunction(buttonId).call();
                if (isChecked) {
                    Button button = (Button) findViewById(buttonId);
                    button.setBackgroundResource(R.drawable.event_chosen_option);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Cannot initialize medical appointment buttons", e);
            throw new RuntimeException(e);
        }
    }

    private void initializeOtherEventButton() {
        if (DailyActivitiesRecordHelper.getActivitiesSet().contains(currentEvent.getDailyActivitiesRecord())) {
            Integer id = DailyActivitiesRecordHelper.getButtonId(currentEvent.getDailyActivitiesRecord());
            Button button = (Button) findViewById(id);
            button.setBackgroundResource(R.drawable.event_chosen_option);
        }
    }

    private Calendar getCalendarForModel(PickedDateViewModel dateModel, PickedTimeViewModel timeModel) {
        Calendar cal = Calendar.getInstance();
        cal.set(
                dateModel.getYear(),
                dateModel.getMonth(),
                dateModel.getDay(),
                timeModel.getHourOfDay(),
                timeModel.getMinute()
        );
        return cal;
    }

    private void updateStartDate() {
        Calendar cal = getCalendarForModel(startDateModel, startTimeModel);
        currentEvent.setStartDate(cal.getTime());
    }

    private void updateEndDate() {
        Calendar cal = getCalendarForModel(endDateModel, new PickedTimeViewModel(new Date()));
        currentEvent.setEndDate(cal.getTime());
    }

    public void changeEmotion(View v) {
        currentEvent.setEmotion(EmotionHelper.getEmotion(v.getId()));
        for (Integer id : EmotionHelper.getButtonIdsKeys()) {
            Button b = (Button) findViewById(id);
            b.setBackground(null);
        }
        Button buttonClicked = (Button) findViewById(v.getId());
        buttonClicked.setBackgroundResource(R.drawable.event_chosen_option);
    }

    public void changeEventType(View v) {
        currentEvent.setDailyActivitiesRecord(DailyActivitiesRecordHelper.getRecord(v.getId()));
        for (Integer id : DailyActivitiesRecordHelper.getButtonsSet()) {
            Button b = (Button) findViewById(id);
            b.setBackground(null);
        }
        Button buttonClicked = (Button) findViewById(v.getId());
        buttonClicked.setBackgroundResource(R.drawable.event_chosen_option);
    }

    public void changeSymptomps(View v) {
        OtherSymptomsRecordHelper.setSymptomProperty(v.getId(), currentEvent.getOtherSymptomsRecord());
        changeBackgroundForButton((Button) findViewById(v.getId()));
    }

    public void changeVisitType(View v) {
        DoctorsAppointmentHelper.setVisitProperty(v.getId(), currentEvent.getDoctorsAppointment());
        changeBackgroundForButton((Button) findViewById(v.getId()));
    }

    public boolean isEditEventFormValid() {
        LinearLayout descriptionLinearLayout = (LinearLayout) findViewById(R.id.event_description_ll);
        LinearLayout timeDeltaLinearLayout = (LinearLayout) findViewById(R.id.event_time_delta_ll);
        LinearLayout timeUnitLinearLayout = (LinearLayout) findViewById(R.id.event_time_unit_ll);
        boolean isDescriptionValid = AddEventFormValidator.validateDescription(descriptionLinearLayout, currentEventViewModel);
        boolean isRepeatableSectionValid = AddEventFormValidator.validateRepeatableSection(timeDeltaLinearLayout, timeUnitLinearLayout, currentEventViewModel);

        return isDescriptionValid && isRepeatableSectionValid;
    }


    public void saveEvent() {
        if (isEditEventFormValid()) {
            updateStartDate();
            updateEndDate();
            try {
                Dao<DoctorsAppointment, Integer> daoDoctorsAppointment = getDbHelper().getDao(DoctorsAppointment.class);
                Dao<OtherSymptomsRecord, Integer> daoSymptomsRecord = getDbHelper().getDao(OtherSymptomsRecord.class);
                Dao<Event, Integer> dao = getDbHelper().getDao(Event.class);
                if (!isEditExistingItem) {
                    daoDoctorsAppointment.create(currentEvent.getDoctorsAppointment());
                    daoSymptomsRecord.create(currentEvent.getOtherSymptomsRecord());
                    dao.create(currentEvent);
                } else {
                    daoDoctorsAppointment.update(currentEvent.getDoctorsAppointment());
                    daoSymptomsRecord.update(currentEvent.getOtherSymptomsRecord());
                    dao.update(currentEvent);
                }
                updateAlaram();
                Toast.makeText(this, R.string.event_saved_successfully, Toast.LENGTH_SHORT).show();
                onBackPressed();
            } catch (SQLException e) {
                Toast.makeText(this, R.string.error_while_saving_event, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Cannot save event", e);
                throw new RuntimeException(e);
            }
        } else {
            Toast.makeText(this, R.string.form_invalid, Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteEvent() {
        if (currentEvent.getId() <= 0)
            return;

        try {
            Dao<DoctorsAppointment, Integer> doctorsAppointmentDao = getDbHelper().getDao(DoctorsAppointment.class);
            doctorsAppointmentDao.deleteById(currentEventViewModel.getEvent().getDoctorsAppointment().getId());

            Dao<OtherSymptomsRecord, Integer> otherSymptomsRecordDao = getDbHelper().getDao(OtherSymptomsRecord.class);
            otherSymptomsRecordDao.deleteById(currentEventViewModel.getEvent().getOtherSymptomsRecord().getId());

            Dao<Event, Integer> eventsDao = getDbHelper().getDao(Event.class);
            eventsDao.deleteById(currentEventViewModel.getEvent().getId());

            cancelAlarm();
            onBackPressed();
            Toast.makeText(this, R.string.event_deleted_successfully, Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            Toast.makeText(this, R.string.error_while_deleting_event, Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Cannot perform Delete action on event", e);
            throw new RuntimeException(e);
        }
    }

    private void cancelAlarm() {
        Intent cancelAlarm = new Intent(this, SetAlarmService.class);
        cancelAlarm.setAction(SetAlarmService.CANCEL);
        cancelAlarm.putExtra(SetAlarmService.EVENT_ID, currentEvent.getId());
    }

    private void updateAlaram() {
        Intent updateAlarm = new Intent(this, SetAlarmService.class);
        updateAlarm.setAction(SetAlarmService.UPDATE);
        updateAlarm.putExtra(SetAlarmService.EVENT_ID, currentEvent.getId());
        startService(updateAlarm);
    }

    public void onSaveClick() {
        if (isEditExistingItem) {
            View contextView = findViewById(R.id.activity_add_event);
            Snackbar
                    .make(contextView, R.string.are_you_sure_to_save_changes, Snackbar.LENGTH_LONG)
                    .setAction(R.string.save, view -> {
                        saveEvent();

                    })
                    .setActionTextColor(ContextCompat.getColor(this, R.color.brightOnDarkBg))
                    .show();
        } else {
            saveEvent();
        }
    }

    private void onDeleteClick() {
        if (isEditExistingItem) {
            View contextView = findViewById(R.id.activity_add_event);
            Snackbar
                    .make(contextView, R.string.are_you_sure_delete_event, Snackbar.LENGTH_LONG)
                    .setAction(R.string.delete, view -> {
                        deleteEvent();
                        onBackPressed();
                    })
                    .setActionTextColor(ContextCompat.getColor(this, R.color.brightOnDarkBg))
                    .show();
        } else {
            deleteEvent();
            onBackPressed();
        }
    }

    public void saveEvent(View v) {
        onSaveClick();
    }

    public void changeBackgroundForButton(Button buttonClicked) {
        if (buttonClicked.getBackground() == null) {
            buttonClicked.setBackgroundResource(R.drawable.event_chosen_option);
        } else {
            buttonClicked.setBackground(null);
        }
    }
}
