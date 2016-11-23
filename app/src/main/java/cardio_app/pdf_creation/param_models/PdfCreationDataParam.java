package cardio_app.pdf_creation.param_models;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cardio_app.db.model.BaseModel;
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
    private List<BitmapFromChart> listOfImages = new ArrayList<>();

    public PdfCreationDataParam() {

    }

    public void sortImages(){
        Collections.sort(listOfImages, BitmapFromChart.getComparator());
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

    public List<BitmapFromChart> listOfBitmapsFromCharts(){
        return listOfImages;
    }

    public void setListOfImages(List<BitmapFromChart> listOfImages) {
        this.listOfImages = listOfImages;
    }
}
