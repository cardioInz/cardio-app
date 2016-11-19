package cardio_app.statistics.pdf_creation;

import java.util.Date;

import cardio_app.db.model.BaseModel;

/**
 * Created by kisam on 19.11.2016.
 */

public class PdfCreationDataModel extends BaseModel {
    private String locationSave;
    private String fileName;
    private boolean isSendEmailOpt;
    private String emailAddr;
    private Date dateFrom = null;
    private Date dateTo = null;

    public PdfCreationDataModel() {

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

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }


}
