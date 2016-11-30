package cardio_app.activity.pdf_creation;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cardio_app.R;
import cardio_app.pdf_creation.param_models.BitmapFromChart;


public class BitmapFromChartDataAdapter extends ArrayAdapter<BitmapFromChart> {

    public BitmapFromChartDataAdapter(CollectedChartsActivity activity, List<BitmapFromChart> data) {
        super(activity, R.layout.diary_list_item, data);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.collected_charts_list_item, parent, false);
        }

        TextView imgNameTextView = (TextView) convertView.findViewById(R.id.image_name_text_view);

        BitmapFromChart pressureData = getItem(position);

        if (pressureData != null) {
            imgNameTextView.setText(pressureData.getFileName());
        }

        return convertView;
    }


}
