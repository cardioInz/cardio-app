package cardio_app.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import cardio_app.viewmodel.date_time.DateTimeViewModel;

@DatabaseTable(tableName = "event")
public class Event extends BaseModel implements Parcelable {
    @DatabaseField
    private Date startDate;
    @DatabaseField
    private Date endDate;
    @DatabaseField
    private boolean isRepeatable;
    @DatabaseField
    private TimeUnit timeUnit;
    @DatabaseField
    private int timeDelta;
    @DatabaseField
    private String description;
    @DatabaseField(foreign = true)
    private OtherSymptompsRecord otherSymptompsRecord;
    @DatabaseField
    private Emotion emotion;
    @DatabaseField(foreign = true)
    private DoctorsAppointment doctorsAppointment;

    public Event() {

    }

    public Event(Date startDate,
                 Date endDate,
                 boolean isRepeatable,
                 int timeDelta,
                 String description,
                 TimeUnit timeUnit,
                 Emotion emotion,
                 OtherSymptompsRecord otherSymptompsRecord,
                 DoctorsAppointment doctorsAppointment) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.isRepeatable = isRepeatable;
        this.timeUnit = timeUnit;
        this.timeDelta = timeDelta;
        this.description = description;
        this.otherSymptompsRecord = otherSymptompsRecord;
        this.emotion = emotion;
        this.doctorsAppointment = doctorsAppointment;
    }

    public Event(int id,
                 Date startDate,
                 Date endDate,
                 boolean isRepeatable,
                 int timeDelta,
                 String description,
                 TimeUnit timeUnit,
                 Emotion emotion,
                 OtherSymptompsRecord otherSymptompsRecord,
                 DoctorsAppointment doctorsAppointment) {
        super(id);
        this.startDate = startDate;
        this.endDate = endDate;
        this.isRepeatable = isRepeatable;
        this.timeUnit = timeUnit;
        this.timeDelta = timeDelta;
        this.description = description;
        this.otherSymptompsRecord = otherSymptompsRecord;
        this.emotion = emotion;
        this.doctorsAppointment = doctorsAppointment;
    }

    protected Event(Parcel in) {
        super(in.readInt());
        isRepeatable = in.readByte() != 0;
        timeUnit = TimeUnit.valueOf(in.readString());
        timeDelta = in.readInt();
        description = in.readString();
        emotion = Emotion.valueOf(in.readString());
        otherSymptompsRecord = in.readParcelable(OtherSymptompsRecord.class.getClassLoader());
        doctorsAppointment = in.readParcelable(DoctorsAppointment.class.getClassLoader());
        try {
            startDate = DateTimeViewModel.DATETIME_FORMATTER.parse(in.readString());
            endDate = DateTimeViewModel.DATETIME_FORMATTER.parse(in.readString());
        } catch (Exception e) {

        }
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeString(DateTimeViewModel.DATETIME_FORMATTER.format(startDate));
        parcel.writeString(DateTimeViewModel.DATETIME_FORMATTER.format(endDate));
        parcel.writeByte((byte) (isRepeatable ? 1 : 0));
        parcel.writeString(timeUnit.name());
        parcel.writeInt(timeDelta);
        parcel.writeString(description);
        parcel.writeString(emotion.name());
        parcel.writeParcelable(otherSymptompsRecord, i);
        parcel.writeParcelable(doctorsAppointment, i);
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isRepeatable() {
        return isRepeatable;
    }

    public void setRepeatable(boolean repeatable) {
        isRepeatable = repeatable;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public int getTimeDelta() {
        return timeDelta;
    }

    public void setTimeDelta(int timeDelta) {
        this.timeDelta = timeDelta;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OtherSymptompsRecord getOtherSymptompsRecord() {
        return otherSymptompsRecord;
    }

    public void setOtherSymptompsRecord(OtherSymptompsRecord otherSymptompsRecord) {
        this.otherSymptompsRecord = otherSymptompsRecord;
    }

    public Emotion getEmotion() {
        return emotion;
    }

    public void setEmotion(Emotion emotion) {
        this.emotion = emotion;
    }

    public DoctorsAppointment getDoctorsAppointment() {
        return doctorsAppointment;
    }

    public void setDoctorsAppointment(DoctorsAppointment doctorsAppointment) {
        this.doctorsAppointment = doctorsAppointment;
    }

}
