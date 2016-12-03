package cardio_app.viewmodel;

import android.content.res.Resources;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

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


    public String getDateOfBirthStr() {
        if (userProfile == null || userProfile.getDateOfBirth() == null)
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
            boolean b = userProfile.isSmoker();
            return b ? yes : no;
        } catch (Exception e) {
            return EMPTY_IN_PDF;
        }
    }

    public String getSexStr(String male, String female){
        try {
            switch (userProfile.getSex()) {
                case MALE:
                    return male;
                case FEMALE:
                    return female;
                default:
                    return EMPTY_IN_PDF;
            }
        } catch (Exception e){
            return EMPTY_IN_PDF;
        }
    }

    public HashMap<String, String> getHashMapFields(Resources resources) {
        ProfileViewModel profileViewModel = this;
        return new HashMap<String, String>() {{
            put(resources.getString(R.string.first_name), profileViewModel.getNameStr());
            put(resources.getString(R.string.surname), profileViewModel.getSurnameStr());
            put(resources.getString(R.string.date_of_birth), profileViewModel.getDateOfBirthStr());
            put(resources.getString(R.string.sex), profileViewModel.getSexStr(
                    resources.getString(R.string.male), resources.getString(R.string.female)
            ));
            put(resources.getString(R.string.height).replace(":", " [cm]:"), profileViewModel.getHeightStr());
            put(resources.getString(R.string.weight).replace(":", " [kg]:"), profileViewModel.getWeightStr());
            put(resources.getString(R.string.smoker), profileViewModel.getSmokerNullable(
                    resources.getString(R.string.yes), resources.getString(R.string.no)
            ));
            put(resources.getString(R.string.glucose).replace(":", " [mmol/l]:"), profileViewModel.getGlucoseStr());
            put(resources.getString(R.string.cholesterol).replace(":", " [mmol/l]:"), profileViewModel.getCholesterolStr());
        }};
    }
}

