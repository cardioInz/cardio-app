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
    private OtherSymptomsRecord otherSymptomsRecord;
    @DatabaseField
    private Emotion emotion;
    @DatabaseField(foreign = true)
    private DoctorsAppointment doctorsAppointment;
    @DatabaseField
    private DailyActivitiesRecord dailyActivitiesRecord;
    @DatabaseField
    private boolean isAlarmSet;

    public Event() {
        this.startDate = getCurrentDate();
        this.endDate = getCurrentDate();
        this.isRepeatable = false;
        this.timeUnit = TimeUnit.NONE;
        this.timeDelta = 0;
        this.description = "";
        this.otherSymptomsRecord = new OtherSymptomsRecord();
        this.emotion = Emotion.NONE;
        this.doctorsAppointment = new DoctorsAppointment();
        this.dailyActivitiesRecord = DailyActivitiesRecord.NONE;
        this.isAlarmSet = false;

    }

    public Date getCurrentDate(){
        return new Date(System.currentTimeMillis());
    }

    public Event(Date startDate,
                 Date endDate,
                 boolean isRepeatable,
                 TimeUnit timeUnit,
                 int timeDelta,
                 String description,
                 OtherSymptomsRecord otherSymptomsRecord,
                 Emotion emotion,
                 DoctorsAppointment doctorsAppointment,
                 DailyActivitiesRecord dailyActivitiesRecord,
                 boolean isAlarmSet) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.isRepeatable = isRepeatable;
        this.timeUnit = timeUnit;
        this.timeDelta = timeDelta;
        this.description = description;
        this.otherSymptomsRecord = otherSymptomsRecord;
        this.emotion = emotion;
        this.doctorsAppointment = doctorsAppointment;
        this.dailyActivitiesRecord = dailyActivitiesRecord;
        this.isAlarmSet = isAlarmSet;
    }

    public Event(int id,
                 Date startDate,
                 Date endDate,
                 boolean isRepeatable,
                 TimeUnit timeUnit,
                 int timeDelta,
                 String description,
                 OtherSymptomsRecord otherSymptomsRecord,
                 Emotion emotion,
                 DoctorsAppointment doctorsAppointment,
                 DailyActivitiesRecord dailyActivitiesRecord,
                 boolean isAlarmSet) {
        super(id);
        this.startDate = startDate;
        this.endDate = endDate;
        this.isRepeatable = isRepeatable;
        this.timeUnit = timeUnit;
        this.timeDelta = timeDelta;
        this.description = description;
        this.otherSymptomsRecord = otherSymptomsRecord;
        this.emotion = emotion;
        this.doctorsAppointment = doctorsAppointment;
        this.dailyActivitiesRecord = dailyActivitiesRecord;
        this.isAlarmSet = isAlarmSet;
    }

    protected Event(Parcel in) {
        super(in.readInt());
        try {
            startDate = DateTimeViewModel.DATETIME_FORMATTER.parse(in.readString());
            endDate = DateTimeViewModel.DATETIME_FORMATTER.parse(in.readString());
        } catch (Exception e) {

        }
        isRepeatable = in.readByte() != 0;
        try {
            timeUnit = TimeUnit.valueOf(in.readString());
        } catch (Exception e) {
            timeUnit = TimeUnit.NONE;
        }
        timeDelta = in.readInt();
        description = in.readString();
        otherSymptomsRecord = in.readParcelable(OtherSymptomsRecord.class.getClassLoader());
        try {
            emotion = Emotion.valueOf(in.readString());
        } catch (Exception e) {
            emotion = Emotion.NONE;
        }
        doctorsAppointment = in.readParcelable(DoctorsAppointment.class.getClassLoader());
        try {
            dailyActivitiesRecord = DailyActivitiesRecord.valueOf(in.readString());
        } catch(Exception e) {
            dailyActivitiesRecord = DailyActivitiesRecord.NONE;
        }
        isAlarmSet = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeString(DateTimeViewModel.DATETIME_FORMATTER.format(startDate));
        parcel.writeString(DateTimeViewModel.DATETIME_FORMATTER.format(endDate));
        parcel.writeByte((byte) (isRepeatable ? 1 : 0));
        parcel.writeString(timeUnit == null ? "" : timeUnit.name());
        parcel.writeInt(timeDelta);
        parcel.writeString(description);
        parcel.writeParcelable(otherSymptomsRecord, i);
        parcel.writeString(emotion == null ? "" : emotion.name());
        parcel.writeParcelable(doctorsAppointment, i);
        parcel.writeString(dailyActivitiesRecord == null ? "" : dailyActivitiesRecord.name());
        parcel.writeByte((byte) (isAlarmSet ? 1 : 0));
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

    public OtherSymptomsRecord getOtherSymptomsRecord() {
        return otherSymptomsRecord;
    }

    public void setOtherSymptomsRecord(OtherSymptomsRecord otherSymptomsRecord) {
        this.otherSymptomsRecord = otherSymptomsRecord;
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

    public DailyActivitiesRecord getDailyActivitiesRecord() {
        return dailyActivitiesRecord;
    }

    public void setDailyActivitiesRecord(DailyActivitiesRecord dailyActivitiesRecord) {
        this.dailyActivitiesRecord = dailyActivitiesRecord;
    }

    public boolean isAlarmSet() {
        return isAlarmSet;
    }

    public void setAlarmSet(boolean alarmSet) {
        isAlarmSet = alarmSet;
    }
}
