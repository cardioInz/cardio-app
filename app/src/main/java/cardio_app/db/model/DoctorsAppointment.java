package cardio_app.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class DoctorsAppointment extends BaseModel implements Parcelable{
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

    public DoctorsAppointment(){
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

    private DoctorsAppointment(Parcel in) {
        setId(in.readInt());
        isRoutineCheck = in.readByte() != 0;
        isExamination = in.readByte() != 0;
        isForPrescription = in.readByte() != 0;
        isEmergency = in.readByte() != 0;
        isFlu = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeByte((byte) (isRoutineCheck ? 1: 0));
        parcel.writeByte((byte) (isExamination ? 1: 0));
        parcel.writeByte((byte) (isForPrescription ? 1: 0));
        parcel.writeByte((byte) (isEmergency ? 1: 0));
        parcel.writeByte((byte) (isFlu ? 1: 0));
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

}
