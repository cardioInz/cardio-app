package cardio_app.structures.table_row;


import android.annotation.SuppressLint;

import cardio_app.structures.Questionnaire;

public enum HealthCondition {

    UNKNOWN(-10),
    TOO_LOW(-2),
    NORMAL_LOW(-1),
    EXCELLENT(0),
    NORMAL(1),
    NORMAL_HIGH(2),
    HIGH(3),
    TOO_HIGH(4),

    // when merge conditions for systol and diastol
    BAD_DIFF(10),
    CRITICAL_DIFF(20);

    char value;

    HealthCondition(int v){
        value = (char) v;
    }

    public int getValue() {
        return this.value;
    }

    public String getStrMapped() {
        return mapToStr(this);
    }


    @SuppressLint("DefaultLocale")
    private static String mapToStr(HealthCondition c) {
        // TODO - in future it will be name of some drawable resource to show in diary
        switch (c){
//            case UNKNOWN: return "ukn.";
//            case TOO_LOW: return "1";
//            case NORMAL_LOW: return "3";
//            case EXCELLENT: return "5";
//            case NORMAL: return "4";
//            case NORMAL_HIGH: return "3";
//            case HIGH: return "2";
//            case TOO_HIGH: return "1";
//            case BAD_DIFF: return "10";
//            case CRITICAL_DIFF: return "20";
            default: return String.format("%d", c.getValue()); // just as a precaution
        }
    }



    public static HealthCondition classify(HealthParams hf) {
        HealthCondition systoleCon = classifyBySystole(hf.getSystoleInt());
        HealthCondition diastoleCon = classifyByDiastole(hf.getDiastoleInt());

        if (systoleCon.equals(UNKNOWN))
            return diastoleCon;
        if (diastoleCon.equals(UNKNOWN))
            return systoleCon;

        if (systoleCon.equals(diastoleCon))
            return systoleCon;
        else {
            int diff = systoleCon.getValue() - diastoleCon.getValue();
            diff *= diff < 0 ? -1 : 1;

            if (diff == 1) {
                if (systoleCon.getValue() <= 0 && diastoleCon.getValue() <= 0) {
                    // LOW
                    if (systoleCon.getValue() < diastoleCon.getValue())
                        return systoleCon;
                    else
                        return diastoleCon;
                } else if (systoleCon.getValue() >= 0 && diastoleCon.getValue() >= 0) {
                    // HIGH
                    if (systoleCon.getValue() < diastoleCon.getValue())
                        return diastoleCon;
                    else
                        return systoleCon;
                } else {
                    return BAD_DIFF;
                }
            } else if (diff == 2) {
                return BAD_DIFF;
            }
            else {
                return CRITICAL_DIFF;
            }
        }
    }


    private static HealthCondition classifyBySystole(int s){
        if (Questionnaire.isMale){
            if (s < 100)
                return TOO_LOW;
            else if (s <= 110)
                return NORMAL_LOW;
        }
        else {
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


    private static HealthCondition classifyByDiastole(int d){
        if (Questionnaire.isMale){
            if (d < 70)
                return TOO_LOW;
            else if (d < 75)
                return NORMAL_LOW;
        }
        else {
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
}
