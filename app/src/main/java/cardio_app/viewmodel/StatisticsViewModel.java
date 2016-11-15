package cardio_app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.ArrayList;
import java.util.List;

import cardio_app.R;
import cardio_app.db.Statistics;
import cardio_app.db.model.PressureData;
import cardio_app.viewmodel.date_time.DateTimeViewModel;

/**
 * Created by kisam on 14.11.2016.
 */

public class StatisticsViewModel extends BaseObservable {

    private Statistics statistics;
    private PressureDataViewModel lastMeasureViewModel;
    private PressureDataViewModel lastBadConditionMeasureViewModel;
    private PressureDataViewModel lastMiddleConditionMeasureViewModel;
    private PressureDataViewModel lastWellConditionMeasureViewModel;
    private PressureDataViewModel lastBadDiffConditionMeasureViewModel;
    private PressureDataViewModel lastArrhythmiaMeasureViewModel;
    private PressureDataViewModel lastNoArrhythmiaMeasureViewModel;

    private DateTimeViewModel dateFrom;
    private DateTimeViewModel dateTo;

    public StatisticsViewModel() {
        statistics = new Statistics(new ArrayList<>());
    }

    public StatisticsViewModel(Statistics statistics) {
        setStatistics(statistics);
    }

    public Statistics getStatistics() {
        return statistics;
    }

    private void initFields() {
        if (statistics == null)
            return;

        lastMeasureViewModel = new PressureDataViewModel(statistics.getLastMeasure());
        lastBadConditionMeasureViewModel = new PressureDataViewModel(statistics.getLastBadConditionMeasure());
        lastMiddleConditionMeasureViewModel = new PressureDataViewModel(statistics.getLastMiddleConditionMeasure());
        lastWellConditionMeasureViewModel = new PressureDataViewModel(statistics.getLastWellConditionMeasure());
        lastBadDiffConditionMeasureViewModel = new PressureDataViewModel(statistics.getLastBadDiffConditionMeasure());
        lastArrhythmiaMeasureViewModel = new PressureDataViewModel(statistics.getLastArrhythmiaMeasure());
        lastNoArrhythmiaMeasureViewModel = new PressureDataViewModel(statistics.getLastNoArrhythmiaMeasure());
    }

    public void setDataListToStatistics(List<PressureData> list) {
        if (statistics == null)
            statistics = new Statistics(list);
        else
            this.statistics.assignValues(list);
        initFields();
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
        if (statistics == null)
            return;
        initFields();
    }


    private static String getStrOfMeasureToStatistics(PressureDataViewModel pressureDataViewModel){
        if (pressureDataViewModel == null || pressureDataViewModel.getPressureData() == null)
            return "-";

        if (pressureDataViewModel.getPressureData().isArrhythmia()){
            return String.format("%s %s\n%s\n%s %s",
                    R.string.statistics_title_values, pressureDataViewModel.getStrValues(),
                    R.string.statistics_title_arrhythmia,
                    R.string.statistics_title_date, pressureDataViewModel.getDateTimeStr());
        } else {
            return String.format("%s %s\n%s %s",
                    R.string.statistics_title_values, pressureDataViewModel.getStrValues(),
                    R.string.statistics_title_date, pressureDataViewModel.getDateTimeStr());
        }
    }

    @Bindable
    public String getLastMeasureStr() {
        return getStrOfMeasureToStatistics(lastMeasureViewModel);
    }

    @Bindable
    public String getLastBadConditionMeasureStr() {
        return getStrOfMeasureToStatistics(lastBadConditionMeasureViewModel);
    }

    @Bindable
    public String getLastMiddleConditionMeasureStr() {
        return getStrOfMeasureToStatistics(lastMiddleConditionMeasureViewModel);
    }

    @Bindable
    public String getLastWellConditionMeasureStr() {
        return getStrOfMeasureToStatistics(lastWellConditionMeasureViewModel);
    }

    @Bindable
    public String getLastBadDiffConditionMeasureStr() {
        return getStrOfMeasureToStatistics(lastBadDiffConditionMeasureViewModel);
    }

    @Bindable
    public String getLastArrhythmiaMeasureStr() {
        return getStrOfMeasureToStatistics(lastArrhythmiaMeasureViewModel);
    }

    @Bindable
    public String getLastNoArrhythmiaMeasureStr() {
        return getStrOfMeasureToStatistics(lastNoArrhythmiaMeasureViewModel);
    }


//
//
//    @Bindable
//    public String getCounterOfMeasurementsInfoStr() {
//        return "TODO"; // TODO
//    }

    @Bindable
    public String getArrhythmiaCnt() {
        if (statistics == null)
            return "-";
        return String.valueOf(statistics.getArrhythmiaCnt());
    }

    @Bindable
    public String getNoArrhythmiaCnt() {
        if (statistics == null)
            return "-";
        return String.valueOf(statistics.getNoArrhythmiaCnt());
    }

    @Bindable
    public String getUnknownCnt() {
        if (statistics == null)
            return "-";
        return String.valueOf(statistics.getUnknownCnt());
    }

    @Bindable
    public String getWellCnt() {
        if (statistics == null)
            return "-";
        return String.valueOf(statistics.getWellCnt());
    }

    @Bindable
    public String getMiddleCnt() {
        if (statistics == null)
            return "-";
        return String.valueOf(statistics.getMiddleCnt());
    }

    @Bindable
    public String getBadDiffCnt() {
        if (statistics == null)
            return "-";
        return String.valueOf(statistics.getBadDiffCnt());
    }

    @Bindable
    public String getBadCnt() {
        if (statistics == null)
            return "-";
        return String.valueOf(statistics.getBadCnt());
    }

    @Bindable
    public String getTotalCnt() {
        if (statistics == null)
            return "-";
        return String.valueOf(statistics.getTotalCnt());
    }
}
