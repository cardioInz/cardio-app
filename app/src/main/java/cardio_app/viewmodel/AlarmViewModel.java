package cardio_app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import cardio_app.BR;
import cardio_app.db.model.Alarm;

public class AlarmViewModel extends BaseObservable {
    private final Alarm alarm;
    private boolean checked;
    private final boolean initiallyChecked;

    public AlarmViewModel(Alarm alarm, boolean checked) {
        this.alarm = alarm;
        this.checked = checked;
        this.initiallyChecked = checked;
    }

    @Bindable
    public String getName() {
        return alarm.getName();
    }

    @Bindable
    public String getTime() {
        return String.format("%02d:%02d", alarm.getHour(), alarm.getMinute());
    }

    @Bindable
    public boolean isChecked() {
        return checked;
    }

    public Alarm getAlarm() {
        return alarm;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        notifyPropertyChanged(BR.checked);
    }

    public boolean checkedChanged() {
        return checked != initiallyChecked;
    }
}
