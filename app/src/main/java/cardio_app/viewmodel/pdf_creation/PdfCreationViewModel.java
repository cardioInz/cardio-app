package cardio_app.viewmodel.pdf_creation;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

/**
 * Created by kisam on 17.11.2016.
 */

public class PdfCreationViewModel extends BaseObservable {
    private String locationSave = "";
    private String fileName = "";
    private boolean isSendEmailOpt = true;
    private String emailAddr = "";

    public PdfCreationViewModel() {

    }


    @Bindable
    public String getLocationSave() {
        if (locationSave == null)
            return "";
        return locationSave;
    }

    public void setLocationSave(String locationSave) {
        this.locationSave = locationSave;
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

    @Bindable
    public int getVisibilitySendEmail() {
        if (isSendEmailOpt()){
            return View.VISIBLE;
        } else {
            return View.GONE;
        }
    }

    @Bindable
    public int getVisibilitySavePdf() {
        if (isSaveOpt()){
            return View.VISIBLE;
        } else {
            return View.GONE;
        }
    }

    private void useVisibilityUtil(int visibility, boolean IsInvokedFromSendEmail){
        switch (visibility){
            case View.VISIBLE:
                isSendEmailOpt = IsInvokedFromSendEmail;
                break;
            case View.GONE:
                isSendEmailOpt = !IsInvokedFromSendEmail;
                break;
            case View.INVISIBLE:
                isSendEmailOpt = !IsInvokedFromSendEmail;
                break;
            default:
                break;
        }
    }

    @Bindable
    public String getEmailAddr() {
        if (emailAddr == null)
            return "";
        return emailAddr;
    }

    public void setEmailAddr(String emailAddr) {
        this.emailAddr = emailAddr;
    }

    @Bindable
    public boolean isSaveOpt() {
        return !isSendEmailOpt;
    }

    public void setSaveOpt(boolean saveOpt) {
        isSendEmailOpt = !saveOpt;
    }

    @Bindable
    public boolean isSendEmailOpt() {
        return isSendEmailOpt;
    }

    public void setSendEmailOpt(boolean sendEmailOpt) {
        isSendEmailOpt = sendEmailOpt;
    }
}
