package cardio_app.statistics.analyse;

import java.util.HashMap;

import cardio_app.R;
import cardio_app.db.model.PressureData;

public class StatisticLastMeasure {
    private String title;
    private boolean isArrhythmiaImportant;
    private PressureData pressureData;

    public StatisticLastMeasure(Boolean isArrhythmiaImportant){
        this("", isArrhythmiaImportant, null);
    }

    public StatisticLastMeasure(Boolean isArrhythmiaImportant, PressureData pressureData) {
        this("", isArrhythmiaImportant, pressureData);
    }

    public StatisticLastMeasure(PressureData pressureData) {
        this("", true, pressureData);
    }

    private StatisticLastMeasure(String title, boolean isArrhythmiaImportant, PressureData pressureData) {
        this.title = title;
        this.isArrhythmiaImportant = isArrhythmiaImportant;
        if (pressureData != null)
            this.pressureData = new PressureData(pressureData);
    }

    public PressureData getPressureData() {
        return pressureData;
    }

    public void setPressureData(PressureData pressureData) {
        if (pressureData != null)
            this.pressureData = new PressureData(pressureData);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isArrhythmiaImportant() {
        return isArrhythmiaImportant;
    }

    public void setArrhythmiaImportant(boolean arrhythmiaImportant) {
        isArrhythmiaImportant = arrhythmiaImportant;
    }



    public enum TypeEnum {

        // order matters (order of statistics in list of last measurements)

        LAST,
        LAST_WELL,
        LAST_MIDDLE,
        LAST_BAD,
        LAST_BAD_DIFF,
        LAST_NO_ARRHYTHMIA,
        LAST_ARRHYTHMIA;

        private static final HashMap<TypeEnum, Integer> measureTitlesMap =
                new HashMap<TypeEnum, Integer>(){{
                    put(LAST, R.string.statistics_last_measure);
                    put(LAST_BAD, R.string.statistics_last_bad_condition_measure);
                    put(LAST_MIDDLE, R.string.statistics_last__middle_condition_measure);
                    put(LAST_WELL, R.string.statistics_last_well_condition_measure);
                    put(LAST_BAD_DIFF, R.string.statistics_bad_difference_condition_measure);
                    put(LAST_ARRHYTHMIA, R.string.statistics_last_arrhythmia_measure);
                    put(LAST_NO_ARRHYTHMIA, R.string.statistics_last_no_arrhythmia_measure);
                }};

        public Integer mapToTitleStringId(){
            return measureTitlesMap.get(this);
        }
    }
}
