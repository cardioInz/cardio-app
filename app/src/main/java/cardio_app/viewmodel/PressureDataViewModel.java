package cardio_app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import cardio_app.db.model.PressureData;
import cardio_app.viewmodel.date_time.DateTimeViewModel;

/**
 * Created by kisam on 27.10.2016.
 */

public class PressureDataViewModel extends BaseObservable implements Comparable<PressureDataViewModel> {
    private PressureData pressureData;
    private DateTimeViewModel dateTimeViewModel;

    public static final String ARRHYTHMIA_STR = "A";
    public static final String NO_ARRHYTHMIA_STR = "-";

    public PressureDataViewModel() {
        this(new PressureData());
    }

    public PressureDataViewModel(PressureData pressureData) {
        this.pressureData = pressureData;
        if (this.pressureData != null)
            dateTimeViewModel = new DateTimeViewModel(this.pressureData.getDateTime());
    }

    private int tryToInt(String str) {
        if (str.isEmpty())
            return 0;

        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Bindable
    public String getConditionStr() {
        return pressureData.getCondition().getStrMapped();
    }

    @Bindable
    public DateTimeViewModel getDateTimeViewModel() {
        return this.dateTimeViewModel;
    }

    @Bindable
    public String getDateTimeStr() {
        return dateTimeViewModel.getDateStr() + "\n" + dateTimeViewModel.getTimeStr();
    }

    @Bindable
    public String getSystoleStr() {
        return String.valueOf(pressureData.getSystole());
    }

    public void setSystoleStr(String systoleStr) {
        pressureData.setSystole(tryToInt(systoleStr));
    }

    @Bindable
    public String getDiastoleStr() {
        return String.valueOf(pressureData.getDiastole());
    }

    public void setDiastoleStr(String diastoleStr) {

        pressureData.setDiastole(tryToInt(diastoleStr));
    }

    @Bindable
    public String getPulseStr() {
        return String.valueOf(pressureData.getPulse());
    }

    public void setPulseStr(String pulseStr) {
        pressureData.setPulse(tryToInt(pulseStr));
    }

    @Bindable
    public String getArrhythmiaStr() {
        // just for one-way binding
        // TODO better, maybe graphical representation for arrhythmia than "A" : "-"
        return pressureData.isArrhythmia() ? ARRHYTHMIA_STR : NO_ARRHYTHMIA_STR;
    }

    @Bindable
    public boolean getArrhythmia() {
        return pressureData.isArrhythmia();
    }

    public void setArrhythmia(boolean isArrhythmia) {
        pressureData.setArrhythmia(isArrhythmia);
    }

    @Bindable
    public int getId() {
        return pressureData.getId();
    }

    @Override
    public int compareTo(@NonNull PressureDataViewModel pressureDataViewModel) {
        return this.pressureData.compareTo(pressureDataViewModel.pressureData);
    }

    public void setPressureData(PressureData pressureData) {
        this.pressureData = pressureData;
    }

    public PressureData getPressureData() {
        return pressureData;
    }

    public String getStrValues() {
        return String.format("%s/%s/%s", getSystoleStr(), getDiastoleStr(), getPulseStr());
    }
}
