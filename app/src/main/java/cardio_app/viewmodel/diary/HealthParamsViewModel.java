package cardio_app.viewmodel.diary;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import cardio_app.db.model.HealthParams;

/**
 * Created by kisam on 27.10.2016.
 */

public class HealthParamsViewModel extends BaseObservable implements Comparable<HealthParamsViewModel> {
    private HealthParams healthParams;

    public HealthParamsViewModel(HealthParams healthParams){
        this.healthParams = healthParams;
    }

    @Bindable
    public String getConditionStr(){
        return HealthCondition.classify(this.healthParams).getStrMapped();
    }

    @Bindable
    public String getDateStr() {
        return healthParams.getDateStr();
    }

    @Bindable
    public String getTimeStr() {
        return healthParams.getTimeStr();
    }

    @Bindable
    public String getSystole() {
        return healthParams.getSystole();
    }

    @Bindable
    public String getDiastole() {
        return healthParams.getDiastole();
    }

    @Bindable
    public String getPulse() {
        return healthParams.getPulse();
    }

    @Bindable
    public String getArrhythmiaStr() {
        return healthParams.isArrhythmia() ? "A" : "-";
    }

    @Bindable
    public int getId() { return healthParams.getId(); }

    @Override
    public int compareTo(@NonNull HealthParamsViewModel healthParamsViewModel) {
        return this.healthParams.compareTo(healthParamsViewModel.healthParams);
    }
}
