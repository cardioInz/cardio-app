package cardio_app.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class AlarmDrug extends BaseModel implements Parcelable {

    @DatabaseField(foreign = true, uniqueCombo = true)
    private Alarm alarm;

    @DatabaseField(foreign = true, uniqueCombo = true)
    private Drug drug;

    public AlarmDrug() {
    }

    public AlarmDrug(Alarm alarm, Drug drug) {
        this.alarm = alarm;
        this.drug = drug;
    }

    public AlarmDrug(int id, Alarm alarm, Drug drug) {
        super(id);
        this.alarm = alarm;
        this.drug = drug;
    }

    protected AlarmDrug(Parcel in) {
        super(in.readInt());
        alarm = in.readParcelable(Alarm.class.getClassLoader());
        drug = in.readParcelable(Drug.class.getClassLoader());
    }

    public static final Creator<AlarmDrug> CREATOR = new Creator<AlarmDrug>() {
        @Override
        public AlarmDrug createFromParcel(Parcel in) {
            return new AlarmDrug(in);
        }

        @Override
        public AlarmDrug[] newArray(int size) {
            return new AlarmDrug[size];
        }
    };

    public Alarm getAlarm() {
        return alarm;
    }

    public void setAlarm(Alarm alarm) {
        this.alarm = alarm;
    }

    public Drug getDrug() {
        return drug;
    }

    public void setDrug(Drug drug) {
        this.drug = drug;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeParcelable(alarm, i);
        parcel.writeParcelable(drug, i);
    }
}
