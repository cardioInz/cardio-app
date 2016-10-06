package cardio_app.structures.table_row;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HealthParams {

    private String systole;
    private String diastole;
    private HealthCondition condition;
    private String pulse;
    private boolean arrhythmia;
    private String timeStr;
    private String dateStr;

    public HealthParams(int systole, int diastole, int pulse, boolean arrhythmia, Date date) {
        this.systole = String.valueOf(systole);
        this.diastole = String.valueOf(diastole);
        this.condition = HealthCondition.classify(this);
        this.pulse = String.valueOf(pulse);
        this.arrhythmia = arrhythmia;
        this.dateStr = makeDateStr(date);
        this.timeStr = makeTimeStr(date);
    }

    private static String makeDateStr(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    private static String makeTimeStr(Date date) {
        return new SimpleDateFormat("HH:mm").format(date);
    }

    public String getDateTimeStr() {
        return getDateStr() + "\n" + getTimeStr();
    }

    public String getDateStr() {
        return dateStr;
    }

    public String getSystole() {
        return systole;
    }

    public int getSystoleInt() {
        return Integer.parseInt(systole);
    }

    public String getDiastole() {
        return diastole;
    }

    public int getDiastoleInt() {
        return Integer.parseInt(diastole);
    }

    public HealthCondition getCondition() {
        return condition;
    }

    public String getConditionStr() {
        return condition.getStrMapped();
    }

    public String getPulse() {
        return pulse;
    }

    public int getPulseInt() {
        return Integer.parseInt(pulse);
    }

    public boolean isArrhythmia() {
        return arrhythmia;
    }

    public String getArrhythmiaStr() {
        return arrhythmia ? "A" : "-";
    }

    public String getTimeStr() {
        return timeStr;
    }
}
