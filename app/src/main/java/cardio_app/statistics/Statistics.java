package cardio_app.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cardio_app.db.model.PressureData;
import cardio_app.statistics.analyse.HealthCondition;
import cardio_app.statistics.analyse.StatisticCounter;
import cardio_app.statistics.analyse.StatisticLastMeasure;

import static cardio_app.statistics.analyse.StatisticLastMeasure.TypeEnum.LAST;
import static cardio_app.statistics.analyse.StatisticLastMeasure.TypeEnum.LAST_ARRHYTHMIA;
import static cardio_app.statistics.analyse.StatisticLastMeasure.TypeEnum.LAST_BAD;
import static cardio_app.statistics.analyse.StatisticLastMeasure.TypeEnum.LAST_BAD_DIFF;
import static cardio_app.statistics.analyse.StatisticLastMeasure.TypeEnum.LAST_MIDDLE;
import static cardio_app.statistics.analyse.StatisticLastMeasure.TypeEnum.LAST_NO_ARRHYTHMIA;
import static cardio_app.statistics.analyse.StatisticLastMeasure.TypeEnum.LAST_WELL;

public class Statistics {

    private static final String TAG = Statistics.class.toString();
    private static boolean isMale = true;
    private StatisticCounter statisticCounter;
    private HashMap<StatisticLastMeasure.TypeEnum, StatisticLastMeasure> statisticMeasuresMap = new HashMap<>();
    private boolean doForCounter;
    private boolean doForMeasures;


    public Statistics(boolean doForCounter, boolean doForMeasures) {
        this(new ArrayList<>(), doForCounter, doForMeasures);
    }

    private Statistics(List<PressureData> pressureDataList, boolean doForCounter, boolean doForMeasures) {

        this.doForCounter = doForCounter;
        this.doForMeasures = doForMeasures;

        if (this.doForCounter) {
            statisticCounter = new StatisticCounter();
        }

        if (this.doForMeasures) {
            statisticMeasuresMap.put(LAST, new StatisticLastMeasure(true));
            statisticMeasuresMap.put(LAST_BAD, new StatisticLastMeasure(true));
            statisticMeasuresMap.put(LAST_MIDDLE, new StatisticLastMeasure(true));
            statisticMeasuresMap.put(LAST_WELL, new StatisticLastMeasure(true));
            statisticMeasuresMap.put(LAST_BAD_DIFF, new StatisticLastMeasure(true));
            statisticMeasuresMap.put(LAST_ARRHYTHMIA, new StatisticLastMeasure(false));
            statisticMeasuresMap.put(LAST_NO_ARRHYTHMIA, new StatisticLastMeasure(false));
        }

        assignValues(pressureDataList);
    }

    private static StatisticLastMeasure.TypeEnum mapConditionTo_StatisticMeasureTypeEnum(HealthCondition hc) {
        HealthCondition.Simplified condition = HealthCondition.mapToSimplifiedCondition(hc);
        if (condition == null)
            return null;

        switch (condition) {

            case WELL:
                return StatisticLastMeasure.TypeEnum.LAST_WELL;
            case BAD:
                return StatisticLastMeasure.TypeEnum.LAST_BAD;
            case MIDDLE:
                return StatisticLastMeasure.TypeEnum.LAST_MIDDLE;
            case BAD_DIFF:
                return StatisticLastMeasure.TypeEnum.LAST_BAD_DIFF;
            default:
                return null;
        }
    }

    private static StatisticCounter.TypeEnum mapConditionTo_CounteredMeasureEnum(HealthCondition hc) {
        HealthCondition.Simplified condition = HealthCondition.mapToSimplifiedCondition(hc);
        if (condition == null)
            return null;

        switch (condition) {
            case WELL:
                return StatisticCounter.TypeEnum.WELL;
            case BAD:
                return StatisticCounter.TypeEnum.BAD;
            case MIDDLE:
                return StatisticCounter.TypeEnum.LITTLE_LOW_OR_HIGH;
            case BAD_DIFF:
                return StatisticCounter.TypeEnum.BAD_DIFF;
            case UNKNOWN:
                return StatisticCounter.TypeEnum.UNKNOWN;
            default:
                return null;
        }
    }

    public static boolean isMale() {
        return isMale;
    }

    public static void setIsMale(boolean isMale) {
        Statistics.isMale = isMale;
    }

    public StatisticCounter getStatisticCounter() {
        return statisticCounter;
    }

    public HashMap<StatisticLastMeasure.TypeEnum, StatisticLastMeasure> getStatisticMeasuresMap() {
        return statisticMeasuresMap;
    }

    private void prepareForCounter(List<PressureData> list) {
        statisticCounter.zeroAll();
        statisticCounter.setCntOfKey(StatisticCounter.TypeEnum.TOTAL, list.size());

        HealthCondition hc; // classified condition of pressure data
        StatisticCounter.TypeEnum typeC; // condition
        StatisticCounter.TypeEnum typeA; // arrhythmia

        for (PressureData pressureData : list) {
            hc = HealthCondition.classify(pressureData);
            if (hc == null)
                continue;

            typeC = mapConditionTo_CounteredMeasureEnum(hc);
            statisticCounter.incValueOfKey(typeC);

            typeA = pressureData.isArrhythmia() ?
                    StatisticCounter.TypeEnum.ARRHYTHMIA :
                    StatisticCounter.TypeEnum.NO_ARRHYTHMIA;
            statisticCounter.incValueOfKey(typeA);
        }
    }

    private void prepareForLastMeasures(List<PressureData> list) {

        HashMap<StatisticLastMeasure.TypeEnum, PressureData> lastMap = new HashMap<StatisticLastMeasure.TypeEnum, PressureData>() {{
            for (StatisticLastMeasure.TypeEnum typeEnum : statisticMeasuresMap.keySet()) {
                put(typeEnum, null);
            }
        }};

        lastMap.put(LAST, list.isEmpty() ? null : list.get(0));

        StatisticLastMeasure.TypeEnum typeC;
        StatisticLastMeasure.TypeEnum typeA;
        HealthCondition hc;
        boolean shouldFinish;

        for (PressureData pressureData : list) {
            hc = HealthCondition.classify(pressureData);

            typeC = mapConditionTo_StatisticMeasureTypeEnum(hc);

            if (lastMap.containsKey(typeC) && lastMap.get(typeC) == null) {
                lastMap.put(typeC, pressureData);
            }


            typeA = pressureData.isArrhythmia() ?
                    StatisticLastMeasure.TypeEnum.LAST_ARRHYTHMIA :
                    StatisticLastMeasure.TypeEnum.LAST_NO_ARRHYTHMIA;
            if (lastMap.containsKey(typeA) && lastMap.get(typeA) == null) {
                lastMap.put(typeA, pressureData);
            }

            shouldFinish = true;
            for (StatisticLastMeasure.TypeEnum typeEnum : lastMap.keySet()) {
                if (lastMap.get(typeEnum) == null) {
                    shouldFinish = false;
                    break;
                }
            }

            if (shouldFinish)
                break;
        }

        for (StatisticLastMeasure.TypeEnum key : statisticMeasuresMap.keySet()) {
            if (!statisticMeasuresMap.containsKey(key) || !lastMap.containsKey(key))
                continue;
            statisticMeasuresMap.get(key).setPressureData(lastMap.get(key));
        }
    }

    public void assignValues(List<PressureData> list) {
        if (doForCounter)
            prepareForCounter(list);
        if (doForMeasures)
            prepareForLastMeasures(list);
    }
}
