package cardio_app.activity.events;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import cardio_app.R;
import cardio_app.databinding.ActivityAddEventBinding;
import cardio_app.db.DbHelper;
import cardio_app.db.model.DailyActivitiesRecord;
import cardio_app.db.model.DoctorsAppointment;
import cardio_app.db.model.Emotion;
import cardio_app.db.model.EmotionHelper;
import cardio_app.db.model.Event;
import cardio_app.db.model.OtherSymptomsRecord;
import cardio_app.service.SetAlarmService;
import cardio_app.viewmodel.EventDataViewModel;
import cardio_app.viewmodel.date_time.PickedDateViewModel;
import cardio_app.viewmodel.date_time.PickedTimeViewModel;


public class AddEventActivity extends AppCompatActivity {

    Event currentEvent;
    boolean isEditExistingItem;
    PickedDateViewModel startDateModel, endDateModel;
    PickedTimeViewModel startTimeModel;
    EventDataViewModel currentEventViewModel;
    private DbHelper dbHelper;
    private Map<Integer, DailyActivitiesRecord> buttonToOtherEventMap;
    private Map<DailyActivitiesRecord, Integer> otherEventToButtonMap;

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
            initializeEmotionButton();
            createOtherEventToButtonMap();
            initializeOtherEventButton();
            initializeSymptomButtons();
            initializeMedicalAppointmentButtons();
        } else {
            currentEvent = new Event();
        }
        startDateModel = new PickedDateViewModel(currentEvent.getStartDate());
        endDateModel = new PickedDateViewModel(currentEvent.getEndDate());
        startTimeModel = new PickedTimeViewModel(currentEvent.getStartDate());
        currentEventViewModel = new EventDataViewModel(currentEvent);
        binding.setEvent(currentEventViewModel);
        binding.setStartDate(startDateModel);
        binding.setEndDate(endDateModel);
        binding.setStartTime(startTimeModel);
        createButtonToOtherEventMap();
    }

    public DbHelper getDbHelper() {
        if (dbHelper == null) {
            dbHelper = OpenHelperManager.getHelper(this, DbHelper.class);
        }
        return dbHelper;
    }

    private void initializeEmotionButton() {
        if (!currentEvent.getEmotion().equals(Emotion.NONE)) {
            Integer id = EmotionHelper.getButtonId(currentEvent.getEmotion());
            Button button = (Button) findViewById(id);
            button.setBackgroundResource(R.drawable.event_chosen_option);
        }
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

    private void initializeSymptomButtons() {
        Map<Integer, Callable<Boolean>> buttonToSymptomFunction = new HashMap<>();
        buttonToSymptomFunction.put(R.id.button_cough, currentEvent.getOtherSymptomsRecord()::isCough);
        buttonToSymptomFunction.put(R.id.button_fever, currentEvent.getOtherSymptomsRecord()::isHighTemperature);
        buttonToSymptomFunction.put(R.id.button_toothache, currentEvent.getOtherSymptomsRecord()::isToothache);
        buttonToSymptomFunction.put(R.id.button_headache, currentEvent.getOtherSymptomsRecord()::isHeadache);
        buttonToSymptomFunction.put(R.id.button_stomachache, currentEvent.getOtherSymptomsRecord()::isStomachAche);
        try {
            for (Integer buttonId : buttonToSymptomFunction.keySet()) {
                boolean isChecked = buttonToSymptomFunction.get(buttonId).call();
                if (isChecked) {
                    Button button = (Button) findViewById(buttonId);
                    button.setBackgroundResource(R.drawable.event_chosen_option);
                }
            }
        } catch (Exception e) {

        }
    }

    private void initializeMedicalAppointmentButtons() {
        Map<Integer, Callable<Boolean>> buttonToVisitTypeFunction = new HashMap<>();
        buttonToVisitTypeFunction.put(R.id.button_medical_checkout, currentEvent.getDoctorsAppointment()::isRoutineCheck);
        buttonToVisitTypeFunction.put(R.id.button_flu, currentEvent.getDoctorsAppointment()::isFlu);
        buttonToVisitTypeFunction.put(R.id.button_examination, currentEvent.getDoctorsAppointment()::isExamination);
        buttonToVisitTypeFunction.put(R.id.button_prescription, currentEvent.getDoctorsAppointment()::isForPrescription);
        buttonToVisitTypeFunction.put(R.id.button_emergency, currentEvent.getDoctorsAppointment()::isEmergency);
        try {
            for (Integer buttonId : buttonToVisitTypeFunction.keySet()) {
                boolean isChecked = buttonToVisitTypeFunction.get(buttonId).call();
                if (isChecked) {
                    Button button = (Button) findViewById(buttonId);
                    button.setBackgroundResource(R.drawable.event_chosen_option);
                }
            }
        } catch (Exception e) {

        }
    }

    private void initializeOtherEventButton() {
        if (otherEventToButtonMap.containsKey(currentEvent.getDailyActivitiesRecord())) {
            Integer id = otherEventToButtonMap.get(currentEvent.getDailyActivitiesRecord());
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
        currentEvent.setDailyActivitiesRecord(buttonToOtherEventMap.get(v.getId()));
        for (Integer id : buttonToOtherEventMap.keySet()) {
            Button b = (Button) findViewById(id);
            b.setBackground(null);
        }
        Button buttonClicked = (Button) findViewById(v.getId());
        buttonClicked.setBackgroundResource(R.drawable.event_chosen_option);
    }

    public void changeSymptomps(View v) {
        setSymptomProperty(v.getId());
        changeBackgroundForButton((Button) findViewById(v.getId()));
    }

    public void changeVisitType(View v) {
        setVisitProperty(v.getId());
        changeBackgroundForButton((Button) findViewById(v.getId()));
    }

    public boolean isEditEventFormValid() {
        boolean isValid = true;
        LinearLayout descriptionLinearLayout = (LinearLayout) findViewById(R.id.event_description_ll);
        LinearLayout timeDeltaLinearLayout = (LinearLayout) findViewById(R.id.event_time_delta_ll);
        LinearLayout timeUnitLinearLayout = (LinearLayout) findViewById(R.id.event_time_unit_ll);
        if (currentEventViewModel.getDescription() == "") {
            descriptionLinearLayout.setBackgroundResource(R.drawable.input_activities_background_error);
            isValid = false;
        } else {
            descriptionLinearLayout.setBackgroundResource(R.drawable.input_activities_background);
        }

        if (currentEventViewModel.getRepeatable()) {
            if (currentEventViewModel.getTimeDelta().equals("0") || currentEventViewModel.getTimeDelta().equals("")) {
                timeDeltaLinearLayout.setBackgroundResource(R.drawable.input_activities_background_error);
                isValid = false;
            } else {
                timeDeltaLinearLayout.setBackgroundResource(R.drawable.input_activities_background);
            }

            if (!currentEventViewModel.isDay() && !currentEventViewModel.isMonth() && !currentEventViewModel.isWeek()) {
                timeUnitLinearLayout.setBackgroundResource(R.drawable.input_activities_background_error);
                isValid = false;
            } else {
                timeDeltaLinearLayout.setBackgroundResource(R.drawable.input_activities_background);
            }
        }
        return isValid;
    }

    public void onSaveClick() {
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

                Intent updateAlarm = new Intent(this, SetAlarmService.class);
                updateAlarm.setAction(SetAlarmService.UPDATE);
                updateAlarm.putExtra(SetAlarmService.EVENT_ID, currentEvent.getId());
                startService(updateAlarm);
            } catch (Exception e) {

            }

            Intent intent = new Intent(this, EventActivity.class);
            startActivity(intent);
        } else {

        }
    }

    public void onDeleteClick() {
        if (currentEvent.getId() <= 0) {
            return;
        }

        try {
            Dao<DoctorsAppointment, Integer> doctorsAppointmentDao =
                    getDbHelper().getDao(DoctorsAppointment.class);
            doctorsAppointmentDao.deleteById(currentEvent.getDoctorsAppointment().getId());
            Dao<OtherSymptomsRecord, Integer> otherSymptomsRecordDao =
                    getDbHelper().getDao(OtherSymptomsRecord.class);
            otherSymptomsRecordDao.deleteById(currentEvent.getOtherSymptomsRecord().getId());
            Dao<Event, Integer> eventsDao = getDbHelper().getDao(Event.class);
            eventsDao.deleteById(currentEvent.getId());
            Uri uri = new Uri.Builder().path(String.valueOf(currentEvent.getId())).build();
            Intent cancelAlarm = new Intent(this, SetAlarmService.class);
            cancelAlarm.setAction(SetAlarmService.CANCEL);
            cancelAlarm.putExtra(SetAlarmService.EVENT_ID, currentEvent.getId());
            onBackPressed();
        } catch (SQLException e) {
            Log.e("", "Can't perform delete action on Event record", e);
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


    public void createButtonToOtherEventMap() {
        buttonToOtherEventMap = new HashMap<Integer, DailyActivitiesRecord>();
        buttonToOtherEventMap.put(R.id.button_other_fight, DailyActivitiesRecord.ARGUE);
        buttonToOtherEventMap.put(R.id.button_other_work, DailyActivitiesRecord.WORK);
        buttonToOtherEventMap.put(R.id.button_other_relax, DailyActivitiesRecord.RELAX);
        buttonToOtherEventMap.put(R.id.button_other_travel, DailyActivitiesRecord.TRAVEL);
        buttonToOtherEventMap.put(R.id.button_other_house_duties, DailyActivitiesRecord.HOUSE_DUTIES);
        buttonToOtherEventMap.put(R.id.button_other_car_driving, DailyActivitiesRecord.DRIVING_CAR);
        buttonToOtherEventMap.put(R.id.button_other_exam, DailyActivitiesRecord.EXAM);
        buttonToOtherEventMap.put(R.id.button_other_shopping, DailyActivitiesRecord.SHOPPING);
        buttonToOtherEventMap.put(R.id.button_other_sport, DailyActivitiesRecord.SPORT);
        buttonToOtherEventMap.put(R.id.button_other_walk, DailyActivitiesRecord.WALK);
        buttonToOtherEventMap.put(R.id.button_other_party, DailyActivitiesRecord.PARTY);
    }

    public void createOtherEventToButtonMap() {
        otherEventToButtonMap = new HashMap<DailyActivitiesRecord, Integer>();
        otherEventToButtonMap.put(DailyActivitiesRecord.ARGUE, R.id.button_other_fight);
        otherEventToButtonMap.put(DailyActivitiesRecord.WORK, R.id.button_other_work);
        otherEventToButtonMap.put(DailyActivitiesRecord.RELAX, R.id.button_other_relax);
        otherEventToButtonMap.put(DailyActivitiesRecord.TRAVEL, R.id.button_other_travel);
        otherEventToButtonMap.put(DailyActivitiesRecord.HOUSE_DUTIES, R.id.button_other_house_duties);
        otherEventToButtonMap.put(DailyActivitiesRecord.DRIVING_CAR, R.id.button_other_car_driving);
        otherEventToButtonMap.put(DailyActivitiesRecord.EXAM, R.id.button_other_exam);
        otherEventToButtonMap.put(DailyActivitiesRecord.SHOPPING, R.id.button_other_shopping);
        otherEventToButtonMap.put(DailyActivitiesRecord.SPORT, R.id.button_other_sport);
        otherEventToButtonMap.put(DailyActivitiesRecord.WALK, R.id.button_other_walk);
        otherEventToButtonMap.put(DailyActivitiesRecord.PARTY, R.id.button_other_party);
    }


    public void setSymptomProperty(int id) {
        switch (id) {
            case R.id.button_headache:
                boolean isHeadache = currentEvent.getOtherSymptomsRecord().isHeadache();
                currentEvent.getOtherSymptomsRecord().setHeadache(!isHeadache);
                break;
            case R.id.button_cough:
                boolean isCough = currentEvent.getOtherSymptomsRecord().isCough();
                currentEvent.getOtherSymptomsRecord().setCough(!isCough);
                break;
            case R.id.button_fever:
                boolean isHighTemperature = currentEvent.getOtherSymptomsRecord().isHighTemperature();
                currentEvent.getOtherSymptomsRecord().setHighTemperature(!isHighTemperature);
                break;
            case R.id.button_stomachache:
                boolean isStomachAche = currentEvent.getOtherSymptomsRecord().isStomachAche();
                currentEvent.getOtherSymptomsRecord().setStomachAche(!isStomachAche);
                break;
            case R.id.button_toothache:
                boolean isToothache = currentEvent.getOtherSymptomsRecord().isToothache();
                currentEvent.getOtherSymptomsRecord().setToothache(!isToothache);
                break;
        }
    }

    public void setVisitProperty(int id) {
        switch (id) {
            case R.id.button_medical_checkout:
                boolean isMedicalCheckout = currentEvent.getDoctorsAppointment().isRoutineCheck();
                currentEvent.getDoctorsAppointment().setRoutineCheck(!isMedicalCheckout);
                break;
            case R.id.button_examination:
                boolean isExamination = currentEvent.getDoctorsAppointment().isExamination();
                currentEvent.getDoctorsAppointment().setExamination(!isExamination);
                break;
            case R.id.button_prescription:
                boolean isForPrescription = currentEvent.getDoctorsAppointment().isForPrescription();
                currentEvent.getDoctorsAppointment().setForPrescription(!isForPrescription);
                break;
            case R.id.button_flu:
                boolean isFlu = currentEvent.getDoctorsAppointment().isFlu();
                currentEvent.getDoctorsAppointment().setFlu(!isFlu);
                break;
            case R.id.button_emergency:
                boolean isEmergency = currentEvent.getDoctorsAppointment().isEmergency();
                currentEvent.getDoctorsAppointment().setEmergency(!isEmergency);
                break;
        }
    }

}
