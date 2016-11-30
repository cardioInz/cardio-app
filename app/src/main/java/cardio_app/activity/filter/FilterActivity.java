package cardio_app.activity.filter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import cardio_app.R;
import cardio_app.filtering.DataFilter;
import cardio_app.filtering.DataFilterModeEnum;
import cardio_app.util.Defaults;

public class FilterActivity extends AppCompatActivity {
    private final static String TAG = FilterActivity.class.toString();
    final DataFilter dataFilter = Defaults.getDefaultDataFilter();
    @SuppressLint("UseSparseArrays")
    final static HashMap<Integer, DataFilterModeEnum> mapFilter = new HashMap<>();

    public static Integer getIdFromMode(DataFilterModeEnum mode) {
        for (Integer id : mapFilter.keySet()) {
            if (mapFilter.get(id).equals(mode)) {
                return id;
            }
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        mapFilter.put(R.id.radio_filter_custom, DataFilterModeEnum.CUSTOM_DATES);
        mapFilter.put(R.id.radio_filter_no_filter, DataFilterModeEnum.NO_FILTER);
        mapFilter.put(R.id.radio_filter_this_moth, DataFilterModeEnum.THIS_MONTH);
        mapFilter.put(R.id.radio_filter_this_year, DataFilterModeEnum.THIS_YEAR);
        mapFilter.put(R.id.radio_filter_x_days, DataFilterModeEnum.LAST_X_DAYS);

        Intent intent = getIntent();
        DataFilter df = intent.getParcelableExtra("filterdata");

        if (df != null) {
            if (mapFilter.containsValue(df.getMode())) {
                dataFilter.setMode(df.getMode());
                RadioButton radioBtnView = (RadioButton) findViewById(getIdFromMode(df.getMode()));
                radioBtnView.setChecked(true);
                onRadioButtonClicked(radioBtnView);

                switch (dataFilter.getMode()) {
                    case LAST_X_DAYS:
                        EditText editText = (EditText) findViewById(R.id.x_days_value);
                        editText.setText(String.valueOf(df.getXDays()));
                        break;
                    case CUSTOM_DATES:
                        Calendar calendar = Calendar.getInstance();

                        Date dateFrom = df.getDateFrom();
                        calendar.setTime(dateFrom);
                        DatePicker dateFromPicker = (DatePicker) findViewById(R.id.filter_dateFrom_datePicker);
                        dateFromPicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                        Date dateTo = df.getDateTo();
                        calendar.setTime(dateTo);
                        DatePicker dateToPicker = (DatePicker) findViewById(R.id.filter_dateTo_datePicker);
                        dateToPicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        break;
                    default:
                        break;
                }

            } else {
                Log.e(TAG, "onCreate: data filter mode was unsupported -> mode: " + df.getModeStr());
            }
        } else {

        }

    }

    public Date getDateFromDatePicker(DatePicker datePicker) {
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    public void sendResponse() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("filterdata", dataFilter);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public void setOnFilterButtonClicked(View view) {
        DataFilterModeEnum mode = dataFilter.getMode();

        switch (mode) {
            case LAST_X_DAYS:
                String xDaysStr = ((EditText) findViewById(R.id.x_days_value)).getText().toString();
                try {
                    Integer xDays = Integer.parseInt(xDaysStr);
                    dataFilter.setLastXDaysFilterMode(xDays);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, R.string.non_negative_number_value_is_required, Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            case CUSTOM_DATES:
                DatePicker dateFromPicker = (DatePicker) findViewById(R.id.filter_dateFrom_datePicker);
                DatePicker dateToPicker = (DatePicker) findViewById(R.id.filter_dateTo_datePicker);

                Date dateFrom = getDateFromDatePicker(dateFromPicker);
                Date dateTo = getDateFromDatePicker(dateToPicker);

                if (dateTo.before(dateFrom)) {
                    Toast.makeText(this, R.string.date_to_could_not_be_before_date_from, Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    dataFilter.setDateFrom(dateFrom);
                    dataFilter.setDateTo(dateTo);
                    break;
                }
            default:
                break;
        }

        sendResponse();
    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        Integer id = view.getId();
        DataFilterModeEnum filterMode = mapFilter.get(id);
        dataFilter.setMode(filterMode);
        setLayoutsVisibility();

        Button filterBtn = (Button) findViewById(R.id.filter_btn);
        filterBtn.setEnabled(checked);
    }


    private void setLayoutsVisibility() {
        boolean xDaysFilterChecked = ((RadioButton) findViewById(R.id.radio_filter_x_days)).isChecked();
        LinearLayout xDaysLayout = (LinearLayout) findViewById(R.id.x_days_layout);
        xDaysLayout.setVisibility(xDaysFilterChecked ? View.VISIBLE : View.GONE);

        boolean customFilterChecked = ((RadioButton) findViewById(R.id.radio_filter_custom)).isChecked();
        LinearLayout customFilterLayout = (LinearLayout) findViewById(R.id.custom_filter_layout);
        customFilterLayout.setVisibility(customFilterChecked ? View.VISIBLE : View.GONE);
    }
}
