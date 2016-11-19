package cardio_app.statistics.analyse;

/**
 * Created by kisam on 15.11.2016.
 */

public class StatisticCounter {


    private int totalCnt;
    private int badCnt;
    private int badDiffCnt;
    private int middleCnt;
    private int wellCnt;
    private int arrhythmiaCnt;
    private int noArrhythmiaCnt;
    private int unknownCnt;

    public StatisticCounter(){
        zeroAll();
    }

    public void zeroAll(){
        totalCnt = 0;
        badCnt = 0;
        badDiffCnt = 0;
        middleCnt = 0;
        wellCnt = 0;
        arrhythmiaCnt = 0;
        noArrhythmiaCnt = 0;
        unknownCnt = 0;
    }



    public int getTotalCnt() {
        return totalCnt;
    }

    public void setTotalCnt(int totalCnt) {
        this.totalCnt = totalCnt;
    }

    public int getBadCnt() {
        return badCnt;
    }

    public void setBadCnt(int badCnt) {
        this.badCnt = badCnt;
    }

    public int getBadDiffCnt() {
        return badDiffCnt;
    }

    public void setBadDiffCnt(int badDiffCnt) {
        this.badDiffCnt = badDiffCnt;
    }

    public int getMiddleCnt() {
        return middleCnt;
    }

    public void setMiddleCnt(int middleCnt) {
        this.middleCnt = middleCnt;
    }

    public int getWellCnt() {
        return wellCnt;
    }

    public void setWellCnt(int wellCnt) {
        this.wellCnt = wellCnt;
    }

    public int getArrhythmiaCnt() {
        return arrhythmiaCnt;
    }

    public void setArrhythmiaCnt(int arrhythmiaCnt) {
        this.arrhythmiaCnt = arrhythmiaCnt;
    }

    public int getNoArrhythmiaCnt() {
        return noArrhythmiaCnt;
    }

    public void setNoArrhythmiaCnt(int noArrhythmiaCnt) {
        this.noArrhythmiaCnt = noArrhythmiaCnt;
    }

    public int getUnknownCnt() {
        return unknownCnt;
    }

    public void setUnknownCnt(int unknownCnt) {
        this.unknownCnt = unknownCnt;
    }



    public void incUnknownCnt() {
        this.unknownCnt++;
    }

    public void incTotalCnt() {
        this.totalCnt++;
    }

    public void incBadCnt() {
        this.badCnt++;
    }

    public void incBadDiffCnt() {
        this.badDiffCnt++;
    }

    public void incMiddleCnt() {
        this.middleCnt++;
    }

    public void incWellCnt() {
        this.wellCnt++;
    }

    public void incArrhythmiaCnt() {
        this.arrhythmiaCnt++;
    }

    public void incNoArrhythmiaCnt() {
        this.noArrhythmiaCnt++;
    }
}
