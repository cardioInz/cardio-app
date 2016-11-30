package cardio_app.viewmodel.pdf_creation;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.Date;

import cardio_app.filtering.DataFilter;
import cardio_app.filtering.DataFilterModeEnum;

import static cardio_app.util.DateTimeUtil.DATE_FORMATTER;


public class DataFilterForPdfCreationViewModel extends BaseObservable {

    private DataFilter dataFilter = null;
    private Date earliestDate = null;
    private Date latestDate = null;

    public DataFilterForPdfCreationViewModel(){
        this(null, null, null);
    }

    public DataFilterForPdfCreationViewModel(DataFilter dataFilter, Date earliestDate, Date latestDate){
        setDatesBoundary(earliestDate, latestDate);
        this.dataFilter = dataFilter;
    }

    public void setDataFilter(DataFilter dataFilter) {
        this.dataFilter = dataFilter;
    }

    public void copyValuesFrom(DataFilter dataFilter) {
        this.dataFilter.copyValues(dataFilter);
    }

    @Bindable
    public String getDateFromStr(){
        if (dataFilter == null)
            return "-";
        else if (dataFilter.getMode().equals(DataFilterModeEnum.NO_FILTER) && earliestDate != null) {
            return DATE_FORMATTER.format(earliestDate);
        } else {
            return dataFilter.getDateToStr();
        }
    }

    @Bindable
    public String getDateToStr(){
        if (dataFilter == null)
            return "-";
        else if (dataFilter.getMode().equals(DataFilterModeEnum.NO_FILTER) && latestDate != null) {
            return DATE_FORMATTER.format(latestDate);
        } else {
            return dataFilter.getDateToStr();
        }
    }
//
//    public void setDateToStr(String date){
//        // stay empty
//    }
//
//    public void setDateFromStr(String date){
//        // stay empty
//    }


    @Bindable
    public DataFilter getDataFilter() {
        return dataFilter;
    }

    public void setDatesBoundary(Date early, Date late){
        this.earliestDate = early;
        this.latestDate = late;
    }

}
