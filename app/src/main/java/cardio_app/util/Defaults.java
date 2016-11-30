package cardio_app.util;

import android.content.res.Resources;

import java.util.Calendar;

import cardio_app.R;
import cardio_app.filtering.DataFilter;
import cardio_app.filtering.DataFilterModeEnum;


public class Defaults {
    // TODO - fix defaults

    public static final String DEFAULT_EMAIL = "cardio.inzynierka@gmail.com"; // TODO empty

    private static final DataFilterModeEnum DEFAULT_DATA_FILTER = DataFilterModeEnum.NO_FILTER; // TODO should be last 3 months

    public static DataFilter getDefaultDataFilter() {
        return new DataFilter(DEFAULT_DATA_FILTER);
    }

    public static String prepareBody(Resources resources) {
        // TODO email msg body

        Calendar calendar = Calendar.getInstance();
        String dateTimeStr = DateTimeUtil.DATETIME_FORMATTER.format(calendar.getTime());

        return String.format("%s %s",
                resources.getString(R.string.pdf_created),
                dateTimeStr);
    }

    public static String prepareSubject(Resources resources) {
        // TODO subject nice
        return String.format("%s - %s", resources.getString(R.string.app_name), resources.getString(R.string.pdf_report));
    }
}
