package cardio_app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class TitleViewModel extends BaseObservable {
    private String title;

    public TitleViewModel(String title) {
        this.title = title;
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
