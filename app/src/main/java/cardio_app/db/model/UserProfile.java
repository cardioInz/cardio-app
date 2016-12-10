package cardio_app.db.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;

import static cardio_app.util.DateTimeUtil.DATETIME_FORMATTER;

@DatabaseTable
public class UserProfile extends BaseModel implements Parcelable {

    public static final Parcelable.Creator<UserProfile> CREATOR = new Parcelable.Creator<UserProfile>() {
        @Override
        public UserProfile createFromParcel(Parcel in) {
            return new UserProfile(in);
        }

        @Override
        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };
    @DatabaseField
    private String name = null;
    @DatabaseField
    private String surname = null;
    @DatabaseField
    private Date dateOfBirth = null;
    @DatabaseField
    private String sex = null;
    @DatabaseField
    private int weight;
    @DatabaseField
    private int height;
    @DatabaseField
    private int cholesterol;
    @DatabaseField
    private int glucose;
    @DatabaseField
    private Boolean isSmoker = null;

    public UserProfile() {
        this.name = null;
        this.surname = null;
        this.dateOfBirth = new Date();
        this.sex = SexType.NOT_SET.getStr();
        this.weight = 0;
        this.height = 0;
        this.cholesterol = 0;
        this.glucose = 0;
        this.isSmoker = false;
    }

    public UserProfile(String name,
                       String surname,
                       Date dateOfBirth,
                       String sex,
                       int weight,
                       int height,
                       int cholesterol,
                       int glucose,
                       boolean isSmoker) {
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
        this.weight = weight;
        this.height = height;
        this.cholesterol = cholesterol;
        this.glucose = glucose;
        this.isSmoker = isSmoker;
    }

    public UserProfile(int id,
                       String name,
                       String surname,
                       Date dateOfBirth,
                       String sex,
                       int weight,
                       int height,
                       int cholesterol,
                       int glucose,
                       boolean isSmoker) {
        super(id);
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
        this.weight = weight;
        this.height = height;
        this.cholesterol = cholesterol;
        this.glucose = glucose;
        this.isSmoker = isSmoker;
    }

    protected UserProfile(Parcel in) {
        setId(in.readInt());
        this.name = in.readString();
        this.surname = in.readString();
        try {
            this.dateOfBirth = DATETIME_FORMATTER.parse(in.readString());
        } catch (ParseException e) {
            e.printStackTrace();
            this.dateOfBirth = new Date(0);
        }
        this.sex = in.readString();
        this.weight = in.readInt();
        this.height = in.readInt();
        this.cholesterol = in.readInt();
        this.glucose = in.readInt();
        this.isSmoker = in.readByte() != 0;
    }

    public static UserProfile convert(JSONObject object) throws JSONException, ParseException {
        String name = object.getString("name");
        String surname = object.getString("surname");
        Date dateOfBirth = DATETIME_FORMATTER.parse(object.getString("dateOfBirth"));
        String sex = object.getString("sex");
        int weight = object.getInt("weight");
        int height = object.getInt("height");
        int cholesterol = object.getInt("cholesterol");
        int glucose = object.getInt("glucose");
        boolean isSmoker = object.getBoolean("isSmoker");

        return new UserProfile(name, surname, dateOfBirth, sex, weight, height, cholesterol, glucose, isSmoker);
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeString(this.name);
        parcel.writeString(this.surname);
        parcel.writeString(DATETIME_FORMATTER.format(this.dateOfBirth));
        parcel.writeString(this.sex);
        parcel.writeInt(this.weight);
        parcel.writeInt(this.height);
        parcel.writeInt(this.cholesterol);
        parcel.writeInt(this.glucose);
        parcel.writeByte((byte) (this.isSmoker ? 1 : 0));
    }

    public JSONObject convertToJson() throws JSONException {
        JSONObject object = new JSONObject();

        object.put("name", getName());
        object.put("surname", getSurname());
        object.put("dateOfBirth", DATETIME_FORMATTER.format(getDateOfBirth()));
        object.put("sex", getSex());
        object.put("weight", getWeight());
        object.put("height", getHeight());
        object.put("cholesterol", getCholesterol());
        object.put("glucose", getGlucose());
        object.put("isSmoker", isSmoker());

        return object;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBrith(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public SexType getSex() {
        try {
            return SexType.mapFromString(sex);
        } catch (Exception e) {
            return SexType.NOT_SET;
        }
    }

    public void setSex(SexType sexType) {
        this.sex = SexType.mapToString(sexType);
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(int cholesterol) {
        this.cholesterol = cholesterol;
    }

    public int getGlucose() {
        return glucose;
    }

    public void setGlucose(int glucose) {
        this.glucose = glucose;
    }

    public Boolean isSmoker() {
        return isSmoker;
    }

    public void setSmoker(Boolean smoker) {
        isSmoker = smoker;
    }

    public enum SexType {
        MALE,
        FEMALE,
        NOT_SET;

        private static final HashMap<SexType, String> sexMap = new HashMap<SexType, String>() {{
            put(MALE, "M");
            put(FEMALE, "F");
            put(NOT_SET, "-");
        }};

        public static String mapToString(SexType sexType) {
            if (sexType != null)
                return sexType.toString();
            return NOT_SET.toString();
        }

        public static SexType mapFromString(String value) {
            if (sexMap.values().contains(value)) {
                for (SexType key : sexMap.keySet()) {
                    if (sexMap.get(key).equals(value))
                        return key;
                }
            }
            return NOT_SET;
        }

        public String getStr() {
            if (sexMap.containsKey(this))
                return sexMap.get(this);
            else
                return sexMap.get(NOT_SET);
        }
    }

}

