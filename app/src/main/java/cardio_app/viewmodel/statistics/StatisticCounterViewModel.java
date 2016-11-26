package cardio_app.viewmodel.statistics;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import cardio_app.statistics.analyse.StatisticCounter;

import static cardio_app.statistics.analyse.StatisticCounter.TypeEnum.*;


public class StatisticCounterViewModel extends BaseObservable {
    private StatisticCounter statisticCounter;

    public StatisticCounterViewModel(){
        this(new StatisticCounter());
    }

    private StatisticCounterViewModel(StatisticCounter statisticCounter) {
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
        return String.valueOf(statisticCounter.getCnt(ARRHYTHMIA));
    }

    @Bindable
    public String getNoArrhythmiaCnt() {
        if (statisticCounter == null)
            return "-";
        return String.valueOf(statisticCounter.getCnt(NO_ARRHYTHMIA));
    }

    @Bindable
    public String getUnknownCnt() {
        if (statisticCounter == null)
            return "-";
        return String.valueOf(statisticCounter.getCnt(UNKNOWN));
    }

    @Bindable
    public String getWellCnt() {
        if (statisticCounter == null)
            return "-";
        return String.valueOf(statisticCounter.getCnt(WELL));
    }

    @Bindable
    public String getMiddleCnt() {
        if (statisticCounter == null)
            return "-";
        return String.valueOf(statisticCounter.getCnt(LITTLE_LOW_OR_HIGH));
    }

    @Bindable
    public String getBadDiffCnt() {
        if (statisticCounter == null)
            return "-";
        return String.valueOf(statisticCounter.getCnt(BAD_DIFF));
    }

    @Bindable
    public String getBadCnt() {
        if (statisticCounter == null)
            return "-";
        return String.valueOf(statisticCounter.getCnt(BAD));
    }

    @Bindable
    public String getTotalCnt() {
        if (statisticCounter == null)
            return "-";
        return String.valueOf(statisticCounter.getCnt(TOTAL));
    }
}
