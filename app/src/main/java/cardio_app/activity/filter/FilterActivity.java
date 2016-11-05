package cardio_app.activity.filter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import cardio_app.filtering_and_statistics.DataFilter;
import cardio_app.filtering_and_statistics.DataFilterModeEnum;

public class FilterActivity extends AppCompatActivity {

    final DataFilter dataFilter = new DataFilter();
    @SuppressLint("UseSparseArrays")
    final static HashMap<Integer, DataFilterModeEnum> mapFilter = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        mapFilter.put(R.id.radio_filter_custom, DataFilterModeEnum.CUSTOM_DATES);
        mapFilter.put(R.id.radio_filter_no_filter, DataFilterModeEnum.NO_FILTER);
        mapFilter.put(R.id.radio_filter_this_moth, DataFilterModeEnum.THIS_MONTH);
        mapFilter.put(R.id.radio_filter_this_year, DataFilterModeEnum.THIS_YEAR);
        mapFilter.put(R.id.radio_filter_x_days, DataFilterModeEnum.LAST_X_DAYS);
    }

    public Date getDateFromDatePicker(DatePicker datePicker){
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    public void sendResponse(){

        if (dataFilter.getMode().equals(DataFilterModeEnum.NO_FILTER)){
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
            return;
        }

        Date dateFrom = dataFilter.getDateFrom();
        Date dateTo = dataFilter.getDateTo();

        String dateFromStr = DataFilter.DATE_FORMATTER.format(dateFrom);
        String dateToStr = DataFilter.DATE_FORMATTER.format(dateTo);

        Intent returnIntent = new Intent();
        returnIntent.putExtra("dateFrom", dateFromStr);
        returnIntent.putExtra("dateTo", dateToStr);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    public void setOnFilterButtonClicked(View view){
        DataFilterModeEnum mode = dataFilter.getMode();

        switch (mode){
            case LAST_X_DAYS:
                String xDaysStr = ((EditText) findViewById(R.id.x_days_value)).getText().toString();
                try {
                    Integer xDays = Integer.parseInt(xDaysStr);
                    dataFilter.setLastXDaysFilterMode(xDays);
                } catch (NumberFormatException e){
                    Toast.makeText(this, R.string.non_negative_number_value_is_required, Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            case CUSTOM_DATES:
                DatePicker dateFromPicker = (DatePicker) findViewById(R.id.filter_dateFrom_datePicker);
                DatePicker dateToPicker = (DatePicker) findViewById(R.id.filter_dateTo_datePicker);

                Date dateFrom = getDateFromDatePicker(dateFromPicker);
                Date dateTo = getDateFromDatePicker(dateToPicker);

                if (dateTo.before(dateFrom)){
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


    private void setLayoutsVisibility(){
        boolean xDaysFilterChecked = ((RadioButton) findViewById(R.id.radio_filter_x_days)).isChecked();
        LinearLayout xDaysLayout = (LinearLayout) findViewById(R.id.x_days_layout);
        xDaysLayout.setVisibility(xDaysFilterChecked ? View.VISIBLE : View.GONE);

        boolean customFilterChecked = ((RadioButton) findViewById(R.id.radio_filter_custom)).isChecked();
        LinearLayout customFilterLayout = (LinearLayout) findViewById(R.id.custom_filter_layout);
        customFilterLayout.setVisibility(customFilterChecked ? View.VISIBLE : View.GONE);
    }
}
