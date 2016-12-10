package cardio_app.viewmodel;

import android.content.res.Resources;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.Calendar;
import java.util.HashMap;

import cardio_app.R;
import cardio_app.db.model.UserProfile;
import cardio_app.util.DateTimeUtil;

public class ProfileViewModel extends BaseObservable {
    private static final String EMPTY_IN_PDF = "-";
    private UserProfile userProfile;

    public ProfileViewModel() {
        this.userProfile = new UserProfile();
    }

    public ProfileViewModel(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    private int tryToInt(String str) {
        if (str.isEmpty())
            return 0;

        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }


    @Bindable
    public String getName() {
        return userProfile.getName();
    }

    public void setName(String name) {
        userProfile.setName(name);
    }

    @Bindable
    public String getSurname() {
        return userProfile.getSurname();
    }

    public void setSurname(String surname) {
        userProfile.setSurname(surname);
    }

    @Bindable
    public boolean isMale() {
        return false;
    }

    public void setMale(boolean sex) {
        userProfile.setSex(UserProfile.SexType.MALE);
    }

    @Bindable
    public boolean isFemale() {
        return true;
    }

    public void setFemale(boolean sex) {
        userProfile.setSex(UserProfile.SexType.FEMALE);
    }

    @Bindable
    public String getSex() {
        try {
            return userProfile.getSex().toString();
        } catch (Exception e) {
            return "-";
        }
    }

    public void setSex(String sex) {
        userProfile.setSex(UserProfile.SexType.mapFromString(sex));
    }

    @Bindable
    public String getWeight() {
        return String.valueOf(userProfile.getWeight());
    }

    public void setWeight(String weight) {
        userProfile.setWeight(tryToInt(weight));
    }

    @Bindable
    public String getHeight() {
        return String.valueOf(userProfile.getHeight());
    }

    public void setHeight(String height) {
        userProfile.setHeight(tryToInt(height));
    }

    @Bindable
    public String getCholesterol() {
        return String.valueOf(userProfile.getCholesterol());
    }

    public void setCholesterol(String cholesterol) {
        userProfile.setCholesterol(tryToInt(cholesterol));
    }

    @Bindable
    public String getGlucose() {
        return String.valueOf(userProfile.getGlucose());
    }

    public void setGlucose(String glucose) {
        userProfile.setGlucose(tryToInt(glucose));
    }

    @Bindable
    public boolean getSmoker() {
        return userProfile.isSmoker();
    }

    public void setSmoker(boolean isSmoker) {
        userProfile.setSmoker(isSmoker);
    }


    public String getDateOfBirthStrForPdf() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -2);

