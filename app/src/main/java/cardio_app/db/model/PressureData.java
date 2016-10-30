package cardio_app.db.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cardio_app.viewmodel.pressure.PressureDataViewModel;

@DatabaseTable
public class PressureData extends BaseModel implements Parcelable, Comparable<PressureData> {

    private static int ID_CNT = 0;
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("HH:mm");
    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat DATETIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm");

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

    }

    private void initParams(int systole, int diastole, int pulse, boolean arrhythmia, Date dateTime){
        this.systole = systole;
        this.diastole = diastole;
        this.pulse = pulse;
        this.arrhythmia = arrhythmia;
        this.dateTime = dateTime;
    }


    public PressureData(int systole, int diastole, int pulse, boolean arrhythmia, Date dateTime) {
        super(++ID_CNT);
        initParams(systole, diastole, pulse, arrhythmia, dateTime);
    }


    private PressureData(Parcel in) {
        systole = in.readInt();
        diastole = in.readInt();
        pulse = in.readInt();
        arrhythmia = in.readString().equals(PressureDataViewModel.ARRYTHMIA_STR);
        try {
            Date date = DATE_FORMATTER.parse(in.readString());
            Date time = TIME_FORMATTER.parse(in.readString());
            this.dateTime = new Date(date.getTime() + time.getTime());
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

    public int getSystole() {
        return systole;
    }

    public int getDiastole() {
        return diastole;
    }

    public int getPulse() {
        return pulse;
    }

    public boolean isArrhythmia() {
        return arrhythmia;
    }

    @Override
    public int compareTo(@NonNull PressureData that) {
        int result = this.dateTime.compareTo(that.dateTime);
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
        parcel.writeInt(systole);
        parcel.writeInt(diastole);
        parcel.writeInt(pulse);
        parcel.writeString(arrhythmia ? PressureDataViewModel.ARRYTHMIA_STR : PressureDataViewModel.NO_ARRYTMIA_STR);
        parcel.writeString(makeDateStr(dateTime));
        parcel.writeString(makeTimeStr(dateTime));
    }


    public static String makeDateStr(Date date) {
        return DATE_FORMATTER.format(date);
    }

    public static String makeTimeStr(Date date) {
        return TIME_FORMATTER.format(date);
    }
}
