package cardio_app.activity.diary;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Comparator;
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
        TextView conditionTextView = (TextView) convertView.findViewById(R.id.pressuredata_condition);
        TextView arrhythmiaTextView = (TextView) convertView.findViewById(R.id.pressuredata_arrhythmia);
        TextView dateTimeTextView = (TextView) convertView.findViewById(R.id.pressuredata_datetime);

        PressureData pressureData = getItem(position);

        if (pressureData != null) {
            PressureDataViewModel viewModel = new PressureDataViewModel(pressureData);
//                valuesTextView.setText(viewModel.getValuesStr());
            systoleTextView.setText(viewModel.getSystoleStr());
            diastoleTextView.setText(viewModel.getDiastoleStr());
            pulseTextView.setText(viewModel.getPulseStr());
            conditionTextView.setText(viewModel.getConditionStr());
            arrhythmiaTextView.setText(viewModel.getArrhythmiaStr());
            dateTimeTextView.setText(viewModel.getDateTimeInTwoLinesStr());
        }

        return convertView;
    }

    static Comparator<PressureData> getComparator() {
        return PressureData.getComparator();
    }
}