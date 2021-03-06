package cardio_app.db.model.helpers;

import java.util.HashMap;
import java.util.Map;

import cardio_app.R;
import cardio_app.db.model.TimeUnit;

public class TimeUnitHelper {

    private static final Map<TimeUnit, Integer> timeUnitToDescriptionMap;

    static {
        timeUnitToDescriptionMap = new HashMap<>();
        timeUnitToDescriptionMap.put(TimeUnit.DAY, R.string.day);
        timeUnitToDescriptionMap.put(TimeUnit.MONTH, R.string.month);
        timeUnitToDescriptionMap.put(TimeUnit.WEEK, R.string.week);
    }

    public static Integer getDescription(TimeUnit timeUnit) {
        return timeUnitToDescriptionMap.get(timeUnit);
    }
}
