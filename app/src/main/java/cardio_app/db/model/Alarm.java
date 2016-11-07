package cardio_app.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Objects;

@DatabaseTable
public class Alarm extends BaseModel implements Parcelable {

    @DatabaseField
    private int hour;

    @DatabaseField
    private int minute;

    @DatabaseField
    private String name;

    @DatabaseField
    private String description;

    public Alarm() {
    }

    public Alarm(int hour, int minute, String name, String description) {
        this.hour = hour;
        this.minute = minute;
        this.name = name;
        this.description = description;
    }

    public Alarm(int id, int hour, int minute, String name, String description) {
        super(id);
        this.hour = hour;
        this.minute = minute;
        this.name = name;
        this.description = description;
    }

    protected Alarm(Parcel in) {
        super(in.readInt());
        hour = in.readInt();
        minute = in.readInt();
        description = in.readString();
    }

    public static final Creator<Alarm> CREATOR = new Creator<Alarm>() {
        @Override
        public Alarm createFromParcel(Parcel in) {
            return new Alarm(in);
        }

        @Override
        public Alarm[] newArray(int size) {
            return new Alarm[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeInt(hour);
        parcel.writeInt(minute);
        parcel.writeString(description);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getId() + ", " + hour + ":" + minute + " " + name + ": " + description;
    }

    @Override
    public int hashCode() {
        return getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Alarm alarm = (Alarm) o;

        return getId() == alarm.getId();
    }
}
