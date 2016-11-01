package cardio_app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import cardio_app.db.model.PressureData;
import cardio_app.BR;

/**
 * Created by kisam on 27.10.2016.
 */

public class PressureDataViewModel extends BaseObservable implements Comparable<PressureDataViewModel> {
    private PressureData pressureData;
    public static final String ARRHYTHMIA_STR = "A";
    public static final String NO_ARRHYTHMIA_STR = "-";

    public PressureDataViewModel() {
        this.pressureData = new PressureData();
    }

    public PressureDataViewModel(PressureData pressureData){
        this.pressureData = pressureData;
    }

    @Bindable
    public String getConditionStr(){
        return pressureData.getCondition().getStrMapped();
    }

//    @Bindable
//    public String getValuesStr() {
//        return String.format("%s/%s/%s", getSystoleStr(), getDiastoleStr(), getPulseStr());
//    }

    @Bindable
    public String getDateTimeStr() {
        return getDateStr() + "\n" + getTimeStr();
//        return PressureData.DATETIME_FORMATTER.format(pressureData.getDateTime());
    }

    @Bindable
    public String getDateStr() {
        return PressureData.makeDateStr(pressureData.getDateTime());
    }

    @Bindable
    public String getTimeStr() {
        return PressureData.makeTimeStr(pressureData.getDateTime());
    }

    @Bindable
    public String getSystoleStr() {
        return String.valueOf(pressureData.getSystole());
    }

    public void setSystoleStr(String systoleStr) {
        pressureData.setSystole(Integer.parseInt(systoleStr));
    }

    @Bindable
    public String getDiastoleStr() {
        return String.valueOf(pressureData.getDiastole());
    }

    public void setDiastoleStr(String diastoleStr) {
        pressureData.setDiastole(Integer.parseInt(diastoleStr));
    }

    @Bindable
    public String getPulseStr() {
        return String.valueOf(pressureData.getPulse());
    }

    public void setPulseStr(String pulseStr) {
        pressureData.setPulse(Integer.parseInt(pulseStr));
    }

    @Bindable
    public String getArrhythmiaStr() {
        // just for one-way binding
        // TODO better, maybe graphical representation for arrhythmia than "A" : "-"
        return pressureData.isArrhythmia() ? ARRHYTHMIA_STR : NO_ARRHYTHMIA_STR;
    }

    @Bindable
    public boolean getArrhythmia() { return pressureData.isArrhythmia(); }

    public void setArrhythmia (boolean isArrhythmia) {
        pressureData.setArrhythmia(isArrhythmia);
    }

    @Bindable
    public int getId() { return pressureData.getId(); }

    @Override
    public int compareTo(@NonNull PressureDataViewModel pressureDataViewModel) {
        return this.pressureData.compareTo(pressureDataViewModel.pressureData);
    }

    public void setPressureData(PressureData pressureData) {
        this.pressureData = pressureData;
    }

    public PressureData getPressureData() { return pressureData; }
}
