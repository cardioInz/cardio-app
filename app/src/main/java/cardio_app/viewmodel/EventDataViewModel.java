package cardio_app.viewmodel;


import android.databinding.BaseObservable;
import android.databinding.Bindable;

import cardio_app.db.model.Event;
import cardio_app.db.model.TimeUnit;

public class EventDataViewModel extends BaseObservable {
    private Event event;

    private int tryToInt(String str) {
        if (str.isEmpty())
            return 0;

        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public EventDataViewModel () {event = new Event();}

    public EventDataViewModel(Event event) {
        this.event = event;
    }

    @Bindable
    public boolean getRepeatable() {
        return event.isRepeatable();
    }

    public void setRepeatable(boolean isRepeatable) {
        event.setRepeatable(isRepeatable);
    }

    @Bindable
    public String getTimeUnit() {return event.getTimeUnit().name();}

    public void setTimeUnit(String timeUnit) {
        event.setTimeUnit(TimeUnit.valueOf(timeUnit));
    }

    @Bindable
    public boolean isDay() {return event.getTimeUnit().equals(TimeUnit.DAY);}

    public void setDay(boolean day) {
        event.setTimeUnit(TimeUnit.DAY);
    }

    @Bindable
    public boolean isWeek() {return event.getTimeUnit().equals(TimeUnit.WEEK);}

    public void setWeek(boolean week) {
        event.setTimeUnit(TimeUnit.WEEK);
    }

    @Bindable
    public boolean isMonth() {return event.getTimeUnit().equals(TimeUnit.MONTH);}

    public void setMonth(boolean month) {
        event.setTimeUnit(TimeUnit.MONTH);
    }


    @Bindable
    public String getDescription() {return event.getDescription();};

    public void setDescription (String description) {
        event.setDescription(description);
    }

    @Bindable
    public String getTimeDelta() {return String.valueOf(event.getTimeDelta());}

    public void setTimeDelta(String timeDelta) {event.setTimeDelta(tryToInt(timeDelta));}

    @Bindable
    public boolean getAlarmSet() {return event.isAlarmSet();}

    public void setAlarmSet(boolean isAlarmSet) {
        event.setAlarmSet(isAlarmSet);
    }

    public Event getEvent() {return event;}

    public void setEvent(Event event) {
        this.event = event;
    }

    @Bindable
    public String getStartDate(){
        return event.getStartDate().getDay() + "." +
                event.getStartDate().getMonth() + "." +
                event.getStartDate().getYear();
    }

    @Bindable
    public String getEndDate(){
        return event.getEndDate().getDay() + "." +
                event.getEndDate().getMonth() + "." +
                event.getEndDate().getYear();
    }

    @Bindable
    public String getRepeatInfo() {
        if(event.isRepeatable()) {
            return "Repeats every " + this.getTimeDelta()+ " " + event.getTimeUnit().name().toLowerCase() + "s";
        } else
            return "";
    }
}
