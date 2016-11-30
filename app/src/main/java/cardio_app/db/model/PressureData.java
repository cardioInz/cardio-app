package cardio_app.db.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;

import cardio_app.statistics.analyse.HealthCondition;
import cardio_app.viewmodel.PressureDataViewModel;

import static cardio_app.util.DateTimeUtil.DATETIME_FORMATTER;

@DatabaseTable
public class PressureData extends BaseModel implements Parcelable, Comparable<PressureData> {


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

    public PressureData(PressureData pressureData) {
        initParams(pressureData.getSystole(),
                pressureData.getDiastole(),
                pressureData.getPulse(),
                pressureData.isArrhythmia(),
                pressureData.getDateTime());
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
            this.dateTime = DATETIME_FORMATTER.parse(in.readString());
        } catch (ParseException e) {
            e.printStackTrace();
            this.dateTime = new Date(0);
        }

    }

    public static PressureData convert(JSONObject object) throws JSONException, ParseException {
        int systole = object.getInt("systole");
        int diastole = object.getInt("diastole");
        int pulse = object.getInt("pulse");
        boolean arrhythmia = object.getBoolean("arrhythmia");
        Date date = DATETIME_FORMATTER.parse(object.getString("dateTime"));

        return new PressureData(systole, diastole, pulse, arrhythmia, date);
    }

    private static Comparator<PressureData> getComparator() {
        return (a1, a2) -> {
            int dateCmp = a2.compareTo(a1);
            if (dateCmp != 0)
                return dateCmp;
            return a2.getId() - a1.getId();
        };
    }

    private void initParams(int systole, int diastole, int pulse, boolean arrhythmia, Date dateTime) {
        this.systole = systole;
        this.diastole = diastole;
        this.pulse = pulse;
        this.arrhythmia = arrhythmia;
        this.dateTime = dateTime;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public int getSystole() {
        return systole;
    }

    public void setSystole(int systole) {
        this.systole = systole;
    }

    public int getDiastole() {
        return diastole;
    }

    public void setDiastole(int diastole) {
        this.diastole = diastole;
    }

    public int getPulse() {
        return pulse;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }

    public boolean isArrhythmia() {
        return arrhythmia;
    }

    public void setArrhythmia(boolean arrhythmia) {
        this.arrhythmia = arrhythmia;
    }

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
        parcel.writeString(DATETIME_FORMATTER.format(dateTime));
    }

    public JSONObject convertToJson() throws JSONException {
        JSONObject object = new JSONObject();

        object.put("systole", getSystole());
        object.put("diastole", getDiastole());
        object.put("pulse", getPulse());
        object.put("arrhythmia", isArrhythmia());
        object.put("dateTime", DATETIME_FORMATTER.format(getDateTime()));

        return object;
    }

    public HealthCondition getCondition() {
        return HealthCondition.classify(this);
    }
}
