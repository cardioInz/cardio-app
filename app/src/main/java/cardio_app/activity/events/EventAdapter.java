package cardio_app.activity.events;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cardio_app.R;
import cardio_app.db.model.Event;
import cardio_app.viewmodel.EventDataViewModel;

public class EventAdapter extends ArrayAdapter<Event> {
    public EventAdapter(EventActivity eventActivity, List<Event> events) {
        super(eventActivity, R.layout.event_list_item, events);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.event_list_item, parent, false);
        }

        TextView startDateTextView = (TextView) convertView.findViewById(R.id.event_start_date);
        TextView endDateTextView = (TextView) convertView.findViewById(R.id.event_end_date);
        TextView descriptionTextView = (TextView) convertView.findViewById(R.id.event_description);
        TextView repeatTextView = (TextView) convertView.findViewById(R.id.event_repeat_info);


        Event event = getItem(position);

        if (event != null) {
            EventDataViewModel viewModel = new EventDataViewModel(event);
            startDateTextView.setText(viewModel.getStartDate());
            descriptionTextView.setText(viewModel.getDescription());
            endDateTextView.setText(viewModel.getEndDate());
            repeatTextView.setText(viewModel.getRepeatInfo());
        }

        return convertView;
    }
}
