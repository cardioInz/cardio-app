package cardio_app.activity.events;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cardio_app.R;
import cardio_app.databinding.ActivityAddEventBinding;
import cardio_app.db.DbHelper;
import cardio_app.db.model.DailyActivitiesRecord;
import cardio_app.db.model.DoctorsAppointment;
import cardio_app.db.model.Emotion;
import cardio_app.db.model.Event;
import cardio_app.db.model.OtherSymptomsRecord;
import cardio_app.viewmodel.EventDataViewModel;
import cardio_app.viewmodel.date_time.PickedDateViewModel;
import cardio_app.viewmodel.date_time.PickedTimeViewModel;


public class AddEventActivity extends AppCompatActivity {

    private GoogleApiClient client;
    private DbHelper dbHelper;
    private Map<Integer, Emotion> buttonToEmotionMap;
    private Map<Integer, DailyActivitiesRecord> buttonToOtherEventMap;
    private Map<Emotion, Integer> emotionToButtonMap;
    private Map<DailyActivitiesRecord, Integer> otherEventToButtonMap;
    Event currentEvent;
    boolean isEditExistingItem;
    PickedDateViewModel startDateModel, endDateModel;
    PickedTimeViewModel startTimeModel;


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
//            createEmotionToButtonMap();
//            initializeEmotionButton();
//            createOtherEventToButtonMap();
//            initializeOtherEventButton();
        } else {
            currentEvent = new Event();
        }
        startDateModel = new PickedDateViewModel(currentEvent.getStartDate());
        endDateModel = new PickedDateViewModel(currentEvent.getEndDate());
        startTimeModel = new PickedTimeViewModel(currentEvent.getStartDate());
        binding.setEvent(new EventDataViewModel(currentEvent));
        binding.setStartDate(startDateModel);
        binding.setEndDate(endDateModel);
        binding.setStartTime(startTimeModel);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        createButtonToEmotionMap();
        createButtonToOtherEventMap();
    }

    public DbHelper getDbHelper() {
        if (dbHelper == null) {
            dbHelper = OpenHelperManager.getHelper(this, DbHelper.class);
        }
        return dbHelper;
    }

    private void initializeEmotionButton() {
        Integer id = emotionToButtonMap.get(currentEvent.getEmotion());
        Button button = (Button)findViewById(id);
        button.setBackgroundResource(R.drawable.event_chosen_option);
    }

    private void initializeSymptomButtons() {

    }

    private void initializeMedicalAppointmentButtons() {

    }

    private void initializeOtherEventButton() {
        Integer id = otherEventToButtonMap.get(currentEvent.getDailyActivitiesRecord());
        Button button = (Button)findViewById(id);
        button.setBackgroundResource(R.drawable.event_chosen_option);
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
        currentEvent.setEmotion(buttonToEmotionMap.get(v.getId()));
        for(Integer id: buttonToEmotionMap.keySet()) {
            Button b = (Button) findViewById(id);
            b.setBackground(null);
        }
        Button buttonClicked = (Button)findViewById(v.getId());
        buttonClicked.setBackgroundResource(R.drawable.event_chosen_option);
    }

    public void changeEventType(View v) {
        currentEvent.setDailyActivitiesRecord(buttonToOtherEventMap.get(v.getId()));
        for(Integer id: buttonToOtherEventMap.keySet()) {
            Button b = (Button) findViewById(id);
            b.setBackground(null);
        }
        Button buttonClicked = (Button)findViewById(v.getId());
        buttonClicked.setBackgroundResource(R.drawable.event_chosen_option);
    }

    public void changeSymptomps(View v) {
        setSymptomProperty(v.getId());
        changeBackgroundForButton((Button)findViewById(v.getId()));
    }

    public void changeVisitType(View v) {
        setVisitProperty(v.getId());
        changeBackgroundForButton((Button)findViewById(v.getId()));
    }

    public void saveEvent(View v) {
        updateStartDate();
        updateEndDate();
        try {
            Dao<DoctorsAppointment, Integer> daoDoctorsAppointment = getDbHelper().getDao(DoctorsAppointment.class);
            Dao<OtherSymptomsRecord, Integer> daoSymptomsRecord = getDbHelper().getDao(OtherSymptomsRecord.class);
            Dao<Event, Integer> dao = getDbHelper().getDao(Event.class);
            if(!isEditExistingItem) {
                daoDoctorsAppointment.create(currentEvent.getDoctorsAppointment());
                daoSymptomsRecord.create(currentEvent.getOtherSymptomsRecord());
                dao.create(currentEvent);
            } else {
                daoDoctorsAppointment.update(currentEvent.getDoctorsAppointment());
                daoSymptomsRecord.update(currentEvent.getOtherSymptomsRecord());
                dao.update(currentEvent);
            }

        } catch (Exception e) {

        }

        Intent intent = new Intent(this, EventActivity.class);
        startActivity(intent);
    }

    public void changeBackgroundForButton(Button buttonClicked) {
        if (buttonClicked.getBackground() == null) {
            buttonClicked.setBackgroundResource(R.drawable.event_chosen_option);
        } else {
            buttonClicked.setBackground(null);
        }
    }

    public void createButtonToEmotionMap() {
        buttonToEmotionMap = new HashMap<Integer, Emotion>();
        buttonToEmotionMap.put(R.id.button_happy, Emotion.HAPPY);
        buttonToEmotionMap.put(R.id.button_sad, Emotion.SAD);
        buttonToEmotionMap.put(R.id.button_angry, Emotion.ANGRY);
        buttonToEmotionMap.put(R.id.button_crying, Emotion.CRYING);
        buttonToEmotionMap.put(R.id.button_stressed, Emotion.STRESSED);
    }

    public void createEmotionToButtonMap() {
        emotionToButtonMap = new HashMap<Emotion, Integer>();
        emotionToButtonMap.put(Emotion.HAPPY, R.id.button_happy);
        emotionToButtonMap.put(Emotion.SAD, R.id.button_sad );
        emotionToButtonMap.put(Emotion.ANGRY, R.id.button_angry);
        emotionToButtonMap.put(Emotion.CRYING, R.id.button_crying);
        emotionToButtonMap.put(Emotion.STRESSED, R.id.button_stressed);
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
