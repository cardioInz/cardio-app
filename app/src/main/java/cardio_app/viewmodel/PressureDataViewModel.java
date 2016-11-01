package cardio_app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import cardio_app.db.model.PressureData;

/**
 * Created by kisam on 27.10.2016.
 */

public class PressureDataViewModel extends BaseObservable implements Comparable<PressureDataViewModel> {
    private PressureData pressureData;
    public static final String ARRHYTHMIA_STR = "A";
    public static final String NO_ARRHYTHMIA_STR = "-";

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
//
//    @Bindable
//    public String getDateTimeStr() {
//        return String.format("%s %s", getDateStr(), getTimeStr());
//    }

    @Bindable
    public String getDateStr() {
        return PressureData.makeDateStr(pressureData.getDateTime());
    }

    @Bindable
    public String getTimeStr() {
        return PressureData.makeTimeStr(pressureData.getDateTime());
    }

    private static String fillStrWithSpaces(int length, String str) {
        return String.format("%" + length + "s", str);
    }

    @Bindable
    public String getSystoleStr() {
        return fillStrWithSpaces(3, String.valueOf(pressureData.getSystole()));
    }

    @Bindable
    public String getDiastoleStr() {
        return fillStrWithSpaces(3, String.valueOf(pressureData.getDiastole()));
    }

    @Bindable
    public String getPulseStr() {
        return fillStrWithSpaces(3, String.valueOf(pressureData.getPulse()));
    }

    @Bindable
    public String getArrhythmiaStr() {
        return pressureData.isArrhythmia() ? ARRHYTHMIA_STR : NO_ARRHYTHMIA_STR;
    }

    @Bindable
    public int getId() { return pressureData.getId(); }

    @Override
    public int compareTo(@NonNull PressureDataViewModel pressureDataViewModel) {
        return this.pressureData.compareTo(pressureDataViewModel.pressureData);
    }
}
