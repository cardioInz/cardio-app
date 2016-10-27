package cardio_app.viewmodel;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cardio_app.db.model.HealthParamsDateAndTime;

public class TableHealthRecord extends TableRow implements Comparable<TableHealthRecord> {

    public final int id;
    private HealthParamsDateAndTime hpdatRecord;
    private final TableHealthRecord self = this;

    TextView systoleTextView;
    TextView diastoleTextView;
    TextView conditionTextView;
    TextView pulseTextView;
    TextView arrhythmiaTextView;
    DateAndTimeView dateAndTimeView;

    public List<View> getListOfViews() {
        return listOfViews;
    }

    private List<View> listOfViews;

    public HealthParamsDateAndTime getHpdatRecord() {
        return hpdatRecord;
    }

    public TableHealthRecord(final Context context, HealthParamsDateAndTime hpdatInfo) {
        super(context);

        this.id = hpdatInfo.getId();
        this.hpdatRecord = hpdatInfo;

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.setLayoutParams(layoutParams);
        createFieldViews(context);
        this.setClickable(true);
        this.setOnClickListener(view -> Snackbar.make(view,
                "Selected row id: " + String.valueOf(self.id)
                //"Here we're gonna make some Edit and Delete buttons for selected measurement. " +
                        ,
                Snackbar.LENGTH_LONG
        ).setAction("Action", null).show());
    }

    public void reassignParamsToViews() {
        final String conditionSir = HealthCondition.classify(hpdatRecord.getHealthParams()).getStrMapped();

        systoleTextView.setText(hpdatRecord.getHealthParams().getSystole());
        diastoleTextView.setText(hpdatRecord.getHealthParams().getDiastole());
        conditionTextView.setText(conditionSir);
        pulseTextView.setText(hpdatRecord.getHealthParams().getPulse());
        arrhythmiaTextView.setText(hpdatRecord.getHealthParams().getArrhythmiaStr());

        dateAndTimeView.getDateTextView().setText(hpdatRecord.getDateAndTime().getDateStr());
        dateAndTimeView.getTimeTextView().setText(hpdatRecord.getDateAndTime().getTimeStr());
    }

    private void setParamsForGivenTextView(View view, float weight) {
        LayoutParams innerParams = new TableRow.LayoutParams(0, LayoutParams.MATCH_PARENT, Gravity.CENTER);
        innerParams.weight = weight;
        view.setPadding(5, 0, 5, 0);
        view.setLayoutParams(innerParams);
    }

    private void createFieldViews(Context context) {
        // add all views to list
        listOfViews = new ArrayList<>();
        listOfViews.add(systoleTextView = new TextView(context));
        listOfViews.add(diastoleTextView = new TextView(context));
        listOfViews.add(conditionTextView = new TextView(context));
        listOfViews.add(pulseTextView = new TextView(context));
        listOfViews.add(arrhythmiaTextView = new TextView(context));
        listOfViews.add(dateAndTimeView = new DateAndTimeView(context));

        // assign values from health params
        reassignParamsToViews();

        this.setWeightSum(1.0f); // sum of below weights
        setParamsForGivenTextView(systoleTextView, 0.15f);
        setParamsForGivenTextView(diastoleTextView, 0.15f);
        setParamsForGivenTextView(conditionTextView, 0.15f);
        setParamsForGivenTextView(pulseTextView, 0.15f);
        setParamsForGivenTextView(arrhythmiaTextView, 0.1f);
        setParamsForGivenTextView(dateAndTimeView, 0.30f);

        for (View view : listOfViews) {
            if (view instanceof TextView){
                ((TextView) view).setTextSize(18);
                ((TextView) view).setGravity(Gravity.END);
            }
        }

        // add views to row
        this.addView(systoleTextView);
        this.addView(diastoleTextView);
        this.addView(conditionTextView);
        this.addView(pulseTextView);
        this.addView(arrhythmiaTextView);
        this.addView(dateAndTimeView);
    }

    @Override
    public int compareTo(@NonNull TableHealthRecord other) {
        int r = this.hpdatRecord.compareTo(other.getHpdatRecord());
        if (r == 0){
            return Integer.valueOf(this.id).compareTo(other.getId());
        }
        return r;
    }
}