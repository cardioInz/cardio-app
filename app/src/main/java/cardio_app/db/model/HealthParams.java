package cardio_app.db.model;

import android.support.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;

public class HealthParams extends BaseModel {

    static int ID_CNT = 0;

    @DatabaseField
    private String systole;
    @DatabaseField
    private String diastole;
    @DatabaseField
    private String pulse;
    @DatabaseField
    private boolean arrhythmia;


    public HealthParams(String systole, String diastole, String pulse, boolean arrhythmia) {
        super(++ID_CNT);
        this.systole = systole;
        this.diastole = diastole;
        this.pulse = pulse;
        this.arrhythmia = arrhythmia;
    }

    public HealthParams(int systole, int diastole, int pulse, boolean arrhythmia) {
        super(++ID_CNT);
        this.systole = String.valueOf(systole);
        this.diastole = String.valueOf(diastole);
        this.pulse = String.valueOf(pulse);
        this.arrhythmia = arrhythmia;
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

    public int compareToHealthParams(@NonNull HealthParams that) {
        return Integer.valueOf(this.getId()).compareTo(that.getId());
    }
}
