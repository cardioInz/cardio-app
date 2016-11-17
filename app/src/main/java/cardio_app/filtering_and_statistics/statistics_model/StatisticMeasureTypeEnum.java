package cardio_app.filtering_and_statistics.statistics_model;

import java.util.HashMap;

import cardio_app.R;

/**
 * Created by kisam on 15.11.2016.
 */

public enum StatisticMeasureTypeEnum {

    // order matters (order of statistics in list of last measurements)

    LAST,
    LAST_WELL,
    LAST_MIDDLE,
    LAST_BAD,
    LAST_BAD_DIFF,
    LAST_NO_ARRHYTHMIA,
    LAST_ARRHYTHMIA;

    private static HashMap<StatisticMeasureTypeEnum, Integer> measureTitlesMap = new HashMap<>();
    static {
        measureTitlesMap.put(LAST, R.string.statistics_last_measure);
        measureTitlesMap.put(LAST_BAD, R.string.statistics_last_bad_condition_measure);
        measureTitlesMap.put(LAST_MIDDLE, R.string.statistics_last__middle_condition_measure);
        measureTitlesMap.put(LAST_WELL, R.string.statistics_last_well_condition_measure);
        measureTitlesMap.put(LAST_BAD_DIFF, R.string.statistics_bad_difference_condition_measure);
        measureTitlesMap.put(LAST_ARRHYTHMIA, R.string.statistics_last_arrhythmia_measure);
        measureTitlesMap.put(LAST_NO_ARRHYTHMIA, R.string.statistics_last_no_arrhythmia_measure);
    }

    public Integer mapToTitleStringId(){
        return measureTitlesMap.get(this);
    }
}