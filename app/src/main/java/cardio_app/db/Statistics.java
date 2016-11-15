package cardio_app.db;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cardio_app.db.model.PressureData;
import cardio_app.filtering_and_statistics.HealthCondition;

/**
 * Created by kisam on 14.11.2016.
 */

public class Statistics {

    private static final String TAG = Statistics.class.toString();
    private Date dateFrom;
    private Date dateTo;

    private PressureData lastMeasure;
    private PressureData lastBadConditionMeasure;
    private PressureData lastMiddleConditionMeasure;
    private PressureData lastWellConditionMeasure;
    private PressureData lastBadDiffConditionMeasure;
    private PressureData lastArrhythmiaMeasure;
    private PressureData lastNoArrhythmiaMeasure;

    private int totalCnt;
    private int badCnt;
    private int badDiffCnt;
    private int middleCnt;
    private int wellCnt;
    private int arrhythmiaCnt;
    private int noArrhythmiaCnt;
    private int unknownCnt;

    public int getArrhythmiaCnt() {
        return this.arrhythmiaCnt;
    }

    public int getNoArrhythmiaCnt() {
        return this.noArrhythmiaCnt;
    }

    public int getUnknownCnt() {
        return this.unknownCnt;
    }

    public int getWellCnt() {
        return this.wellCnt;
    }

    public int getMiddleCnt() {
        return this.middleCnt;
    }

    public int getBadDiffCnt() {
        return this.badDiffCnt;
    }

    public int getBadCnt() {
        return this.badCnt;
    }

    public int getTotalCnt() {
        return this.totalCnt;
    }

    public Statistics() {
        this(new ArrayList<>());
    }

    public Statistics(List<PressureData> pressureDataList) {
        assignValues(pressureDataList);
    }

    public void assignValues(List<PressureData> list) {
        lastMeasure = list.isEmpty() ? null : list.get(0);

        lastBadConditionMeasure = null;
        lastMiddleConditionMeasure = null;
        lastWellConditionMeasure = null;
        lastBadDiffConditionMeasure = null;
        lastArrhythmiaMeasure = null;
        lastNoArrhythmiaMeasure = null;

        totalCnt = list.size();
        badCnt = 0;
        badDiffCnt = 0;
        wellCnt = 0;
        middleCnt = 0;
        arrhythmiaCnt = 0;
        noArrhythmiaCnt = 0;
        unknownCnt = 0;

        for (int i = 0; i < totalCnt; i++) {
            PressureData currentPressureData = list.get(i);
            HealthCondition hc = HealthCondition.classify(currentPressureData);

            if (hc.isSimplifiedBadDiff()) {
                if (lastBadDiffConditionMeasure == null) {
                    lastBadDiffConditionMeasure = currentPressureData;
                }
                badDiffCnt++;
            } else if (hc.isSimplifiedBad()) {
                if (lastBadConditionMeasure == null) {
                    lastBadConditionMeasure = currentPressureData;
                }
                badCnt++;
            } else if (hc.isSimplifiedWell()) {
                if (lastWellConditionMeasure == null) {
                    lastWellConditionMeasure = currentPressureData;
                }
                wellCnt++;
            } else if (hc.isSimplifiedMiddle()) {
                if (lastMiddleConditionMeasure == null) {
                    lastMiddleConditionMeasure = currentPressureData;
                }
                middleCnt++;
            } else if (hc.isSimplifiedUnknown()) {
                unknownCnt++;
            }

            if (currentPressureData.isArrhythmia()) {
                if (lastArrhythmiaMeasure == null) {
                    lastArrhythmiaMeasure = currentPressureData;
                }
                arrhythmiaCnt++;
            } else {
                if (lastNoArrhythmiaMeasure == null) {
                    lastNoArrhythmiaMeasure = currentPressureData;
                }
                noArrhythmiaCnt++;
            }
        }
    }

    public PressureData getLastBadDiffConditionMeasure() {
        return lastBadDiffConditionMeasure;
    }

    public void setLastBadDiffConditionMeasure(PressureData lastBadDiffConditionMeasure) {
        this.lastBadDiffConditionMeasure = lastBadDiffConditionMeasure;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public PressureData getLastMeasure() {
        return lastMeasure;
    }

    public void setLastMeasure(PressureData lastMeasure) {
        this.lastMeasure = lastMeasure;
    }

    public PressureData getLastBadConditionMeasure() {
        return lastBadConditionMeasure;
    }

    public void setLastBadConditionMeasure(PressureData lastBadConditionMeasure) {
        this.lastBadConditionMeasure = lastBadConditionMeasure;
    }

    public PressureData getLastMiddleConditionMeasure() {
        return lastMiddleConditionMeasure;
    }

    public void setLastMiddleConditionMeasure(PressureData lastMiddleConditionMeasure) {
        this.lastMiddleConditionMeasure = lastMiddleConditionMeasure;
    }

    public PressureData getLastWellConditionMeasure() {
        return lastWellConditionMeasure;
    }

    public void setLastWellConditionMeasure(PressureData lastWellConditionMeasure) {
        this.lastWellConditionMeasure = lastWellConditionMeasure;
    }

    public PressureData getLastArrhythmiaMeasure() {
        return lastArrhythmiaMeasure;
    }

    public void setLastArrhythmiaMeasure(PressureData lastArrhythmiaMeasure) {
        this.lastArrhythmiaMeasure = lastArrhythmiaMeasure;
    }

    public PressureData getLastNoArrhythmiaMeasure() {
        return lastNoArrhythmiaMeasure;
    }

    public void setLastNoArrhytmiaMeasure(PressureData lastNoArrhythmiaMeasure) {
        this.lastNoArrhythmiaMeasure = lastNoArrhythmiaMeasure;
    }

}
