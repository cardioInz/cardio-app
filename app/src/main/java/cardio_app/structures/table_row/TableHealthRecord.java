package cardio_app.structures.table_row;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TableHealthRecord extends TableRow implements Comparable<TableHealthRecord> {

    private static long ID_CNT = 0;
    public final long id;
    private HealthParams healthParams;

    TextView systoleTextView;
    TextView diastoleTextView;
    TextView conditionTextView;
    TextView pulseTextView;
    TextView arrhythmiaTextView;
    TextView dateTextView;
    TextView timeTextView;

    public List<View> getListOfViews() {
        return listOfViews;
    }

    private List<View> listOfViews;


    public TableHealthRecord(AppCompatActivity activity, HealthParams params) {
        super(activity);

        this.id = ++ID_CNT;
        this.healthParams = params;

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.setLayoutParams(layoutParams);
        createFieldViews(activity);

        //add views to row
        this.addView(systoleTextView);
        this.addView(diastoleTextView);
        this.addView(conditionTextView);
        this.addView(pulseTextView);
        this.addView(arrhythmiaTextView);
        this.addView(dateTextView);
        this.addView(timeTextView);
    }

    public void reassignParamsToViews() {
        systoleTextView.setText(healthParams.getSystole());
        diastoleTextView.setText(healthParams.getDiastole());
        conditionTextView.setText(healthParams.getConditionStr());
        pulseTextView.setText(healthParams.getPulse());
        arrhythmiaTextView.setText(healthParams.getArrhythmiaStr());
        dateTextView.setText(healthParams.getDateStr());
        timeTextView.setText(healthParams.getTimeStr());
    }

    private LayoutParams prepareInnerLayoutParams(float weight){
        LayoutParams innerParams = new TableRow.LayoutParams(0, LayoutParams.MATCH_PARENT, Gravity.CENTER);
        innerParams.weight = weight;
        return innerParams;
    }

    private void createFieldViews(AppCompatActivity activity) {
        // add all views to list
        listOfViews = new ArrayList<>();
        listOfViews.add(systoleTextView = new TextView(activity));
        listOfViews.add(diastoleTextView = new TextView(activity));
        listOfViews.add(conditionTextView = new TextView(activity));
        listOfViews.add(pulseTextView = new TextView(activity));
        listOfViews.add(arrhythmiaTextView = new TextView(activity));
        listOfViews.add(dateTextView = new TextView(activity));
        listOfViews.add(timeTextView = new TextView(activity));

        // assign values from health params
        reassignParamsToViews();

        this.setWeightSum(1.0f); // sum of below weights
        systoleTextView.setLayoutParams(prepareInnerLayoutParams(0.12f));
        diastoleTextView.setLayoutParams(prepareInnerLayoutParams(0.12f));
        conditionTextView.setLayoutParams(prepareInnerLayoutParams(0.12f));
        pulseTextView.setLayoutParams(prepareInnerLayoutParams(0.12f));;
        arrhythmiaTextView.setLayoutParams(prepareInnerLayoutParams(0.07f));
        dateTextView.setLayoutParams(prepareInnerLayoutParams(0.30f));
        timeTextView.setLayoutParams(prepareInnerLayoutParams(0.15f));
    }

    @Override
    public int compareTo(@NonNull TableHealthRecord other) {
        if (this.id == other.id)
            return 0;
        else if (this.id < other.id)
            return -1;
        else
            return 1;
    }
}