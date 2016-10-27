package cardio_app.viewmodel.diary;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cardio_app.db.model.HealthParams;

public class TableHealthRecord extends TableRow implements Comparable<TableHealthRecord> {

    public HealthParamsViewModel hpModelView;
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

    public TableHealthRecord(final Context context, HealthParamsViewModel hpModelView) {
        super(context);
        this.hpModelView = hpModelView;

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.setLayoutParams(layoutParams);
        createFieldViews(context);
        this.setClickable(true);
        this.setOnClickListener(view -> Snackbar.make(view,
                "Selected row id: " + String.valueOf(self.getId())
                //"Here we're gonna make some Edit and Delete buttons for selected measurement. " +
                        ,
                Snackbar.LENGTH_LONG
        ).setAction("Action", null).show());
    }

    public void reassignParamsToViews() {
        systoleTextView.setText(hpModelView.getSystole());
        diastoleTextView.setText(hpModelView.getDiastole());
        conditionTextView.setText(hpModelView.getConditionStr());
        pulseTextView.setText(hpModelView.getPulse());
        arrhythmiaTextView.setText(hpModelView.getArrhythmiaStr());

        dateAndTimeView.getDateTextView().setText(hpModelView.getDateStr());
        dateAndTimeView.getTimeTextView().setText(hpModelView.getTimeStr());
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

    public int getId() {
        return hpModelView.getId();
    }

    @Override
    public int compareTo(@NonNull TableHealthRecord other) {
        int r = this.hpModelView.compareTo(other.hpModelView);
        if (r == 0){
            return Integer.valueOf(this.getId()).compareTo(other.getId());
        }
        return r;
    }
}