package cardio_app.viewmodel.statistics;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import cardio_app.statistics.analyse.StatisticLastMeasure;
import cardio_app.viewmodel.PressureDataViewModel;


public class StatisticLastMeasureViewModel extends BaseObservable {

    private StatisticLastMeasure statisticLastMeasure;
    private PressureDataViewModel pressureDataViewModel;


    public StatisticLastMeasureViewModel(){

    }

    public StatisticLastMeasureViewModel(StatisticLastMeasure statisticLastMeasure){
        setStatisticLastMeasure(statisticLastMeasure);
    }

    public void setStatisticLastMeasure(StatisticLastMeasure statisticLastMeasure) {
        this.statisticLastMeasure = statisticLastMeasure;
        if (pressureDataViewModel == null)
            pressureDataViewModel = new PressureDataViewModel(statisticLastMeasure.getPressureData());
        else
            this.pressureDataViewModel.setPressureData(statisticLastMeasure.getPressureData());
    }

    @Bindable
    public String getDateStr() {
        return pressureDataViewModel.getDateStr();
    }

    @Bindable
    public String getTimeStr() {
        return pressureDataViewModel.getTimeStr();
    }

    @Bindable
    public String getValuesStr() {
        return pressureDataViewModel.getStrValues();
    }

    @Bindable
    public int getArrhythmiaVisibility() {
        return shouldShowArrhythmia() ? View.VISIBLE : View.GONE;
    }

    public boolean shouldShowArrhythmia() {
        return !(pressureDataViewModel == null || pressureDataViewModel.getPressureData() == null)
                && statisticLastMeasure != null
                && statisticLastMeasure.isArrhythmiaImportant()
                && statisticLastMeasure.getPressureData().isArrhythmia();
    }

    @Bindable
    public String getArrhythmiaStr() {
        return shouldShowArrhythmia() ? "A" : ""; // TODO string could not be initialize in code
    }

    @Bindable
    public String getTitle() {
        return statisticLastMeasure.getTitle();
    }
}
