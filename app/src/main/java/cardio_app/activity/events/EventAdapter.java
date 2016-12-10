package cardio_app.activity.events;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itextpdf.text.pdf.parser.Line;

import java.util.ArrayList;
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
        TextView dateTextView = (TextView) convertView.findViewById(R.id.event_date);
        TextView descriptionTextView = (TextView) convertView.findViewById(R.id.event_description);
        TextView repeatTextView = (TextView) convertView.findViewById(R.id.event_repeat_info);
        TextView startTimeTextView = (TextView) convertView.findViewById(R.id.event_start_time);
        ImageView emotionImageView = (ImageView) convertView.findViewById(R.id.events_list_emotion_icon);
        TextView emotionTextView = (TextView) convertView.findViewById(R.id.events_list_emotion_text);
        ImageView eventAlarmImageView = (ImageView) convertView.findViewById(R.id.events_list_alarm);
        ImageButton showMoreImageView = (ImageButton) convertView.findViewById(R.id.event_show_more);
        ImageButton showLessImageView = (ImageButton) convertView.findViewById(R.id.event_show_less);
        LinearLayout otherTypeLinearLayout = (LinearLayout) convertView.findViewById(R.id.events_list_details_other);
        ImageView otherTypeImageView = (ImageView) convertView.findViewById(R.id.events_list_other_icon);
        TextView otherTypeTextView = (TextView) convertView.findViewById(R.id.events_list_other_text);
        LinearLayout medLinearLayout = (LinearLayout) convertView.findViewById(R.id.events_list_details_med);
        ImageView medImageView = (ImageView) convertView.findViewById(R.id.events_list_med_icon);
        TextView medTypeTextView = (TextView) convertView.findViewById(R.id.events_list_med_text);
        LinearLayout symptomsLinearLayout = (LinearLayout) convertView.findViewById(R.id.events_list_details_symptoms);
        ImageView symptomsImageView = (ImageView) convertView.findViewById(R.id.events_list_symptoms_icon);
        TextView symptomsTypeTextView = (TextView) convertView.findViewById(R.id.events_list_symptoms_text);
        LinearLayout detailsLinearLayout = (LinearLayout) convertView.findViewById(R.id.events_list_details);



        Event event = getItem(position);

        if (event != null) {
            EventDataViewModel viewModel = new EventDataViewModel(event);
            dateTextView.setText(viewModel.getDate());
            descriptionTextView.setText(viewModel.getDescription());
            repeatTextView.setText(viewModel.getRepeatInfo());
            repeatTextView.setVisibility(viewModel.getRepeatable() ? View.VISIBLE : View.INVISIBLE);
            startTimeTextView.setText(viewModel.getStartTime());
            detailsLinearLayout.setVisibility(View.GONE);
            showLessImageView.setVisibility(View.GONE);


          initializeEmotionSection(emotionImageView, emotionTextView, viewModel);
          initializeOtherSymptomsSection(symptomsImageView, symptomsTypeTextView, symptomsLinearLayout, viewModel, getContext());
          initializeDoctorsAppointmentSection(medImageView, medTypeTextView, medLinearLayout, viewModel, getContext());
          initializeDailyActivitiesRecordSection(otherTypeImageView, otherTypeTextView, otherTypeLinearLayout, viewModel);


            eventAlarmImageView.setVisibility(viewModel.getAlarmSet() ? View.VISIBLE : View.INVISIBLE);

            showMoreImageView.setOnClickListener( view -> {
                detailsLinearLayout.setVisibility(View.VISIBLE);
                showLessImageView.setVisibility(View.VISIBLE);
                showMoreImageView.setVisibility(View.GONE);
            });

            showLessImageView.setOnClickListener( view -> {
                    detailsLinearLayout.setVisibility(View.GONE);
                    showLessImageView.setVisibility(View.GONE);
                    showMoreImageView.setVisibility(View.VISIBLE);
            });
        }

        return convertView;
    }

    private void initializeEmotionSection(ImageView emotionImageView, TextView emotionTextView, EventDataViewModel vm){
        if(vm.getEvent().getEmotion() != null &&
                !vm.getEmotion().equals(Emotion.NONE)) {
            Integer emotionImageId = EmotionHelper.getImageId(vm.getEmotion());
            emotionImageView.setImageResource(emotionImageId);
            emotionTextView.setText(EmotionHelper.getDescription(vm.getEmotion()));
        } else {
            emotionImageView.setVisibility(View.INVISIBLE);
            emotionTextView.setVisibility(View.INVISIBLE);
        }
    }

    private void initializeDailyActivitiesRecordSection(ImageView otherTypeImageView, TextView otherTypeTextView, LinearLayout otherTypeLinearLayout,
                                                        EventDataViewModel vm){
        if(vm.getEvent().getDailyActivitiesRecord() != null &&
                !vm.getEvent().getDailyActivitiesRecord().equals(DailyActivitiesRecord.NONE)){
            Integer otherTypeImageId = DailyActivitiesRecordHelper.getImageId(vm.getEvent().getDailyActivitiesRecord());
            otherTypeImageView.setImageResource(otherTypeImageId);
            otherTypeTextView.setText(DailyActivitiesRecordHelper.getDescription(vm.getEvent().getDailyActivitiesRecord()));
        } else {
            otherTypeLinearLayout.setVisibility(View.GONE);
        }
    }

    private void initializeDoctorsAppointmentSection(ImageView medImageView, TextView medTypeTextView, LinearLayout medLinearLayout,
                                                     EventDataViewModel vm, Context context){
        if(vm.getEvent().getDoctorsAppointment() != null &&
         vm.getEvent().getDoctorsAppointment().isDoctorsAppointment()){
            medImageView.setImageResource(R.drawable.medical_checkout);
            String visitDescription = getDetailsDescription(vm.getEvent().getDoctorsAppointment().getDoctorsAppointmentElements(), context);
            medTypeTextView.setText(visitDescription);
        } else {
            medLinearLayout.setVisibility(View.GONE);
        }
    }

    private void initializeOtherSymptomsSection(ImageView symptomsImageView, TextView symptomsTypeTextView, LinearLayout symptomsLinearLayout,
                                                EventDataViewModel vm, Context context) {
        if(vm.getEvent().getOtherSymptomsRecord()!= null &&
                vm.getEvent().getOtherSymptomsRecord().isOtherSymptom()){
            symptomsImageView.setImageResource(R.drawable.cough);
            String symptomsDescription = getDetailsDescription(vm.getEvent().getOtherSymptomsRecord().getOtherSymptoms(), context);
            symptomsTypeTextView.setText(symptomsDescription);
        } else {
            symptomsLinearLayout.setVisibility(View.GONE);
        }
    }

    private String getDetailsDescription(ArrayList<Integer> detailsElements, Context context) {
        String description = "";
        for(int i=0; i<detailsElements.size(); i++) {
            description += context.getResources().getString(detailsElements.get(i));
            description+= i == detailsElements.size()-1 ? "" : ", ";
        }
        return description;
    }
}
