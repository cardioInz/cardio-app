package cardio_app.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class OtherSymptompsRecord extends BaseModel implements Parcelable {
    @DatabaseField
    private boolean isCough;
    @DatabaseField
    private boolean isHeadache;
    @DatabaseField
    private boolean isHighTemperature;
    @DatabaseField
    private boolean isStomachAche;
    @DatabaseField
    private boolean isToothache;

    public OtherSymptompsRecord(){
    }

    public OtherSymptompsRecord(boolean isCough, boolean isHeadache, boolean isHighTemperature, boolean isStomachAche, boolean isToothache) {
        this.isCough = isCough;
        this.isHeadache = isHeadache;
        this.isHighTemperature = isHighTemperature;
        this.isStomachAche = isStomachAche;
        this.isToothache = isToothache;
    }

    public OtherSymptompsRecord(int id, boolean isCough, boolean isHeadache, boolean isHighTemperature, boolean isStomachAche, boolean isToothache) {
        super(id);
        this.isCough = isCough;
        this.isHeadache = isHeadache;
        this.isHighTemperature = isHighTemperature;
        this.isStomachAche = isStomachAche;
        this.isToothache = isToothache;
    }

    protected OtherSymptompsRecord(Parcel in) {
        super(in.readInt());
        isCough = in.readByte() != 0;
        isHeadache = in.readByte() != 0;
        isHighTemperature = in.readByte() != 0;
        isStomachAche = in.readByte() != 0;
        isToothache = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeByte((byte) (isCough ? 1: 0));
        parcel.writeByte((byte) (isHeadache ? 1: 0));
        parcel.writeByte((byte) (isHighTemperature ? 1: 0));
        parcel.writeByte((byte) (isStomachAche ? 1: 0));
        parcel.writeByte((byte) (isToothache ? 1: 0));
    }

    public static final Creator<OtherSymptompsRecord> CREATOR = new Creator<OtherSymptompsRecord>() {
        @Override
        public OtherSymptompsRecord createFromParcel(Parcel in) {
            return new OtherSymptompsRecord(in);
        }

        @Override
        public OtherSymptompsRecord[] newArray(int size) {
            return new OtherSymptompsRecord[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }



    public boolean isStomachAche() {
        return isStomachAche;
    }

    public void setStomachAche(boolean stomachAche) {
        this.isStomachAche = stomachAche;
    }

    public boolean isCough() {
        return isCough;
    }

    public void setCough(boolean cough) {
        isCough = cough;
    }

    public boolean isHeadache() {
        return isHeadache;
    }

    public void setHeadache(boolean headache) {
        isHeadache = headache;
    }

    public boolean isHighTemperature() {
        return isHighTemperature;
    }

    public void setHighTemperature(boolean highTemperature) {
        isHighTemperature = highTemperature;
    }

    public boolean isToothache() {
        return isToothache;
    }

    public void setToothache(boolean toothache) {
        isToothache = toothache;
    }



}
