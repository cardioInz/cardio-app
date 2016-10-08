package cardio_app.structures.table_row;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
    private final TableHealthRecord self = this;

    TextView systoleTextView;
    TextView diastoleTextView;
    TextView conditionTextView;
    TextView pulseTextView;
    TextView arrhythmiaTextView;
    DateTimeView dateTimeView;



    public List<View> getListOfViews() {
        return listOfViews;
    }

    private List<View> listOfViews;


    public TableHealthRecord(final Context context, HealthParams params) {
        super(context);

        this.id = ++ID_CNT;
        this.healthParams = params;

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.setLayoutParams(layoutParams);
        createFieldViews(context);
        this.setClickable(true);
        this.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,
                        "Selected row id: " + String.valueOf(self.id)
                        //"Here we're gonna make some Edit and Delete buttons for selected measurement. " +
                                ,
                        Snackbar.LENGTH_LONG
                ).setAction("Action", null).show();
            }
        });
    }


    public void reassignParamsToViews() {
        systoleTextView.setText(healthParams.getSystole());
        diastoleTextView.setText(healthParams.getDiastole());
        conditionTextView.setText(healthParams.getConditionStr());
        pulseTextView.setText(healthParams.getPulse());
        arrhythmiaTextView.setText(healthParams.getArrhythmiaStr());
        dateTimeView.getDateTextView().setText(healthParams.getDateStr());
        dateTimeView.getTimeTextView().setText(healthParams.getTimeStr());
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
        listOfViews.add(dateTimeView = new DateTimeView(context));

        // assign values from health params
        reassignParamsToViews();

        this.setWeightSum(1.0f); // sum of below weights
        setParamsForGivenTextView(systoleTextView, 0.15f);
        setParamsForGivenTextView(diastoleTextView, 0.15f);
        setParamsForGivenTextView(conditionTextView, 0.15f);
        setParamsForGivenTextView(pulseTextView, 0.15f);
        setParamsForGivenTextView(arrhythmiaTextView, 0.1f);
        setParamsForGivenTextView(dateTimeView, 0.30f);

        for (View view : listOfViews) {
            if (view instanceof TextView){
                ((TextView) view).setTextSize(18);
                ((TextView) view).setGravity(Gravity.END);
            }
        }

        //add views to row
        this.addView(systoleTextView);
        this.addView(diastoleTextView);
        this.addView(conditionTextView);
        this.addView(pulseTextView);
        this.addView(arrhythmiaTextView);
        this.addView(dateTimeView);
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