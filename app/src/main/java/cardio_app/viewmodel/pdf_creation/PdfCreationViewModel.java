package cardio_app.viewmodel.pdf_creation;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.List;

import cardio_app.pdf_creation.param_models.BitmapFromChart;
import cardio_app.pdf_creation.param_models.PdfChosenParams;

/**
 * Created by kisam on 17.11.2016.
 */

public class PdfCreationViewModel extends BaseObservable {
    private PdfChosenParams pdfDataModel = new PdfChosenParams();

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

    public PdfChosenParams getPdfDataModel() {
        return pdfDataModel;
    }

    public void setPdfDataModel(PdfChosenParams pdfDataModel) {
        this.pdfDataModel = pdfDataModel;
    }

    @Bindable
    public String getChartsListSize(){
        if (pdfDataModel == null)
            return String.valueOf(0);
        List<BitmapFromChart> list = pdfDataModel.getExtraBitmapFromChartList();
        if (list == null)
            return String.valueOf(0);
        return String.valueOf(list.size());
    }

    public void setChartsListSize(String size){
        // pass
    }

    public void setExtraChartsList(List<BitmapFromChart> listImages){
        pdfDataModel.setExtraBitmapFromChartList(listImages);
    }

    public List<BitmapFromChart> getExtraChartsList(){
        return pdfDataModel.getExtraBitmapFromChartList();
    }
}
