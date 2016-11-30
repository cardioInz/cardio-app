package cardio_app.pdf_creation.param_models;

import java.util.ArrayList;
import java.util.List;

import cardio_app.db.model.BaseModel;


public class PdfChosenParams extends BaseModel {
    private String locationSave;
    private String fileName;
    private boolean isSendEmailOpt;
    private String emailAddr;
    private List<BitmapFromChart> extraBitmapFromChartList = new ArrayList<>();

    public PdfChosenParams() {

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

    public List<BitmapFromChart> getExtraBitmapFromChartList() {
        return extraBitmapFromChartList;
    }

    public void setExtraBitmapFromChartList(List<BitmapFromChart> listOfImages) {
        this.extraBitmapFromChartList = listOfImages;
    }
}
