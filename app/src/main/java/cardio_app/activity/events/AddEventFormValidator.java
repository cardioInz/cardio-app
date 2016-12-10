package cardio_app.activity.events;


import android.widget.LinearLayout;

import cardio_app.R;
import cardio_app.viewmodel.EventDataViewModel;

public class AddEventFormValidator {
    public static boolean validateDescription(LinearLayout descriptionLinearLayout, EventDataViewModel vm) {
        boolean isValid = true;
        if (vm.getDescription() == "") {
            descriptionLinearLayout.setBackgroundResource(R.drawable.input_activities_background_error);
            isValid = false;
        } else {
            descriptionLinearLayout.setBackgroundResource(R.drawable.input_activities_background);
        }
        return isValid;
    }

    public static boolean validateRepeatableSection(LinearLayout timeDeltaLL, LinearLayout timeUnitLL, EventDataViewModel vm) {
        boolean isValid = true;
        if (vm.getRepeatable()) {
            if (vm.getTimeDelta().equals("0") || vm.getTimeDelta().equals("")) {
                timeDeltaLL.setBackgroundResource(R.drawable.input_activities_background_error);
                isValid = false;
            } else {
                timeDeltaLL.setBackgroundResource(R.drawable.input_activities_background);
            }

            if (!vm.isDay() && !vm.isMonth() && !vm.isWeek()) {
                timeUnitLL.setBackgroundResource(R.drawable.input_activities_background_error);
                isValid = false;
            } else {
                timeDeltaLL.setBackgroundResource(R.drawable.input_activities_background);
            }
        }
        return isValid;
    }


}
