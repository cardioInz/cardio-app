package cardio_app.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONException;
import org.json.JSONObject;

@DatabaseTable
public class DoctorsAppointment extends BaseModel implements Parcelable {
    public static final Creator<DoctorsAppointment> CREATOR = new Creator<DoctorsAppointment>() {
        @Override
        public DoctorsAppointment createFromParcel(Parcel in) {
            return new DoctorsAppointment(in);
        }

        @Override
        public DoctorsAppointment[] newArray(int size) {
            return new DoctorsAppointment[size];
        }
    };
    @DatabaseField
    private boolean isRoutineCheck;
    @DatabaseField
    private boolean isExamination;
    @DatabaseField
    private boolean isForPrescription;
    @DatabaseField
    private boolean isEmergency;
    @DatabaseField
    private boolean isFlu;

    public DoctorsAppointment() {
    }

    public DoctorsAppointment(int id, boolean isRoutineCheck, boolean isExamination, boolean isForPrescription, boolean isEmergency, boolean isFlu) {
        super(id);
        this.isRoutineCheck = isRoutineCheck;
        this.isExamination = isExamination;
        this.isForPrescription = isForPrescription;
        this.isEmergency = isEmergency;
        this.isFlu = isFlu;
    }

    public DoctorsAppointment(boolean isRoutineCheck, boolean isExamination, boolean isForPrescription, boolean isEmergency, boolean isFlu) {
        this.isRoutineCheck = isRoutineCheck;
        this.isExamination = isExamination;
        this.isForPrescription = isForPrescription;
        this.isEmergency = isEmergency;
        this.isFlu = isFlu;
    }

    private DoctorsAppointment(Parcel in) {
        setId(in.readInt());
        isRoutineCheck = in.readByte() != 0;
        isExamination = in.readByte() != 0;
        isForPrescription = in.readByte() != 0;
        isEmergency = in.readByte() != 0;
        isFlu = in.readByte() != 0;
    }

    public static DoctorsAppointment convert(JSONObject object) throws JSONException {
        boolean isRoutineCheck = object.getBoolean("isRoutineCheck");
        boolean isExamination = object.getBoolean("isExamination");
        boolean isForPrescription = object.getBoolean("isForPrescription");
        boolean isEmergency = object.getBoolean("isEmergency");
        boolean isFlu = object.getBoolean("isFlu");

        return new DoctorsAppointment(isRoutineCheck, isExamination, isForPrescription, isEmergency, isFlu);
    }

    public boolean isRoutineCheck() {
        return isRoutineCheck;
    }

    public void setRoutineCheck(boolean routineCheck) {
        isRoutineCheck = routineCheck;
    }

    public boolean isExamination() {
        return isExamination;
    }

    public void setExamination(boolean examination) {
        isExamination = examination;
    }

    public boolean isForPrescription() {
        return isForPrescription;
    }

    public void setForPrescription(boolean forPrescription) {
        isForPrescription = forPrescription;
    }

    public boolean isEmergency() {
        return isEmergency;
    }

    public void setEmergency(boolean emergency) {
        isEmergency = emergency;
    }

    public boolean isFlu() {
        return isFlu;
    }

    public void setFlu(boolean flu) {
        isFlu = flu;
    }

    public boolean isDoctorsAppointment() {
        return isRoutineCheck ||
                isEmergency ||
                isFlu ||
                isForPrescription ||
                isExamination;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeByte((byte) (isRoutineCheck ? 1 : 0));
        parcel.writeByte((byte) (isExamination ? 1 : 0));
        parcel.writeByte((byte) (isForPrescription ? 1 : 0));
        parcel.writeByte((byte) (isEmergency ? 1 : 0));
        parcel.writeByte((byte) (isFlu ? 1 : 0));
    }

    public JSONObject convertToJson() throws JSONException {
        JSONObject object = new JSONObject();

        object.put("isRoutineCheck", isRoutineCheck());
        object.put("isExamination", isExamination());
        object.put("isForPrescription", isForPrescription());
        object.put("isEmergency", isEmergency());
        object.put("isFlu", isFlu());

        return object;
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
