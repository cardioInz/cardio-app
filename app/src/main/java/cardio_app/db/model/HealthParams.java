package cardio_app.db.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HealthParams extends BaseModel implements Parcelable, Comparable<HealthParams> {

    private static int ID_CNT = 0;

    @DatabaseField
    private String systole;
    @DatabaseField
    private String diastole;
    @DatabaseField
    private String pulse;
    @DatabaseField
    private boolean arrhythmia;
    @DatabaseField
    private String timeStr;
    @DatabaseField
    private String dateStr;


    public HealthParams(String systole, String diastole, String pulse, boolean arrhythmia, String dateStr, String timeStr) {
        super(++ID_CNT);
        this.systole = systole;
        this.diastole = diastole;
        this.pulse = pulse;
        this.arrhythmia = arrhythmia;
        this.dateStr = dateStr;
        this.timeStr = timeStr;
    }

    public HealthParams(int systole, int diastole, int pulse, boolean arrhythmia, Date date) {
        super(++ID_CNT);
        this.systole = String.valueOf(systole);
        this.diastole = String.valueOf(diastole);
        this.pulse = String.valueOf(pulse);
        this.arrhythmia = arrhythmia;
        this.dateStr = makeDateStr(date);
        this.timeStr = makeTimeStr(date);
    }


    protected HealthParams(Parcel in) {
        systole = in.readString();
        diastole = in.readString();
        pulse = in.readString();
        arrhythmia = in.readByte() != 0;
        timeStr = in.readString();
        dateStr = in.readString();
    }

    public static final Creator<HealthParams> CREATOR = new Creator<HealthParams>() {
        @Override
        public HealthParams createFromParcel(Parcel in) {
            return new HealthParams(in);
        }

        @Override
        public HealthParams[] newArray(int size) {
            return new HealthParams[size];
        }
    };

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

    private static String makeDateStr(Date date) {
        final String DATE_PATTERN = "yyyy-MM-dd";
        return new SimpleDateFormat(DATE_PATTERN).format(date);
    }

    private static String makeTimeStr(Date date) {
        final String TIME_PATTERN = "HH:mm";
        return new SimpleDateFormat(TIME_PATTERN).format(date);
    }

    public String getDateStr() {
        return dateStr;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public int compareDateAndTime(HealthParams that) {
        // do not implement this method as comparable because of unique ID field that occurs in BaseModel
        int result = this.dateStr.compareTo(that.getDateStr());
        if (result == 0) {
            result = this.timeStr.compareTo(that.getTimeStr());
        }
        return result;
    }

    @Override
    public int compareTo(@NonNull HealthParams that) {
        int result = this.compareDateAndTime(that);
        if (result == 0)
            return Integer.valueOf(this.getId()).compareTo(that.getId());
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeString(systole);
        parcel.writeString(diastole);
        parcel.writeString(pulse);
        parcel.writeString(arrhythmia ? "A" : "-");
        parcel.writeString(dateStr);
        parcel.writeString(timeStr);
    }
}
