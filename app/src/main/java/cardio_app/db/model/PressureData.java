package cardio_app.db.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;

import cardio_app.db.HealthCondition;
import cardio_app.viewmodel.date_time.DateTimeViewModel;
import cardio_app.viewmodel.PressureDataViewModel;

@DatabaseTable
public class PressureData extends BaseModel implements Parcelable, Comparable<PressureData> {


    @DatabaseField
    private int systole;
    @DatabaseField
    private int diastole;
    @DatabaseField
    private int pulse;
    @DatabaseField
    private boolean arrhythmia;
    @DatabaseField
    private Date dateTime;

    public PressureData() {
        this.systole = 0;
        this.diastole = 0;
        this.pulse = 0;
        this.arrhythmia = false;
        this.dateTime = new Date();
    }

    private void initParams(int systole, int diastole, int pulse, boolean arrhythmia, Date dateTime){
        this.systole = systole;
        this.diastole = diastole;
        this.pulse = pulse;
        this.arrhythmia = arrhythmia;
        this.dateTime = dateTime;
    }


    public PressureData(int systole, int diastole, int pulse, boolean arrhythmia, Date dateTime) {
        initParams(systole, diastole, pulse, arrhythmia, dateTime);
    }

    private PressureData(Parcel in) {
        setId(in.readInt());
        systole = in.readInt();
        diastole = in.readInt();
        pulse = in.readInt();
        arrhythmia = in.readString().equals(PressureDataViewModel.ARRHYTHMIA_STR);
        try {
            this.dateTime = DateTimeViewModel.DATETIME_FORMATTER.parse(in.readString());
        } catch (ParseException e) {
            e.printStackTrace();
            this.dateTime = new Date(0);
        }

    }

    public static final Creator<PressureData> CREATOR = new Creator<PressureData>() {
        @Override
        public PressureData createFromParcel(Parcel in) {
            return new PressureData(in);
        }

        @Override
        public PressureData[] newArray(int size) {
            return new PressureData[size];
        }
    };

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) { this.dateTime = dateTime; }

    public int getSystole() {
        return systole;
    }

    public void setSystole(int systole) { this.systole = systole; }

    public int getDiastole() {
        return diastole;
    }

    public void setDiastole(int diastole) { this.diastole = diastole; }

    public int getPulse() {
        return pulse;
    }

    public void setPulse(int pulse) { this.pulse = pulse; }

    public boolean isArrhythmia() {
        return arrhythmia;
    }

    public void setArrhythmia(boolean arrhythmia) { this.arrhythmia = arrhythmia; }

    @Override
    public int compareTo(@NonNull PressureData that) {
        return getComparator().compare(this, that);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeInt(systole);
        parcel.writeInt(diastole);
        parcel.writeInt(pulse);
        parcel.writeString(arrhythmia ? PressureDataViewModel.ARRHYTHMIA_STR : PressureDataViewModel.NO_ARRHYTHMIA_STR);
        parcel.writeString(DateTimeViewModel.DATETIME_FORMATTER.format(dateTime));
    }

    public HealthCondition getCondition() {
        return HealthCondition.classify(this);
    }


    public static Comparator<PressureData> getComparator() {
        return (a1, a2) -> {
            int dateCmp = a2.compareTo(a1);
            if (dateCmp != 0)
                return dateCmp;
            return a2.getId() - a1.getId();
        };
    }
}
