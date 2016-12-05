package cardio_app.activity.events;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cardio_app.R;
import cardio_app.db.model.DailyActivitiesRecord;
import cardio_app.db.model.DailyActivitiesRecordHelper;
import cardio_app.db.model.Emotion;
import cardio_app.db.model.EmotionHelper;
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
//        TextView dateTextView = (TextView) convertView.findViewById(R.id.event_date);
        TextView descriptionTextView = (TextView) convertView.findViewById(R.id.event_description);
//        TextView repeatTextView = (TextView) convertView.findViewById(R.id.event_repeat_info);
        TextView startTimeTextView = (TextView) convertView.findViewById(R.id.event_start_time);
//        ImageView emotionImageView = (ImageView) convertView.findViewById(R.id.events_list_emotion_icon);
//        TextView emotionTextView = (TextView) convertView.findViewById(R.id.events_list_emotion_text);
        ImageView eventAlarmImageView = (ImageView) convertView.findViewById(R.id.events_list_alarm);
//        ImageButton showMoreImageView = (ImageButton) convertView.findViewById(R.id.event_show_more);
//        LinearLayout otherTypeLinearLayout = (LinearLayout) convertView.findViewById(R.id.events_list_details_other);
//        ImageView otherTypeImageView = (ImageView) convertView.findViewById(R.id.events_list_other_icon);
//        TextView otherTypeTextView = (TextView) convertView.findViewById(R.id.events_list_other_text);
//        LinearLayout medLinearLayout = (LinearLayout) convertView.findViewById(R.id.events_list_details_med);
//        ImageView medImageView = (ImageView) convertView.findViewById(R.id.events_list_med_icon);
//        TextView medTypeTextView = (TextView) convertView.findViewById(R.id.events_list_med_text);
//        LinearLayout symptomsLinearLayout = (LinearLayout) convertView.findViewById(R.id.events_list_details_symptoms);
//        ImageView symptomsImageView = (ImageView) convertView.findViewById(R.id.events_list_symptoms_icon);
//        TextView symptomsTypeTextView = (TextView) convertView.findViewById(R.id.events_list_symptoms_text);
//        LinearLayout detailsLinearLayout = (LinearLayout) convertView.findViewById(R.id.events_list_details);



        Event event = getItem(position);

        if (event != null) {
            EventDataViewModel viewModel = new EventDataViewModel(event);
//            dateTextView.setText(viewModel.getDate());
            descriptionTextView.setText(viewModel.getDescription());
//            repeatTextView.setText(viewModel.getRepeatInfo());
//            repeatTextView.setVisibility(viewModel.getRepeatable() ? View.VISIBLE : View.INVISIBLE);
            startTimeTextView.setText(viewModel.getStartTime());
//            detailsLinearLayout.setVisibility(View.GONE);

//            if(!viewModel.getEmotion().equals(Emotion.NONE)) {
//                Integer emotionImageId = EmotionHelper.getImageId(viewModel.getEmotion());
//                emotionImageView.setImageResource(emotionImageId);
//                emotionTextView.setText(viewModel.getEmotion().name().toLowerCase());
//            } else {
//                emotionImageView.setVisibility(View.INVISIBLE);
//                emotionTextView.setVisibility(View.INVISIBLE);
//            }
//
//            if(!viewModel.getEvent().getDailyActivitiesRecord().equals(DailyActivitiesRecord.NONE)){
//                Integer otherTypeImageId = DailyActivitiesRecordHelper.getImageId(viewModel.getEvent().getDailyActivitiesRecord());
//                otherTypeImageView.setImageResource(otherTypeImageId);
//                otherTypeTextView.setText(DailyActivitiesRecordHelper.getDescription(viewModel.getEvent().getDailyActivitiesRecord()));
//            } else {
//               otherTypeLinearLayout.setVisibility(View.GONE);
//            }
//
//            if(viewModel.getEvent().getDoctorsAppointment().isDoctorsAppointment()){
//                medImageView.setImageResource(R.drawable.medical_checkout);
//                medTypeTextView.setText(viewModel.getEvent().getDoctorsAppointment().getDoctorsAppointmentDescription());
//            } else {
//                medLinearLayout.setVisibility(View.GONE);
//            }
//
//            if(viewModel.getEvent().getOtherSymptomsRecord().isOtherSymptom()){
//                symptomsImageView.setImageResource(R.drawable.cough);
//                symptomsTypeTextView.setText(viewModel.getEvent().getOtherSymptomsRecord().getOtherSymptomsDescription());
//            } else {
//                symptomsLinearLayout.setVisibility(View.GONE);
//            }

            eventAlarmImageView.setVisibility(viewModel.getAlarmSet() ? View.VISIBLE : View.INVISIBLE);

//            showMoreImageView.setOnClickListener( view -> {
//                if(detailsLinearLayout.getVisibility() == View.VISIBLE){
//                    detailsLinearLayout.setVisibility(View.GONE);
//                    showMoreImageView.setImageResource(R.drawable.show_less);
//                } else {
//                    detailsLinearLayout.setVisibility(View.VISIBLE);
//                    showMoreImageView.setImageResource(R.drawable.show_more);
//                }
//            });
        }

        return convertView;
    }
}
