package cardio_app.activity.diary;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cardio_app.R;
import cardio_app.db.model.PressureData;
import cardio_app.viewmodel.PressureDataViewModel;


public class PressureDataAdapter extends ArrayAdapter<PressureData> {

    public PressureDataAdapter(DiaryActivity activity, List<PressureData> data) {
        super(activity, R.layout.diary_list_item, data);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.diary_list_item, parent, false);
        }

//            TextView valuesTextView = (TextView) convertView.findViewById(R.id.pressuredata_values);
        TextView systoleTextView = (TextView) convertView.findViewById(R.id.pressuredata_systole);
        TextView diastoleTextView = (TextView) convertView.findViewById(R.id.pressuredata_diastole);
        TextView pulseTextView = (TextView) convertView.findViewById(R.id.pressuredata_pulse);
        ImageView arrhythmiaImageView = (ImageView) convertView.findViewById(R.id.arrhythmia_image);
        TextView dateTimeTextView = (TextView) convertView.findViewById(R.id.pressuredata_datetime);
//        TextView dateTiTextView = (TextView) convertView.findViewById(R.id.pressuredata_date);
//        TextView timeTextView = (TextView) convertView.findViewById(R.id.pressuredata_time);
        LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.layout_pressure_list_item);

        PressureData pressureData = getItem(position);

        if (pressureData != null) {
            PressureDataViewModel viewModel = new PressureDataViewModel(pressureData);
            systoleTextView.setText(viewModel.getSystoleStr());
            diastoleTextView.setText(viewModel.getDiastoleStr());
            pulseTextView.setText(viewModel.getPulseStr());
            arrhythmiaImageView.setVisibility(viewModel.getArrhythmia() ? View.VISIBLE : View.INVISIBLE);
            dateTimeTextView.setText(viewModel.getDateTimeInTwoLinesStr());
//            dateTextView.setText(viewModel.getDateStr());
//            timeTextView.setText(viewModel.getTimeStr());
            if (viewModel.getConditionStr().equals("0") ||
                    viewModel.getConditionStr().equals("1") ||
                    viewModel.getConditionStr().equals("2")) {
                linearLayout.setBackgroundResource(R.drawable.low_pressure);
            } else if (viewModel.getConditionStr().equals("3") ||
                    viewModel.getConditionStr().equals("4") ||
                    viewModel.getConditionStr().equals("5")) {
                linearLayout.setBackgroundResource(R.drawable.normal_pressure);
            } else if (viewModel.getConditionStr().equals("6") ||
                    viewModel.getConditionStr().equals("7") ||
                    viewModel.getConditionStr().equals("8")) {
                linearLayout.setBackgroundResource(R.drawable.high_pressure);
            }

        }


        return convertView;
    }
}