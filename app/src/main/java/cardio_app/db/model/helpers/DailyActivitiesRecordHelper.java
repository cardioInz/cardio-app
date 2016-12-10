package cardio_app.db.model.helpers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cardio_app.R;
import cardio_app.db.model.DailyActivitiesRecord;

public class DailyActivitiesRecordHelper {

    private static final Map<Integer, DailyActivitiesRecord> buttonToOtherEventMap;
    private static final Map<DailyActivitiesRecord, Integer> otherEventToButtonMap;
    private static final Map<DailyActivitiesRecord, Integer> otherEventToImageMap;
    private static final Map<DailyActivitiesRecord, Integer> otherEventToDescriptionMap;

    static {
        buttonToOtherEventMap = new HashMap<>();
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

    static {
        otherEventToButtonMap = new HashMap<>();
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

    static {
        otherEventToImageMap = new HashMap<>();
        otherEventToImageMap.put(DailyActivitiesRecord.ARGUE, R.drawable.event_other_fight);
        otherEventToImageMap.put(DailyActivitiesRecord.DRIVING_CAR, R.drawable.event_other_car_driving);
        otherEventToImageMap.put(DailyActivitiesRecord.EXAM, R.drawable.event_other_exam);
        otherEventToImageMap.put(DailyActivitiesRecord.HOUSE_DUTIES, R.drawable.event_other_house_duties);
        otherEventToImageMap.put(DailyActivitiesRecord.PARTY, R.drawable.event_other_party);
        otherEventToImageMap.put(DailyActivitiesRecord.RELAX, R.drawable.event_other_relax);
        otherEventToImageMap.put(DailyActivitiesRecord.SHOPPING, R.drawable.event_other_shopping);
        otherEventToImageMap.put(DailyActivitiesRecord.SPORT, R.drawable.event_other_sport);
        otherEventToImageMap.put(DailyActivitiesRecord.TRAVEL, R.drawable.event_other_travel);
        otherEventToImageMap.put(DailyActivitiesRecord.WALK, R.drawable.event_other_walk);
        otherEventToImageMap.put(DailyActivitiesRecord.WORK, R.drawable.event_other_work);
    }

    static {
        otherEventToDescriptionMap = new HashMap<>();
        otherEventToDescriptionMap.put(DailyActivitiesRecord.ARGUE, R.string.argue);
        otherEventToDescriptionMap.put(DailyActivitiesRecord.DRIVING_CAR, R.string.driving_a_car);
        otherEventToDescriptionMap.put(DailyActivitiesRecord.EXAM, R.string.exam);
        otherEventToDescriptionMap.put(DailyActivitiesRecord.HOUSE_DUTIES, R.string.house_duties);
        otherEventToDescriptionMap.put(DailyActivitiesRecord.PARTY, R.string.party);
        otherEventToDescriptionMap.put(DailyActivitiesRecord.RELAX, R.string.relax);
        otherEventToDescriptionMap.put(DailyActivitiesRecord.SHOPPING, R.string.shopping);
        otherEventToDescriptionMap.put(DailyActivitiesRecord.SPORT, R.string.sport);
        otherEventToDescriptionMap.put(DailyActivitiesRecord.TRAVEL, R.string.travel);
        otherEventToDescriptionMap.put(DailyActivitiesRecord.WALK, R.string.walk);
        otherEventToDescriptionMap.put(DailyActivitiesRecord.WORK, R.string.work);
    }


    public static Integer getImageId(DailyActivitiesRecord record) {
        return otherEventToImageMap.get(record);
    }

    public static Integer getDescription(DailyActivitiesRecord record) {
        return otherEventToDescriptionMap.get(record);
    }

    public static DailyActivitiesRecord getRecord(int buttonId) {
        return buttonToOtherEventMap.get(buttonId);
    }

    public static Integer getButtonId(DailyActivitiesRecord record) {
        return otherEventToButtonMap.get(record);
    }

    public static Set<Integer> getButtonsSet() {
        return buttonToOtherEventMap.keySet();
    }

    public static Set<DailyActivitiesRecord> getActivitiesSet() {
        return otherEventToButtonMap.keySet();
    }

}
