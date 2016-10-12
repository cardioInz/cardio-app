package cardio_app.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Drug extends BaseModel implements Parcelable {

    @DatabaseField
    private String name;

    @DatabaseField
    private String description;

    public Drug() {}

    public Drug(String name, String description) {
        this.name = name;
        this.description = description;
    }

    protected Drug(Parcel in) {
        super(in.readInt());
        name = in.readString();
        description = in.readString();
    }

    public static final Creator<Drug> CREATOR = new Creator<Drug>() {
        @Override
        public Drug createFromParcel(Parcel in) {
            return new Drug(in);
        }

        @Override
        public Drug[] newArray(int size) {
            return new Drug[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Drug(int id, String name, String description) {
        super(id);
        this.name = name;
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeString(name);
        parcel.writeString(description);
    }
}
