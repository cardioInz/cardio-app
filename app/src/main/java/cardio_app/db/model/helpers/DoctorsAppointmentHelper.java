package cardio_app.db.model.helpers;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import cardio_app.R;
import cardio_app.db.model.DoctorsAppointment;

public class DoctorsAppointmentHelper {
    private DoctorsAppointment doctorsAppointment;
    private Map<Integer, Callable<Boolean>> buttonToVisitTypeFunction;

    public DoctorsAppointmentHelper(DoctorsAppointment doctorsAppointment) {
        this.doctorsAppointment = doctorsAppointment;
        createButtonToVisitTypeFunctionMap();
    }

    private void createButtonToVisitTypeFunctionMap() {
        buttonToVisitTypeFunction = new HashMap<>();
        buttonToVisitTypeFunction.put(R.id.button_medical_checkout, doctorsAppointment::isRoutineCheck);
        buttonToVisitTypeFunction.put(R.id.button_flu, doctorsAppointment::isFlu);
        buttonToVisitTypeFunction.put(R.id.button_examination, doctorsAppointment::isExamination);
        buttonToVisitTypeFunction.put(R.id.button_prescription, doctorsAppointment::isForPrescription);
        buttonToVisitTypeFunction.put(R.id.button_emergency, doctorsAppointment::isEmergency);
    }

    public Callable<Boolean> getVisitTypeFunction(Integer buttonId) {
        return buttonToVisitTypeFunction.get(buttonId);
    }

    public Set<Integer> getVisitTypeButtons() {
        return buttonToVisitTypeFunction.keySet();
    }

    public static void setVisitProperty(int id, DoctorsAppointment doctorsAppointment) {
        switch (id) {
            case R.id.button_medical_checkout:
                boolean isMedicalCheckout = doctorsAppointment.isRoutineCheck();
                doctorsAppointment.setRoutineCheck(!isMedicalCheckout);
                break;
            case R.id.button_examination:
                boolean isExamination = doctorsAppointment.isExamination();
                doctorsAppointment.setExamination(!isExamination);
                break;
            case R.id.button_prescription:
                boolean isForPrescription = doctorsAppointment.isForPrescription();
                doctorsAppointment.setForPrescription(!isForPrescription);
                break;
            case R.id.button_flu:
                boolean isFlu = doctorsAppointment.isFlu();
                doctorsAppointment.setFlu(!isFlu);
                break;
            case R.id.button_emergency:
                boolean isEmergency = doctorsAppointment.isEmergency();
                doctorsAppointment.setEmergency(!isEmergency);
                break;
        }
    }
}
