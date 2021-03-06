package cardio_app.activity.profile;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import cardio_app.R;
import cardio_app.databinding.ActivityProfileBinding;
import cardio_app.db.DbHelper;
import cardio_app.db.model.UserProfile;
import cardio_app.viewmodel.ProfileViewModel;
import cardio_app.viewmodel.date_time.PickedDateViewModel;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = ProfileActivity.class.getName();
    PickedDateViewModel dateOfBirthViewModel;
    private DbHelper dbHelper;
    private UserProfile currentUser;
    private boolean isProfileAlreadyCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        try {
            Dao<UserProfile, Integer> dao = getDbHelper().getDao(UserProfile.class);
            List<UserProfile> userProfiles = dao.queryForAll();
            if (userProfiles.size() == 0) {
                currentUser = new UserProfile();
            } else {
                currentUser = userProfiles.get(0);
                isProfileAlreadyCreated = true;
            }
        } catch (SQLException e) {
            Log.e(TAG, "Cannot get profile data", e);
            throw new RuntimeException(e);
        }
        dateOfBirthViewModel = new PickedDateViewModel(currentUser.getDateOfBirth());
        ActivityProfileBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        binding.setProfile(new ProfileViewModel(currentUser));
        binding.setBirthday(dateOfBirthViewModel);
    }

    public DbHelper getDbHelper() {
        if (dbHelper == null) {
            dbHelper = OpenHelperManager.getHelper(this, DbHelper.class);
        }

        return dbHelper;
    }

    public void updateBirthDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(
                dateOfBirthViewModel.getYear(),
                dateOfBirthViewModel.getMonth(),
                dateOfBirthViewModel.getDay()
        );

        currentUser.setDateOfBrith(cal.getTime());
    }

    public void saveProfileChanges(View view) {
        updateBirthDate();
        try {
            Dao<UserProfile, Integer> dao = getDbHelper().getDao(UserProfile.class);
            if (!isProfileAlreadyCreated) {
                dao.create(currentUser);
            } else {
                dao.update(currentUser);
            }
            Toast.makeText(this, R.string.profile_saved_successfully, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, R.string.error_while_saving_profile, Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Cannot save profile changes", e);
            throw new RuntimeException(e);
        }
    }
}