        if (userProfile == null || userProfile.getDateOfBirth() == null)
            return EMPTY_IN_PDF;
        else if (c.getTime().compareTo(userProfile.getDateOfBirth()) < 0)
            return EMPTY_IN_PDF;
        return DateTimeUtil.DATE_FORMATTER.format(userProfile.getDateOfBirth());
    }

    public String getCholesterolStr() {
        try {
            int val = userProfile.getCholesterol();
            if (val <= 0)
                return EMPTY_IN_PDF;
            return String.valueOf(val);
        } catch (Exception e) {
            return EMPTY_IN_PDF;
        }
    }

    public String getGlucoseStr() {
        try {
            int val = userProfile.getGlucose();
            if (val <= 0)
                return EMPTY_IN_PDF;
            return String.valueOf(val);
        } catch (Exception e) {
            return EMPTY_IN_PDF;
        }
    }


    public String getWeightStr() {
        try {
            int val = userProfile.getHeight();
            if (val <= 0)
                return EMPTY_IN_PDF;
            return String.valueOf(val);
        } catch (Exception e) {
            return EMPTY_IN_PDF;
        }
    }

    public String getHeightStr() {
        try {
            int val = userProfile.getHeight();
            if (val <= 0)
                return EMPTY_IN_PDF;
            return String.valueOf(val);
        } catch (Exception e) {
            return EMPTY_IN_PDF;
        }
    }

    public String getSurnameStr() {
        try {
            if (userProfile.getSurname() == null)
                return EMPTY_IN_PDF;
            return getSurname();
        } catch (Exception e) {
            return EMPTY_IN_PDF;
        }
    }

    public String getNameStr() {
        try {
            if (userProfile.getName() == null)
                return EMPTY_IN_PDF;
            return getName();
        } catch (Exception e) {
            return EMPTY_IN_PDF;
        }
    }

    public String getSmokerNullable(String yes, String no) {
        try {
            Boolean b = userProfile.isSmoker();
            if (b == null)
                return EMPTY_IN_PDF;
            return b.booleanValue() ? yes : no;
        } catch (Exception e) {
            return EMPTY_IN_PDF;
        }
    }

    public String getSexStr(String male, String female) {
        try {
            switch (userProfile.getSex()) {
                case MALE:
                    return male;
                case FEMALE:
                    return female;
                default:
                    return EMPTY_IN_PDF;
            }
        } catch (Exception e) {
            return EMPTY_IN_PDF;
        }
    }

    public enum MyProfileFieldTypeEnum {
        FIRST_NAME,
        SURNAME,
        DATE_OF_BIRTH,
        SEX,
        HEIGHT,
        WEIGHT,
        SMOKER,
        GLUCOSE,
        CHOLESTEROL;

        public static MyProfileFieldTypeEnum[] questionaireKeysInOrder() {
            return new MyProfileFieldTypeEnum[]{
                    FIRST_NAME,
                    SURNAME,
                    DATE_OF_BIRTH,
                    SEX,
                    HEIGHT,
                    WEIGHT,
                    SMOKER,
                    GLUCOSE,
                    CHOLESTEROL
            };
        }

        private final static HashMap<MyProfileFieldTypeEnum, Integer> mapToStrId = new HashMap<MyProfileFieldTypeEnum, Integer>() {{
            put(FIRST_NAME, R.string.first_name);
            put(SURNAME, R.string.surname);
            put(DATE_OF_BIRTH, R.string.date_of_birth);
            put(SEX, R.string.sex);
            put(HEIGHT, R.string.height);
            put(WEIGHT, R.string.weight);
            put(SMOKER, R.string.smoker);
            put(GLUCOSE, R.string.glucose);
            put(CHOLESTEROL, R.string.cholesterol);
        }};

        public static String getFieldTitle(MyProfileFieldTypeEnum fieldTypeEnum, Resources resources) {
            if (!mapToStrId.containsKey(fieldTypeEnum))
                return null;

            int strId = mapToStrId.get(fieldTypeEnum);
            String title = resources.getString(strId);

            switch (fieldTypeEnum) {
                case HEIGHT:
                    return title.replace(":", " [cm]:");
                case WEIGHT:
                    return title.replace(":", " [kg]:");
                case GLUCOSE:
                case CHOLESTEROL:
                    return title.replace(":", " [mmol/l]:");
                default:
                    return title;
            }
        }
    }


    public HashMap<MyProfileFieldTypeEnum, String> getHashMapValues(Resources resources) {
        ProfileViewModel profileViewModel = this;
        return new HashMap<MyProfileFieldTypeEnum, String>() {{
            put(MyProfileFieldTypeEnum.FIRST_NAME, profileViewModel.getNameStr());
            put(MyProfileFieldTypeEnum.SURNAME, profileViewModel.getSurnameStr());
            put(MyProfileFieldTypeEnum.DATE_OF_BIRTH, profileViewModel.getDateOfBirthStrForPdf());
            put(MyProfileFieldTypeEnum.SEX, profileViewModel.getSexStr(
                    resources.getString(R.string.male), resources.getString(R.string.female)
            ));
            put(MyProfileFieldTypeEnum.HEIGHT, profileViewModel.getHeightStr());
            put(MyProfileFieldTypeEnum.WEIGHT, profileViewModel.getWeightStr());
            put(MyProfileFieldTypeEnum.SMOKER, profileViewModel.getSmokerNullable(
                    resources.getString(R.string.yes), resources.getString(R.string.no)
            ));
            put(MyProfileFieldTypeEnum.GLUCOSE, profileViewModel.getGlucoseStr());
            put(MyProfileFieldTypeEnum.CHOLESTEROL, profileViewModel.getCholesterolStr());
        }};
    }
}

