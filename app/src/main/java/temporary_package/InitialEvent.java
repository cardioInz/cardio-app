package temporary_package;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cardio_app.db.model.DailyActivitiesRecord;
import cardio_app.db.model.DoctorsAppointment;
import cardio_app.db.model.Emotion;
import cardio_app.db.model.Event;
import cardio_app.db.model.OtherSymptomsRecord;
import cardio_app.db.model.TimeUnit;
import cardio_app.util.DateTimeUtil;

public class InitialEvent {

    private static final Date MIN_DATE = DateTimeUtil.getDate(16, 4, 2016);
    private static final Date MAX_DATE = DateTimeUtil.getDate(27, 7, 2016);
    private static final int CNT = 70;
    private static final Random r = new Random();

    public static List<Event> makeEventList() {
        List<Event> list = new ArrayList<>();
        createRandomEventList(list);
        return list;
    }


    private static void createRandomEventList(List<Event> list) {
        Date startDate;
        Date endDate;
        boolean isRepeatable;
        TimeUnit timeUnit;
        int timeDelta;
        String description;
        OtherSymptomsRecord otherSymptomsRecord;
        Emotion emotion;
        DoctorsAppointment doctorsAppointment;
        DailyActivitiesRecord dailyActivitiesRecord;
        final boolean isAlarmSet = false;

        for (int i=1; i <= CNT; i++){
            startDate = getRandomDate();
            description = "description" + i;
            emotion = getRandomEmotion();
            doctorsAppointment = getRandomDoctorsAppointment();
            otherSymptomsRecord = getRandomOtherSymptoms();

            if (isRepeatable = r.nextBoolean()){ // repeatable = discrete
                endDate = startDate;
                description += "_r";
                dailyActivitiesRecord = DailyActivitiesRecord.NONE;
                timeUnit = getRandomTimeUnit();
                timeDelta = r.nextInt(10);
            } else { // non repeatable
                if (r.nextBoolean()) { // continuous
                    endDate = getRandomDateAfter(startDate);
                    dailyActivitiesRecord = DailyActivitiesRecord.NONE;
                    description += "_c";
                } else { // discrete
                    endDate = startDate;
                    dailyActivitiesRecord = getRandomDailyActivity();
                    description += "_d";
                }
                timeUnit = TimeUnit.NONE;
                timeDelta = 0;
            }

            Event event = new Event(
                    startDate,
                    endDate,
                    isRepeatable,
                    timeUnit,
                    timeDelta,
                    description,
                    otherSymptomsRecord,
                    emotion,
                    doctorsAppointment,
                    dailyActivitiesRecord,
                    isAlarmSet
            );
            list.add(event);
        }


    }

    private static OtherSymptomsRecord getRandomOtherSymptoms() {
        return new OtherSymptomsRecord(
                r.nextBoolean(),
                r.nextBoolean(),
                r.nextBoolean(),
                r.nextBoolean(),
                r.nextBoolean()
        );
    }


    private static TimeUnit getRandomTimeUnit() {
        TimeUnit[] array = TimeUnit.values();
        return array[r.nextInt(array.length)];
    }


    private static Date getRandomDate(){
        return getRandomDateBetween(MIN_DATE, MAX_DATE);
    }

    private static Date getRandomDateBetween(Date start, Date end){
        return new Date((long) (start.getTime() + Math.random() * (end.getTime() - start.getTime())));
    }

    private static Date getRandomDateAfter(Date after) {
        return getRandomDateBetween(after, MAX_DATE);
    }

    private static Date getRandomDateBefore(Date before) {
        return getRandomDateBetween(MIN_DATE, before);
    }

    private static DailyActivitiesRecord getRandomDailyActivity() {
        DailyActivitiesRecord[] array = DailyActivitiesRecord.values();
        return array[r.nextInt(array.length)];
    }

    private static Emotion getRandomEmotion() {
        Emotion[] array = Emotion.values();
        return array[r.nextInt(array.length)];
    }

    private static DoctorsAppointment getRandomDoctorsAppointment() {
        return new DoctorsAppointment(
                r.nextBoolean(),
                r.nextBoolean(),
                r.nextBoolean(),
                r.nextBoolean(),
                r.nextBoolean()
        );
    }
}
