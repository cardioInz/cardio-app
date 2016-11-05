package cardio_app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import cardio_app.BR;
import cardio_app.db.model.Drug;

public class DrugViewModel extends BaseObservable {
    private Drug drug;

    public DrugViewModel() {
        this(new Drug());
    }

    public DrugViewModel(Drug drug) {
        this.drug = drug;
    }

    public Drug getDrug() {
        return drug;
    }

    public void setDrug(Drug drug) {
        this.drug = drug;
        notifyPropertyChanged(BR.name);
        notifyPropertyChanged(BR.description);
    }

    @Bindable
    public String getName() {
        return drug.getName();
    }

    public void setName(String name) {
        drug.setName(name);
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getDescription() {
        return drug.getDescription();
    }

    public void setDescription(String description) {
        drug.setDescription(description);
        notifyPropertyChanged(BR.description);
    }
}
