package cardio_app.viewmodel.statistics;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import cardio_app.filtering_and_statistics.statistics_model.StatisticMeasure;
import cardio_app.viewmodel.PressureDataViewModel;

/**
 * Created by kisam on 15.11.2016.
 */

public class StatisticMeasureViewModel extends BaseObservable {

    private StatisticMeasure statisticMeasure;
    private PressureDataViewModel pressureDataViewModel;

    public StatisticMeasureViewModel(StatisticMeasure statisticMeasure){
        setStatisticMeasure(statisticMeasure);
    }

    public void setStatisticMeasure(StatisticMeasure statisticMeasure) {
        this.statisticMeasure = statisticMeasure;
        if (pressureDataViewModel == null)
            pressureDataViewModel = new PressureDataViewModel(statisticMeasure.getPressureData());
        else
            this.pressureDataViewModel.setPressureData(statisticMeasure.getPressureData());
    }

    @Bindable
    public String getDateTimeStr() {
        return pressureDataViewModel.getDateTimeStr();
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
                && statisticMeasure != null
                && statisticMeasure.isArrhythmiaImportant()
                && statisticMeasure.getPressureData().isArrhythmia();
    }

    @Bindable
    public String getArrhythmiaStr() {
        return shouldShowArrhythmia() ? "A" : ""; // TODO string could not be initialize in code
    }

    @Bindable
    public String getTitle() {
        return statisticMeasure.getTitle();
    }
}
