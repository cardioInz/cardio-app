package cardio_app.activity.statistics;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import cardio_app.R;
import cardio_app.statistics.StatisticMeasure;
import cardio_app.statistics.StatisticMeasureTypeEnum;
import cardio_app.viewmodel.statistics.StatisticMeasureViewModel;

/**
 * Created by kisam on 15.11.2016.
 */

public class StatisticMeasureAdapter extends ArrayAdapter<StatisticMeasure> {

    private StatisticMeasureAdapter(StatisticsLastMeasurementsActivity activity, List<StatisticMeasure> data) {
        super(activity, R.layout.diary_list_item, data);
    }

    StatisticMeasureAdapter(StatisticsLastMeasurementsActivity activity, HashMap<StatisticMeasureTypeEnum, StatisticMeasure> map) {
        this(activity, getListOrderedFromMap(map));
    }

    private static List<StatisticMeasure> getListOrderedFromMap(HashMap<StatisticMeasureTypeEnum, StatisticMeasure> map){
        List<StatisticMeasure> list = new ArrayList<>();
        List<StatisticMeasureTypeEnum> keys = new ArrayList<>(map.keySet());
        Collections.sort(keys);
        for (StatisticMeasureTypeEnum key : keys) {
            list.add(map.get(key));
        };
        return list;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.statistics_measure_list_item, parent, false);
        }

        TextView titleTextView = (TextView) convertView.findViewById(R.id.measure_stat_title);
        TextView valuesTextView = (TextView) convertView.findViewById(R.id.measure_stat_values);
        TableRow arrhythmiaTableRow = (TableRow) convertView.findViewById(R.id.measure_stat_arrhythmia_table_row);
        TextView dateTextView = (TextView) convertView.findViewById(R.id.measure_stat_date);
        TextView timeTextView = (TextView) convertView.findViewById(R.id.measure_stat_time);

        StatisticMeasure statisticMeasure = getItem(position);

        if (statisticMeasure != null) {
            StatisticMeasureViewModel viewModel = new StatisticMeasureViewModel(statisticMeasure);
            titleTextView.setText(viewModel.getTitle());

            try {
                nullPtrFound(convertView, false);
                valuesTextView.setText(viewModel.getValuesStr());
                arrhythmiaTableRow.setEnabled(statisticMeasure.isArrhythmiaImportant());
                arrhythmiaTableRow.setVisibility(viewModel.shouldShowArrhythmia() ? View.VISIBLE : View.GONE);
                dateTextView.setText(viewModel.getDateStr());
                timeTextView.setText(viewModel.getTimeStr());
            } catch (Exception e) {
                nullPtrFound(convertView, true);
            }
        } else {
            nullPtrFound(convertView, true);
        }

        return convertView;
    }

    private void nullPtrFound(View convertView, boolean isFound){
        TableRow valuesTableRow = (TableRow) convertView.findViewById(R.id.measure_stat_values_table_row);
        TableRow dateTableRow = (TableRow) convertView.findViewById(R.id.measure_stat_date_table_row);
        TableRow timeTableRow = (TableRow) convertView.findViewById(R.id.measure_stat_time_table_row);
        TableRow arrhythmiaTableRow = (TableRow) convertView.findViewById(R.id.measure_stat_arrhythmia_table_row);

        valuesTableRow.setVisibility(isFound ? View.GONE : View.VISIBLE);
        dateTableRow.setVisibility(isFound ? View.GONE : View.VISIBLE);
        timeTableRow.setVisibility(isFound ? View.GONE : View.VISIBLE);
        arrhythmiaTableRow.setVisibility(isFound ? View.GONE : View.VISIBLE);

        TableRow noMatchingTableRow = (TableRow) convertView.findViewById(R.id.measure_stat_no_matching_table_row);
        noMatchingTableRow.setVisibility(isFound ? View.VISIBLE : View.GONE);
    }

}
