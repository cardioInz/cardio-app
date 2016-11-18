package cardio_app.filtering;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cardio_app.db.model.PressureData;
import cardio_app.statistics.HealthCondition;
import cardio_app.statistics.StatisticCounter;
import cardio_app.statistics.StatisticMeasure;
import cardio_app.statistics.StatisticMeasureTypeEnum;

import static cardio_app.statistics.StatisticMeasureTypeEnum.*;

/**
 * Created by kisam on 14.11.2016.
 */

public class Statistics {

    private static final String TAG = Statistics.class.toString();
    private StatisticCounter statisticCounter;
    private HashMap<StatisticMeasureTypeEnum, StatisticMeasure> statisticMeasuresMap = new HashMap<>();
    private boolean doForCounter;
    private boolean doForMeasures;


    public Statistics(boolean doForCounter, boolean doForMeasures) {
        this(new ArrayList<>(), doForCounter, doForMeasures);
    }

    public Statistics(List<PressureData> pressureDataList, boolean doForCounter, boolean doForMeasures) {

        this.doForCounter = doForCounter;
        this.doForMeasures = doForMeasures;

        if (this.doForCounter) {
            statisticCounter = new StatisticCounter();
        }

        if (this.doForMeasures) {
            statisticMeasuresMap.put(LAST, new StatisticMeasure(true));
            statisticMeasuresMap.put(LAST_BAD, new StatisticMeasure(true));
            statisticMeasuresMap.put(LAST_MIDDLE, new StatisticMeasure(true));
            statisticMeasuresMap.put(LAST_WELL, new StatisticMeasure(true));
            statisticMeasuresMap.put(LAST_BAD_DIFF, new StatisticMeasure(true));
            statisticMeasuresMap.put(LAST_ARRHYTHMIA, new StatisticMeasure(false));
            statisticMeasuresMap.put(LAST_NO_ARRHYTHMIA, new StatisticMeasure(false));
        }

        assignValues(pressureDataList);
    }


    public StatisticCounter getStatisticCounter() {
        return statisticCounter;
    }

    public HashMap<StatisticMeasureTypeEnum, StatisticMeasure> getStatisticMeasuresMap() {
        return statisticMeasuresMap;
    }

    private void prepareForCounter(List<PressureData> list) {
        statisticCounter.zeroAll();
        statisticCounter.setTotalCnt(list.size());

        for (int i = 0; i < list.size(); i++) {
            PressureData currentPressureData = list.get(i);
            HealthCondition hc = HealthCondition.classify(currentPressureData);

            if (hc.isSimplifiedBadDiff()) {
                statisticCounter.incBadDiffCnt();
            } else if (hc.isSimplifiedBad()) {
                statisticCounter.incBadCnt();
            } else if (hc.isSimplifiedWell()) {
                statisticCounter.incWellCnt();
            } else if (hc.isSimplifiedMiddle()) {
                statisticCounter.incMiddleCnt();
            } else if (hc.isSimplifiedUnknown()) {
                statisticCounter.incUnknownCnt();
            }

            if (currentPressureData.isArrhythmia()) {
                statisticCounter.incArrhythmiaCnt();
            } else {
                statisticCounter.incNoArrhythmiaCnt();
            }
        }
    }

    private void prepareForLastMeasures(List<PressureData> list) {
        PressureData lastMeasure = list.isEmpty() ? null : list.get(0);

        PressureData lastBadConditionMeasure = null;
        PressureData lastMiddleConditionMeasure = null;
        PressureData lastWellConditionMeasure = null;
        PressureData lastBadDiffConditionMeasure = null;
        PressureData lastArrhythmiaMeasure = null;
        PressureData lastNoArrhythmiaMeasure = null;

        for (int i = 0; i < list.size(); i++) {
            PressureData currentPressureData = list.get(i);
            HealthCondition hc = HealthCondition.classify(currentPressureData);

            if (hc.isSimplifiedBadDiff() && lastBadDiffConditionMeasure == null) {
                lastBadDiffConditionMeasure = currentPressureData;
            } else if (hc.isSimplifiedBad() && lastBadConditionMeasure == null) {
                lastBadConditionMeasure = currentPressureData;
            } else if (hc.isSimplifiedWell() && lastWellConditionMeasure == null) {
                lastWellConditionMeasure = currentPressureData;
            } else if (hc.isSimplifiedMiddle() && lastMiddleConditionMeasure == null) {
                lastMiddleConditionMeasure = currentPressureData;
            }

            if (currentPressureData.isArrhythmia()) {
                if (lastArrhythmiaMeasure == null) {
                    lastArrhythmiaMeasure = currentPressureData;
                }
            } else {
                if (lastNoArrhythmiaMeasure == null) {
                    lastNoArrhythmiaMeasure = currentPressureData;
                }
            }

            if (lastBadConditionMeasure != null
                    && lastMiddleConditionMeasure != null
                    && lastWellConditionMeasure != null
                    && lastBadDiffConditionMeasure != null
                    && lastArrhythmiaMeasure != null
                    && lastNoArrhythmiaMeasure != null){
                break;
            }
        }

        statisticMeasuresMap.get(LAST).setPressureData(lastMeasure);

        statisticMeasuresMap.get(LAST_BAD).setPressureData(lastBadConditionMeasure);
        statisticMeasuresMap.get(LAST_MIDDLE).setPressureData(lastMiddleConditionMeasure);
        statisticMeasuresMap.get(LAST_WELL).setPressureData(lastWellConditionMeasure);
        statisticMeasuresMap.get(LAST_BAD_DIFF).setPressureData(lastBadDiffConditionMeasure);
        statisticMeasuresMap.get(LAST_ARRHYTHMIA).setPressureData(lastArrhythmiaMeasure);
        statisticMeasuresMap.get(LAST_NO_ARRHYTHMIA).setPressureData(lastNoArrhythmiaMeasure);
    }

    public void assignValues(List<PressureData> list) {
        if (doForCounter)
            prepareForCounter(list);
        if (doForMeasures)
            prepareForLastMeasures(list);
    }
}
