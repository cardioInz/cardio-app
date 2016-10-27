package cardio_app.viewmodel;


import android.annotation.SuppressLint;

import cardio_app.db.model.HealthParams;
import cardio_app.db.model.Questionnaire;


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

    private static class LastCondition {
        static int VALUE = 0;
    }

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

    public int getValue() {
        return this.value;
    }

    public static HealthCondition classify(HealthParams hf) {
        HealthCondition systoleCond = classifyBySystole(hf.getSystoleInt());
        HealthCondition diastoleCond = classifyByDiastole(hf.getDiastoleInt());
        int diff = hf.getSystoleInt() - hf.getDiastoleInt();

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
        if (Questionnaire.isMale) {
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
        if (Questionnaire.isMale) {
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


    public String getStrMapped() {
        return mapToStr(this);
    }


    @SuppressLint("DefaultLocale")
    private static String mapToStr(HealthCondition c) {
        // TODO - in future it will be name of some drawable resource to show in diary
        switch (c) {
            default:
                return String.format("%d", c.getValue()); // just as a precaution
        }
    }

}
