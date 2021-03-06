package cardio_app.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONException;
import org.json.JSONObject;

@DatabaseTable
public class Drug extends BaseModel implements Parcelable {

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
    @DatabaseField
    private String name;
    @DatabaseField
    private String description;

    public Drug() {
    }

    public Drug(String name, String description) {
        this.name = name;
        this.description = description;
    }

    protected Drug(Parcel in) {
        super(in.readInt());
        name = in.readString();
        description = in.readString();
    }

    public Drug(int id, String name, String description) {
        super(id);
        this.name = name;
        this.description = description;
    }

    public static Drug convert(JSONObject object) throws JSONException {
        String name = object.getString("name");
        String description = object.getString("description");

        return new Drug(name, description);
    }

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

    public JSONObject convertToJson() throws JSONException {
        JSONObject object = new JSONObject();

        object.put("name", getName());
        object.put("description", getDescription());

        return object;
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
