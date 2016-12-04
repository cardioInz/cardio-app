package cardio_app.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONException;
import org.json.JSONObject;

@DatabaseTable
public class OtherSymptomsRecord extends BaseModel implements Parcelable {
    public static final Creator<OtherSymptomsRecord> CREATOR = new Creator<OtherSymptomsRecord>() {
        @Override
        public OtherSymptomsRecord createFromParcel(Parcel in) {
            return new OtherSymptomsRecord(in);
        }

        @Override
        public OtherSymptomsRecord[] newArray(int size) {
            return new OtherSymptomsRecord[size];
        }
    };
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

    public OtherSymptomsRecord() {
        isCough=false;
        isHeadache = false;
        isHighTemperature = false;
        isStomachAche = false;
        isToothache = false;
    }

    public OtherSymptomsRecord(boolean isCough, boolean isHeadache, boolean isHighTemperature, boolean isStomachAche, boolean isToothache) {
        this.isCough = isCough;
        this.isHeadache = isHeadache;
        this.isHighTemperature = isHighTemperature;
        this.isStomachAche = isStomachAche;
        this.isToothache = isToothache;
    }

    public OtherSymptomsRecord(int id, boolean isCough, boolean isHeadache, boolean isHighTemperature, boolean isStomachAche, boolean isToothache) {
        super(id);
        this.isCough = isCough;
        this.isHeadache = isHeadache;
        this.isHighTemperature = isHighTemperature;
        this.isStomachAche = isStomachAche;
        this.isToothache = isToothache;
    }

    protected OtherSymptomsRecord(Parcel in) {
        super(in.readInt());
        isCough = in.readByte() != 0;
        isHeadache = in.readByte() != 0;
        isHighTemperature = in.readByte() != 0;
        isStomachAche = in.readByte() != 0;
        isToothache = in.readByte() != 0;
    }

    public static OtherSymptomsRecord convert(JSONObject object) throws JSONException {
        boolean isCough = object.getBoolean("isCough");
        boolean isHeadache = object.getBoolean("isHeadache");
        boolean isHighTemperature = object.getBoolean("isHighTemperature");
        boolean isStomachAche = object.getBoolean("isStomachAche");
        boolean isToothache = object.getBoolean("isToothache");

        return new OtherSymptomsRecord(isCough, isHeadache, isHighTemperature, isStomachAche, isToothache);
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeByte((byte) (isCough ? 1 : 0));
        parcel.writeByte((byte) (isHeadache ? 1 : 0));
        parcel.writeByte((byte) (isHighTemperature ? 1 : 0));
        parcel.writeByte((byte) (isStomachAche ? 1 : 0));
        parcel.writeByte((byte) (isToothache ? 1 : 0));
    }

    public JSONObject convertToJSON() throws JSONException {
        JSONObject object = new JSONObject();

        object.put("isCough", isCough());
        object.put("isHeadache", isHeadache());
        object.put("isHighTemperature", isHighTemperature());
        object.put("isStomachAche", isStomachAche());
        object.put("isToothache", isToothache());

        return object;
    }

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

    public boolean isOtherSymptom() {
        return this.isCough() || this.isHeadache || this.isHighTemperature ||
                this.isStomachAche() || this.isToothache();
    }

    public String getOtherSymptomsDescription() {
        String description = "";
        description += this.isHeadache() ? "headache, " : "";
        description += this.isHighTemperature() ? "high temperature, " : "";
        description += this.isCough() ? "cough, " : "";
        description += this.isToothache() ? "toothache, " : "";
        description += this.isStomachAche() ? "stomachache, " : "";
        if (description != "") {
            return description.substring(0, description.length()-2);
        }
        return description;
    }
}
