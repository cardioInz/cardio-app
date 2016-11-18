package cardio_app.statistics;

import cardio_app.db.model.PressureData;

/**
 * Created by kisam on 15.11.2016.
 */

public class StatisticMeasure {
    private String title;
    private boolean isArrhythmiaImportant;
    private PressureData pressureData;

    public StatisticMeasure(Boolean isArrhythmiaImportant){
        this("", isArrhythmiaImportant, null);
    }

    public StatisticMeasure(Boolean isArrhythmiaImportant, PressureData pressureData) {
        this("", isArrhythmiaImportant, pressureData);
    }

    public StatisticMeasure(PressureData pressureData) {
        this("", true, pressureData);
    }

    public StatisticMeasure(String title, boolean isArrhythmiaImportant, PressureData pressureData) {
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
}
