package cardio_app.db.model;


import java.util.HashMap;
import java.util.Map;
import cardio_app.R;

public class DailyActivitiesRecordHelper {


    private static final Map<DailyActivitiesRecord, Integer> otherEventToImageMap;
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

    private static final Map<DailyActivitiesRecord, Integer> otherEventToDescriptionMap;
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


}
