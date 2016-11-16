package cardio_app.viewmodel.statistics;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import cardio_app.filtering_and_statistics.statistics_model.StatisticCounter;

/**
 * Created by kisam on 15.11.2016.
 */

public class StatisticCounterViewModel extends BaseObservable {
    private StatisticCounter statisticCounter;

    public StatisticCounterViewModel(){
        this(new StatisticCounter());
    }

    public StatisticCounterViewModel(StatisticCounter statisticCounter) {
        this.statisticCounter = statisticCounter;
    }

    public StatisticCounter getStatisticCounter() {
        return statisticCounter;
    }

    public void setStatisticCounter(StatisticCounter statisticCounter) {
        this.statisticCounter = statisticCounter;
    }

    @Bindable
    public String getArrhythmiaCnt() {
        if (statisticCounter == null)
            return "-";
        return String.valueOf(statisticCounter.getArrhythmiaCnt());
    }

    @Bindable
    public String getNoArrhythmiaCnt() {
        if (statisticCounter == null)
            return "-";
        return String.valueOf(statisticCounter.getNoArrhythmiaCnt());
    }

    @Bindable
    public String getUnknownCnt() {
        if (statisticCounter == null)
            return "-";
        return String.valueOf(statisticCounter.getUnknownCnt());
    }

    @Bindable
    public String getWellCnt() {
        if (statisticCounter == null)
            return "-";
        return String.valueOf(statisticCounter.getWellCnt());
    }

    @Bindable
    public String getMiddleCnt() {
        if (statisticCounter == null)
            return "-";
        return String.valueOf(statisticCounter.getMiddleCnt());
    }

    @Bindable
    public String getBadDiffCnt() {
        if (statisticCounter == null)
            return "-";
        return String.valueOf(statisticCounter.getBadDiffCnt());
    }

    @Bindable
    public String getBadCnt() {
        if (statisticCounter == null)
            return "-";
        return String.valueOf(statisticCounter.getBadCnt());
    }

    @Bindable
    public String getTotalCnt() {
        if (statisticCounter == null)
            return "-";
        return String.valueOf(statisticCounter.getTotalCnt());
    }
}
