package cardio_app.db.model.helpers;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import cardio_app.R;
import cardio_app.db.model.OtherSymptomsRecord;

public class OtherSymptomsRecordHelper {
    private OtherSymptomsRecord otherSymptomsRecord;
    private Map<Integer, Callable<Boolean>> buttonToSymptomFunction;

    public OtherSymptomsRecordHelper(OtherSymptomsRecord otherSymptomsRecord) {
        this.otherSymptomsRecord = otherSymptomsRecord;
        createButtonToSymptomFunctionMap();
    }

    public static void setSymptomProperty(int id, OtherSymptomsRecord record) {
        switch (id) {
            case R.id.button_headache:
                boolean isHeadache = record.isHeadache();
                record.setHeadache(!isHeadache);
                break;
            case R.id.button_cough:
                boolean isCough = record.isCough();
                record.setCough(!isCough);
                break;
            case R.id.button_fever:
                boolean isHighTemperature = record.isHighTemperature();
                record.setHighTemperature(!isHighTemperature);
                break;
            case R.id.button_stomachache:
                boolean isStomachAche = record.isStomachAche();
                record.setStomachAche(!isStomachAche);
                break;
            case R.id.button_toothache:
                boolean isToothache = record.isToothache();
                record.setToothache(!isToothache);
                break;
        }
    }

    private void createButtonToSymptomFunctionMap() {
        buttonToSymptomFunction = new HashMap<>();
        buttonToSymptomFunction.put(R.id.button_cough, otherSymptomsRecord::isCough);
        buttonToSymptomFunction.put(R.id.button_fever, otherSymptomsRecord::isHighTemperature);
        buttonToSymptomFunction.put(R.id.button_toothache, otherSymptomsRecord::isToothache);
        buttonToSymptomFunction.put(R.id.button_headache, otherSymptomsRecord::isHeadache);
        buttonToSymptomFunction.put(R.id.button_stomachache, otherSymptomsRecord::isStomachAche);
    }

    public Callable<Boolean> getSymptomFunction(Integer buttonId) {
        return buttonToSymptomFunction.get(buttonId);
    }

    public Set<Integer> getSymptomButtons() {
        return buttonToSymptomFunction.keySet();
    }
}
