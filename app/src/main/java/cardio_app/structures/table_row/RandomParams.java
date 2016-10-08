package cardio_app.structures.table_row;

import java.util.Date;
import java.util.Random;

public class RandomParams {
    // just temporary class

    private static Random r = new Random();

    private static int randIntFromTo(int from, int to) {
        return r.nextInt(to - from) + from;
    }

    public static HealthParams getRandomHealthParams() {
        int diastole = randIntFromTo(60, 100);
        int systole = randIntFromTo(diastole+29, diastole+60);
        int pulse = randIntFromTo(30, 100);
        boolean arrhythmia = r.nextBoolean();
        ;
        Date date = new Date();
        return new HealthParams(systole, diastole, pulse, arrhythmia, date);
    }
}
