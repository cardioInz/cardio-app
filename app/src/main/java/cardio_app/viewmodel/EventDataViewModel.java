package cardio_app.viewmodel;


import android.databinding.BaseObservable;
import android.databinding.Bindable;

import cardio_app.db.model.Emotion;
import cardio_app.db.model.Event;
import cardio_app.db.model.TimeUnit;
import cardio_app.util.DateTimeUtil;

public class EventDataViewModel extends BaseObservable {
    private Event event;

    public EventDataViewModel() {
        event = new Event();
    }

    public EventDataViewModel(Event event) {
        this.event = event;
    }

    private int tryToInt(String str) {
        if (str.isEmpty())
            return 0;

        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Bindable
    public boolean getRepeatable() {
        return event.isRepeatable();
    }

    public void setRepeatable(boolean isRepeatable) {
        event.setRepeatable(isRepeatable);
    }

    @Bindable
    public String getTimeUnit() {
        return event.getTimeUnit().name();
    }

    public void setTimeUnit(String timeUnit) {
        event.setTimeUnit(TimeUnit.valueOf(timeUnit));
    }

    @Bindable
    public boolean isDay() {
        return event.getTimeUnit().equals(TimeUnit.DAY);
    }

    public void setDay(boolean day) {
        event.setTimeUnit(TimeUnit.DAY);
    }

    @Bindable
    public boolean isWeek() {
        return event.getTimeUnit().equals(TimeUnit.WEEK);
    }

    public void setWeek(boolean week) {
        event.setTimeUnit(TimeUnit.WEEK);
    }

    @Bindable
    public boolean isMonth() {
        return event.getTimeUnit().equals(TimeUnit.MONTH);
    }

    public void setMonth(boolean month) {
        event.setTimeUnit(TimeUnit.MONTH);
    }


    @Bindable
    public String getDescription() {
        return event.getDescription();
    }

    ;

    public void setDescription(String description) {
        event.setDescription(description);
    }

    @Bindable
    public String getTimeDelta() {
        return String.valueOf(event.getTimeDelta());
    }

    public void setTimeDelta(String timeDelta) {
        event.setTimeDelta(tryToInt(timeDelta));
    }

    @Bindable
    public boolean getAlarmSet() {
        return event.isAlarmSet();
    }

    public void setAlarmSet(boolean isAlarmSet) {
        event.setAlarmSet(isAlarmSet);
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getStartDate() {
        return DateTimeUtil.DATE_FORMATTER.format(event.getStartDate());
    }

    @Bindable
    public String getStartTime() {
        return DateTimeUtil.TIME_FORMATTER.format(event.getStartDate());
    }

    public String getEndDate() {
        if (!event.isStartDateEqualEndDate()) {
            return " - " + DateTimeUtil.DATE_FORMATTER.format(event.getEndDate());
        } else
            return "";
    }

    @Bindable
    public String getDate() {
        return getStartDate() + getEndDate();
    }

    @Bindable
    public String getRepeatInfo() {
        return "Every " + this.getTimeDelta() + " " + event.getTimeUnit().name().toLowerCase() + "s";
    }

    public Emotion getEmotion() {
        return event.getEmotion();
    }
}
