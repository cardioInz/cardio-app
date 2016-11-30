package cardio_app.statistics.analyse;

import java.util.HashMap;

import cardio_app.R;

public class StatisticCounter {

    private final HashMap<TypeEnum, Integer> mapCounter = new HashMap<TypeEnum, Integer>() {{
        for (TypeEnum measureEnum : TypeEnum.values()) {
            put(measureEnum, 0);
        }
    }};


    public StatisticCounter() {
        zeroAll();
    }

    public void incValueOfKey(TypeEnum key) {
        if (mapCounter.containsKey(key))
            mapCounter.put(key, mapCounter.get(key) + 1);
    }

    public void zeroAll() {
        for (TypeEnum key : mapCounter.keySet()) {
            mapCounter.put(key, 0);
        }
    }

    public Integer getCnt(TypeEnum key) {
        if (mapCounter.containsKey(key))
            return mapCounter.get(key);
        else
            return null;
    }

    public void setCntOfKey(TypeEnum key, Integer value) {
        if (mapCounter.containsKey(key))
            mapCounter.put(key, value);
    }

    public HashMap<TypeEnum, Integer> getMapCounter() {
        return mapCounter;
    }

    public enum TypeEnum {
        TOTAL,
        WELL,
        LITTLE_LOW_OR_HIGH,
        BAD,
        BAD_DIFF,
        NO_ARRHYTHMIA,
        ARRHYTHMIA,
        UNKNOWN;

        private static final HashMap<TypeEnum, Integer> mapTitleID = new HashMap<TypeEnum, Integer>() {{
            put(TOTAL, R.string.statistics_counter_total);
            put(WELL, R.string.statistics_counter_well);
            put(LITTLE_LOW_OR_HIGH, R.string.statistics_counter_middle);
            put(BAD, R.string.statistics_counter_bad);
            put(BAD_DIFF, R.string.statistics_counter_bad_diff);
            put(NO_ARRHYTHMIA, R.string.statistics_counter_no_arrhythmia);
            put(ARRHYTHMIA, R.string.statistics_counter_arrhythmia);
            put(UNKNOWN, R.string.statistics_counter_errors);
        }};

        private static Integer mapToTitleId(TypeEnum key) {
            if (mapTitleID.containsKey(key))
                return mapTitleID.get(key);
            else
                return null;
        }

        public Integer toTitleId() {
            return mapToTitleId(this);
        }
    }
}
