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
import java.util.Collection;
import java.util.List;

import cardio_app.R;
import cardio_app.filtering_and_statistics.statistics_model.StatisticMeasure;
import cardio_app.viewmodel.statistics.StatisticMeasureViewModel;

/**
 * Created by kisam on 15.11.2016.
 */

public class StatisticMeasureAdapter extends ArrayAdapter<StatisticMeasure> {

    public StatisticMeasureAdapter(StatisticsLastMeasurementsActivity activity, List<StatisticMeasure> data) {
        super(activity, R.layout.diary_list_item, data);
    }

    public StatisticMeasureAdapter(StatisticsLastMeasurementsActivity activity, Collection<StatisticMeasure> values) {
        this(activity, new ArrayList(values));
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
        TextView dateTextView = (TextView) convertView.findViewById(R.id.measure_stat_dateTime);

        StatisticMeasure statisticMeasure = getItem(position);

        if (statisticMeasure != null) {
            StatisticMeasureViewModel viewModel = new StatisticMeasureViewModel(statisticMeasure);
            titleTextView.setText(viewModel.getTitle());

            try {
                valuesTextView.setText(viewModel.getValuesStr());
                arrhythmiaTableRow.setEnabled(statisticMeasure.isArrhythmiaImportant());
                arrhythmiaTableRow.setVisibility(statisticMeasure.isArrhythmiaImportant() ? View.VISIBLE : View.GONE);
                dateTextView.setText(viewModel.getDateTimeStr());
            } catch (Exception e) {
                TableRow valuesTableRow = (TableRow) convertView.findViewById(R.id.measure_stat_values_table_row);
                TableRow dateTimeTableRow = (TableRow) convertView.findViewById(R.id.measure_stat_datetime_table_row);

                valuesTableRow.setVisibility(View.GONE);
                dateTimeTableRow.setVisibility(View.GONE);
                arrhythmiaTableRow.setVisibility(View.GONE);

                TableRow noMatchingTableRow = (TableRow) convertView.findViewById(R.id.measure_stat_no_matching_table_row);
                noMatchingTableRow.setVisibility(View.VISIBLE);
            }
        }

        return convertView;
    }

}
