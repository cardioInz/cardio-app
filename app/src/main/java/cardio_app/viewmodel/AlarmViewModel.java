package cardio_app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmViewModel extends BaseObservable {
    private final Alarm alarm;
    private Date date;

    public AlarmViewModel(Alarm alarm) {
        this.alarm = alarm;
        this.date = new Date(2001, 8, 11, alarm.getHour(), alarm.getMinute());
    }

    @Bindable
    public String getName() {
        return alarm.getName();
    }

    public void setName(String name) {
        alarm.setName(name);
    }

    @Bindable
    public String getDescription() {
        return alarm.getDescription();
    }

    public void setDescription(String description) {
        alarm.setDescription(description);
    }

    @Bindable
    public String getTime() {
        return SimpleDateFormat.getTimeInstance().format(date);
    }

    public Alarm getAlarm() {
        return alarm;
    }

    @Bindable
    public int getHour() {
        return alarm.getHour();
    }

    @Bindable
    public int getMinute() {
        return alarm.getMinute();
    }

    public void setHour(int hour) {
        alarm.setHour(hour);
    }

    public void setMinute(int minute) {
        alarm.setMinute(minute);
    }
}
