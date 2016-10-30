package cardio_app.viewmodel.pressure;


import android.content.Context;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class DateAndTimeView extends TableLayout {

    private TextView dateTextView;
    private TextView timeTextView;

    public DateAndTimeView(Context context) {
        super(context);
        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(0, TableLayout.LayoutParams.MATCH_PARENT, Gravity.END);
        this.setLayoutParams(layoutParams);
        this.setGravity(Gravity.END);

        TableRow trDate = new TableRow(this.getContext());
        trDate.addView(dateTextView = new TextView(context));
        trDate.setGravity(Gravity.END);
        dateTextView.setGravity(Gravity.END);
        dateTextView.setTextSize(11);
        this.addView(trDate);

        TableRow trTime = new TableRow(this.getContext());
        trTime.addView(timeTextView = new TextView(context));
        trTime.setGravity(Gravity.END);
        timeTextView.setGravity(Gravity.END);
        timeTextView.setTextSize(9);
        this.addView(trTime);
    }

    public void setWeight(float weight) {
        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(0, TableLayout.LayoutParams.MATCH_PARENT, Gravity.END);
        this.setLayoutParams(layoutParams);
        layoutParams.weight = weight;
        this.setLayoutParams(layoutParams);
    }

    public TextView getDateTextView() {
        return dateTextView;
    }

    public void setDateTextView(TextView dateTextView) {
        this.dateTextView = dateTextView;
    }

    public TextView getTimeTextView() {
        return timeTextView;
    }

    public void setTimeTextView(TextView timeTextView) {
        this.timeTextView = timeTextView;
    }
}