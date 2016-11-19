package cardio_app.viewmodel.pdf_creation;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import cardio_app.statistics.pdf_creation.PdfCreationDataModel;

/**
 * Created by kisam on 17.11.2016.
 */

public class PdfCreationViewModel extends BaseObservable {

    private DataFilterForPdfCreationViewModel dataFilterViewModel = new DataFilterForPdfCreationViewModel();
    private PdfCreationDataModel pdfDataModel = new PdfCreationDataModel();

    public PdfCreationViewModel() {
        pdfDataModel.setSendEmailOpt(true); // by default "send email" redio button checkeds
    }

    private static String getStrIfNotNull(String str){
        return str == null ? "" : str;
    }

    @Bindable
    public String getLocationSave() {
        return getStrIfNotNull(pdfDataModel.getLocationSave());
    }

    public void setLocationSave(String locationSave) {
        pdfDataModel.setLocationSave(locationSave);
    }

    @Bindable
    public String getFileName() {
        return getStrIfNotNull(pdfDataModel.getFileName());
    }

    public void setFileName(String fileName) {
        pdfDataModel.setFileName(fileName);
    }

    @Bindable
    public String getEmailAddr() {
        return getStrIfNotNull(pdfDataModel.getEmailAddr());
    }

    public void setEmailAddr(String emailAddr) {
        pdfDataModel.setEmailAddr(emailAddr);
    }

    @Bindable
    public boolean isSaveOpt() {
        return !pdfDataModel.isSendEmailOpt();
    }

    public void setSaveOpt(boolean saveOpt) {
        pdfDataModel.setSendEmailOpt(!saveOpt);
    }

    @Bindable
    public boolean isSendEmailOpt() {
        return pdfDataModel.isSendEmailOpt();
    }

    public void setSendEmailOpt(boolean sendEmailOpt) {
        pdfDataModel.setSendEmailOpt(sendEmailOpt);
    }

    public PdfCreationDataModel getPdfDataModel() {
        return pdfDataModel;
    }

    public void setPdfDataModel(PdfCreationDataModel pdfDataModel) {
        this.pdfDataModel = pdfDataModel;
    }

    public DataFilterForPdfCreationViewModel getDataFilterViewModel() {
        return dataFilterViewModel;
    }
}
