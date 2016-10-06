package cardio_app.structures.table_row;

import java.util.Date;
import java.util.Random;

public class RandomParams {
    // just temporary class

    private static Random r = new Random();

    private static int randIntFromTo(int from, int to){
        return r.nextInt(to-from) + from;
    }

    public static HealthParams getRandomHealthParams() {
        int systole = randIntFromTo(70, 200);
        int diastole = randIntFromTo(50, 120);
        int pulse = randIntFromTo(30, 100);
        boolean arrhythmia = r.nextBoolean();;
        Date date = new Date();
        return new HealthParams(systole, diastole, pulse, arrhythmia, date);
    }
}
