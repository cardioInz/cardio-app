package cardio_app.structures;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HealthRecord implements Comparable<HealthRecord> {

    private short systol;
    private short diastol;
    private char condition; // TODO enum class
    private short pulse;



    private boolean arrhythmia;
    private Date date;

    public HealthRecord(int systol, int diastol, char condition, int pulse, boolean arrhythmia, Date date) {
        this.systol = (short) systol;
        this.diastol = (short) diastol;
        this.condition = condition;
        this.pulse = (short) pulse;
        this.arrhythmia = arrhythmia;
        this.date = date;
    }

    public String getDateStr() {
        String d = new SimpleDateFormat("yyyy-MM-dd").format(date);
        String t = new SimpleDateFormat("HH:mm:ss").format(date);
        return d + '\n' + t;
    }

    public String getSystolStr() {
        return String.valueOf(systol);
    }

    public String getDiastolStr() {
        return String.valueOf(diastol);
    }

    public String getConditionStr() {
        return String.valueOf(condition);
    }

    public String getPulseStr() {
        return String.valueOf(pulse);
    }

    public String getArrhythmiaStr() {
        return arrhythmia ? "T" : "F";
    }




    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public short getSystol() {
        return systol;
    }

    public void setSystol(short systol) {
        this.systol = systol;
    }

    public short getDiastol() {
        return diastol;
    }

    public void setDiastol(short diastol) {
        this.diastol = diastol;
    }

    public char getCondition() {
        return condition;
    }

    public void setCondition(char condition) {
        this.condition = condition;
    }

    public short getPulse() {
        return pulse;
    }

    public void setPulse(short pulse) {
        this.pulse = pulse;
    }

    public boolean isArrhythmia() {
        return arrhythmia;
    }

    public void setArrhythmia(boolean arrhythmia) {
        this.arrhythmia = arrhythmia;
    }


    @Override
    public int compareTo(HealthRecord other) {
        return this.getDate().compareTo(other.getDate());
    }
}
