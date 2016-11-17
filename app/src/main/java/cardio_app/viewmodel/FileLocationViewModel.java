package cardio_app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by kisam on 17.11.2016.
 */

public class FileLocationViewModel extends BaseObservable {

    private String fileLocation = "";
    private String fileName = "";

    public FileLocationViewModel() {

    }

    @Bindable
    public String getFileLocation() {
        if (fileLocation == null)
            return "";
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    @Bindable
    public String getFileName() {
        if (fileName == null)
            return "";
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
