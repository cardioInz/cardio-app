package cardio_app.viewmodel.pdf_creation;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.Date;

import cardio_app.R;
import cardio_app.filtering.DataFilter;
import cardio_app.filtering.DataFilterModeEnum;

/**
 * Created by kisam on 18.11.2016.
 */

public class DataFilterForPdfCreationViewModel extends BaseObservable {

    private DataFilter dataFilter = null;
    private String earliest = null;
    private String newest = null;

    public DataFilterForPdfCreationViewModel(Context context, DataFilter dataFilter, String earliest, String newest){
        if (earliest != null && newest != null && !earliest.isEmpty() && !newest.isEmpty()) {
            this.earliest = earliest;
            this.newest = newest;
        } else {
            this.earliest = null;
            this.newest = null;
        }

        this.dataFilter = dataFilter;
    }

    public void setDataFilter(DataFilter dataFilter) {
        this.dataFilter = dataFilter;
    }

    @Bindable
    public String getDateFromStr(){
        if (dataFilter == null){
            return "-";
        } else {
            return prepareOutputDate(dataFilter.getMode(), dataFilter.getDateFromStr(), earliest);
        }
    }

    @Bindable
    public String getDateToStr(){
        if (dataFilter == null){
            return "-";
        } else {
            return prepareOutputDate(dataFilter.getMode(), dataFilter.getDateToStr(), newest);
        }
    }


    private static String prepareOutputDate(DataFilterModeEnum mode, String dateStr, String customDateStr){
        if (mode.equals(DataFilterModeEnum.NO_FILTER) && customDateStr != null) {
                return String.format("%s", customDateStr);
        } else {
            return dateStr;
        }
    }

    public DataFilter getDataFilter() {
        return dataFilter;
    }
}
