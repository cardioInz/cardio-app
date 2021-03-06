package cardio_app.statistics.analyse;


import android.annotation.SuppressLint;

import cardio_app.db.model.PressureData;
import cardio_app.statistics.Statistics;


public enum HealthCondition {
    MIN_VAL(1),
    TOO_LOW(MIN_VAL),
    NORMAL_LOW,
    EXCELLENT,
    NORMAL,
    NORMAL_HIGH,
    HIGH,
    TOO_HIGH,
    MAX_VAL(TOO_HIGH), // 8

    SPECIAL_VAL(10),
    UNKNOWN(SPECIAL_VAL),
    BAD_DIFF;

    private final int value;

    HealthCondition() {
        this.value = ++LastCondition.VALUE;
    }

    HealthCondition(int value) {
        this.value = value;
        LastCondition.VALUE = value;
    }

    HealthCondition(HealthCondition c) {
        this.value = c.value;
        LastCondition.VALUE = c.value;
    }

    public static HealthCondition classify(PressureData hf) {
        HealthCondition systoleCond = classifyBySystole(hf.getSystole());
        HealthCondition diastoleCond = classifyByDiastole(hf.getDiastole());
        int diff = hf.getSystole() - hf.getDiastole();

        if (diff < 30 || diff > 60)
            return BAD_DIFF;
        if (systoleCond.equals(UNKNOWN) || diastoleCond.equals(UNKNOWN))
            return UNKNOWN;

        int sCondVal = systoleCond.value;
        int dCondVal = diastoleCond.value;

        if (sCondVal <= EXCELLENT.value && dCondVal <= EXCELLENT.value) {
            // LOW - so return lower
            return (sCondVal < dCondVal) ? systoleCond : diastoleCond;
        } else if (sCondVal >= EXCELLENT.value && dCondVal >= EXCELLENT.value) {
            // HIGH - so return higher
            return (sCondVal > dCondVal) ? systoleCond : diastoleCond;
        } else {
            if (dCondVal <= NORMAL_LOW.value && sCondVal >= NORMAL_HIGH.value)
                return BAD_DIFF;
            else
                return NORMAL;
        }
    }

    private static HealthCondition classifyBySystole(int s) {
        if (Statistics.isMale()) {
            if (s < 100)
                return TOO_LOW;
            else if (s <= 110)
                return NORMAL_LOW;
        } else {
            if (s < 90)
                return TOO_LOW;
            else if (s <= 105)
                return NORMAL_LOW;
        }

        if (s <= 120)
            return EXCELLENT;
        else if (s <= 129)
            return NORMAL;
        else if (s <= 139)
            return NORMAL_HIGH;
        else if (s <= 150)
            return HIGH;
        else
            return TOO_HIGH;
    }

    private static HealthCondition classifyByDiastole(int d) {
        if (Statistics.isMale()) {
            if (d < 70)
                return TOO_LOW;
            else if (d < 75)
                return NORMAL_LOW;
        } else {
            if (d < 60)
                return TOO_LOW;
            else if (d <= 70)
                return NORMAL_LOW;
        }

        if (d <= 80)
            return EXCELLENT;
        else if (d <= 84)
            return NORMAL;
        else if (d <= 89)
            return NORMAL_HIGH;
        else if (d <= 95)
            return HIGH;
        else
            return TOO_HIGH;
    }

    @SuppressLint("DefaultLocale")
    private static String mapToStr(HealthCondition c) {
        switch (c) {
            default:
                return String.format("%d", c.getValue()); // just as a precaution
        }
    }

    public static Simplified mapToSimplifiedCondition(HealthCondition healthCondition) {
        switch (healthCondition) {
            case MIN_VAL:
            case TOO_LOW:
                return Simplified.BAD;
            case NORMAL_LOW:
                return Simplified.MIDDLE;
            case EXCELLENT:
            case NORMAL:
                return Simplified.WELL;
            case NORMAL_HIGH:
                return Simplified.MIDDLE;
            case HIGH:
            case TOO_HIGH:
            case MAX_VAL:
                return Simplified.BAD;
            case SPECIAL_VAL:
            case UNKNOWN:
                return Simplified.UNKNOWN;
            case BAD_DIFF:
                return Simplified.BAD_DIFF;
            default:
                return null;
        }
    }

    public int getValue() {
        return this.value;
    }

    public String getStrMapped() {
        return mapToStr(this);
    }

    // used to simplify HealthCondition
    public enum Simplified {
        WELL,
        BAD,
        MIDDLE,
        BAD_DIFF,
        UNKNOWN
    }

    private static class LastCondition {
        static int VALUE = 0;
    }

}
