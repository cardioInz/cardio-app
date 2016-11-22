package cardio_app.pdf_creation.param_models;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import cardio_app.db.model.BaseModel;
import lecho.lib.hellocharts.view.AbstractChartView;
import lecho.lib.hellocharts.view.LineChartView;

import static android.content.ContentValues.TAG;

/**
 * Created by kisam on 19.11.2016.
 */

public class PdfCreationDataParam extends BaseModel {
    private String locationSave;
    private String fileName;
    private boolean isSendEmailOpt;
    private String emailAddr;
    private List<ImageFromChart> listOfImages = new ArrayList<>();

    public PdfCreationDataParam() {

    }

    public void sortImages(){
        Collections.sort(listOfImages, ImageFromChart.getComparator());
    }

    public void addImageFromChart(LineChartView chartView){
        ImageFromChart imageFromChart = new ImageFromChart(chartView);
        if (imageFromChart.hasCompletedValues())
            listOfImages.add(imageFromChart);
        else
            Log.w(TAG, "addImageFromChart: has not completed values");
    }

    public String getLocationSave() {
        return locationSave;
    }

    public void setLocationSave(String locationSave) {
        this.locationSave = locationSave;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isSendEmailOpt() {
        return isSendEmailOpt;
    }

    public void setSendEmailOpt(boolean sendEmailOpt) {
        isSendEmailOpt = sendEmailOpt;
    }

    public String getEmailAddr() {
        return emailAddr;
    }

    public void setEmailAddr(String emailAddr) {
        this.emailAddr = emailAddr;
    }
}
