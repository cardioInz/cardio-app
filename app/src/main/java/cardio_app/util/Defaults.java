package cardio_app.util;

import android.content.res.Resources;

import java.util.Calendar;

import cardio_app.R;
import cardio_app.filtering.DataFilter;


public class Defaults {

    public static final String DEFAULT_EMAIL = "";

    public static DataFilter getDefaultDataFilter() {
//        return new DataFilter(DEFAULT_DATA_FILTER);
        return new DataFilter(90); // last 3 months
    }

    public static String prepareBody(Resources resources) {
        Calendar calendar = Calendar.getInstance();
        String dateTimeStr = DateTimeUtil.DATETIME_FORMATTER.format(calendar.getTime());

        return String.format("%s %s",
                resources.getString(R.string.pdf_created),
                dateTimeStr);
    }

    public static String prepareSubject(Resources resources) {
        return String.format("%s - %s", resources.getString(R.string.app_name), resources.getString(R.string.pdf_report));
    }
}
