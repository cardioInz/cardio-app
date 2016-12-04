package cardio_app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import cardio_app.db.model.PressureData;
import cardio_app.viewmodel.date_time.DateTimeViewModel;


public class PressureDataViewModel extends BaseObservable implements Comparable<PressureDataViewModel> {
    // TODO better, maybe graphical representation for arrhythmia than "A" : "-"
    public static final String ARRHYTHMIA_STR = "A";
    public static final String NO_ARRHYTHMIA_STR = "-";
    private PressureData pressureData;
    private DateTimeViewModel dateTimeViewModel = new DateTimeViewModel();

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
    public String getDateTimeInTwoLinesStr() {
        return dateTimeViewModel.getDateStr() + "\n" + dateTimeViewModel.getTimeStr();
    }

    @Bindable
    public String getDateTimeStr() {
        return dateTimeViewModel.getDateStr() + " " + dateTimeViewModel.getTimeStr();
    }

    @Bindable
    public String getSystoleStr() {
        try {
            return String.valueOf(pressureData.getSystole());
        } catch (Exception e) {
            return "-";
        }
    }

    public void setSystoleStr(String systoleStr) {
        pressureData.setSystole(tryToInt(systoleStr));
    }

    @Bindable
    public String getDiastoleStr() {
        try {
            return String.valueOf(pressureData.getDiastole());
        } catch (Exception e) {
            return "-";
        }
    }

    public void setDiastoleStr(String diastoleStr) {
        pressureData.setDiastole(tryToInt(diastoleStr));
    }

    @Bindable
    public String getPulseStr() {
        try {
            return String.valueOf(pressureData.getPulse());
        } catch (Exception e) {
            return "-";
        }
    }

    public void setPulseStr(String pulseStr) {
        pressureData.setPulse(tryToInt(pulseStr));
    }

    @Bindable
    public String getArrhythmiaStr() {
        // just for one-way binding
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

    public PressureData getPressureData() {
        return pressureData;
    }

    public void setPressureData(PressureData pressureData) {
        this.pressureData = pressureData;
    }

    @Bindable
    public String getStrValues() {
        return String.format("%s/%s/%s", getSystoleStr(), getDiastoleStr(), getPulseStr());
    }

    @Bindable
    public String getDateStr() {
        return dateTimeViewModel.getDateStr();
    }

    @Bindable
    public String getTimeStr() {
        return dateTimeViewModel.getTimeStr();
    }
}
