package cardio_app.db.model;

import android.support.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by kisam on 27.10.2016.
 */

public class HealthParamsDateAndTime extends BaseModel implements Comparable<HealthParamsDateAndTime> {
    private static int ID_CNT = 0;

    @DatabaseField(foreign = true, uniqueCombo = true)
    private HealthParams healthParams;

    @DatabaseField(foreign = true, uniqueCombo = true)
    private DateAndTime dateAndTime;

    public HealthParamsDateAndTime(HealthParams healthParams, DateAndTime dateAndTime) {
        super(++ID_CNT);
        this.healthParams = healthParams;
        this.dateAndTime = dateAndTime;
    }

    public DateAndTime getDateAndTime() {
        return dateAndTime;
    }

    public HealthParams getHealthParams() {
        return healthParams;
    }

    @Override
    public int compareTo(@NonNull HealthParamsDateAndTime that) {
        int result = this.dateAndTime.compareToDateTime(that.getDateAndTime());
        if (result != 0)
            return result;
        return this.healthParams.compareToHealthParams(that.getHealthParams());
    }
}
