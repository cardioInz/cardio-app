package cardio_app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class ImportExportViewModel extends BaseObservable {
    private String path;
    private String fileName;

    public ImportExportViewModel() {

    }

    public ImportExportViewModel(String path, String fileName) {
        this.path = path;
        this.fileName = fileName;
    }

    @Bindable
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Bindable
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
