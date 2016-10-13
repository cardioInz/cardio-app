package temporary_package;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cardio_app.structures.table_row.HealthParams;

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

    public static List<HealthParams> makeParamList() {
        String [] array = DATA_STR.split("\n");
        List<HealthParams> paramsList = new ArrayList<>();
        String systole;
        String diastole;
        String pulse;
        String date = "2020-12-12";
        String time;
        boolean arrhythmia;

        for (String s : array) {
            String[] params = s.split(",");
            if (params.length != 4){
                continue;
            }

            systole = params[1];
            diastole = params[2];
            pulse = params[3];
            arrhythmia = s.hashCode() % 11 == 0;

            if (!params[0].isEmpty()){
                date = params[0];
                time = "08:00";
            } else {
                time = "19:00";
            }

            paramsList.add(new HealthParams(systole, diastole, pulse, arrhythmia, date, time));
        }

        return paramsList;
    }

    private static String DATA_STR = "2016-04-16,113,85,28\n" +
                ",131,94,37\n" +
                "2016-04-17,123,89,34\n" +
                ",132,93,39\n" +
                "2016-04-18,121,87,34\n" +
                ",147,93,54\n" +
                "2016-04-19,112,84,28\n" +
                ",127,86,41\n" +
                "2016-04-20,125,100,25\n" +
                ",131,93,38\n" +
                "2016-04-21,120,95,25\n" +
                ",123,89,34\n" +
                "2016-04-22,122,90,32\n" +
                ",125,91,34\n" +
                "2016-04-23,136,88,48\n" +
                ",110,85,25\n" +
                "2016-04-24,102,85,17\n" +
                ",110,85,25\n" +
                "2016-04-25,136,85,51\n" +
                ",110,87,23\n" +
                "2016-04-26,150,100,50\n" +
                ",123,87,36\n" +
                "2016-04-27,120,98,22\n" +
                ",148,89,59\n" +
                "2016-04-28,129,105,24\n" +
                ",141,95,46\n" +
                "2016-04-29,130,98,32\n" +
                ",140,92,48\n" +
                "2016-04-30,150,96,54\n" +
                ",112,83,29\n" +
                "2016-05-01,127,91,36\n" +
                ",136,93,43\n" +
                "2016-05-02,124,86,38\n" +
                ",136,93,43\n" +
                "2016-05-03,124,99,25\n" +
                ",121,84,37\n" +
                "2016-05-04,135,84,51\n" +
                ",123,83,40\n" +
                "2016-05-05,119,82,37\n" +
                ",160,100,60\n" +
                "2016-05-06,150,90,60\n" +
                "2016-05-07,124,74,50\n" +
                ",123,82,41\n" +
                "2016-05-08,116,96,20\n" +
                ",128,90,38\n" +
                "2016-05-09,157,92,65\n" +
                ",106,85,21\n" +
                "2016-05-10,130,89,41\n" +
                ",105,79,26\n" +
                "2016-05-11,115,88,27\n" +
                ",133,81,52\n" +
                "2016-05-12,129,89,40\n" +
                ",123,93,30\n" +
                "2016-05-13,130,93,37\n" +
                ",136,93,43\n" +
                "2016-05-14,100,85,15\n" +
                ",133,87,46\n" +
                "2016-05-15,101,78,23\n" +
                ",130,91,39\n" +
                "2016-05--16,129,78,51\n" +
                ",137,97,40\n" +
                "2016-05-17,125,93,32\n" +
                ",140,93,47\n" +
                "2016-05-18,133,87,46\n" +
                ",136,85,51\n" +
                "2016-05-19,124,95,29\n" +
                ",118,75,43\n" +
                "2016-05-20,114,94,20\n" +
                ",110,73,37\n" +
                "2016-05-21,110,73,37\n" +
                ",142,89,53\n" +
                "2016-05-22,126,91,35\n" +
                ",132,93,39\n" +
                "2016-05-23,117,87,30\n" +
                ",118,83,35\n" +
                "2016-05-24,131,88,43\n" +
                ",113,80,33\n" +
                "2016-05-25,128,83,45\n" +
                ",103,72,31\n" +
                "2016-05-26,113,71,42\n" +
                ",129,85,44\n" +
                "2016-05-27,118,80,38\n" +
                ",136,93,43\n" +
                "2016-05-28,113,89,24\n" +
                ",115,81,34\n" +
                "2016-05-29,\n" +
                "2016-05-30,124,79,45\n" +
                ",122,85,37\n" +
                "2016-05-31,146,94,52\n" +
                ",120,80,40\n" +
                "2016-06-01,120,87,33\n" +
                ",128,87,41\n" +
                "2016-06-02,138,87,51\n" +
                ",147,96,51\n" +
                "2016-06-03,120,88,32\n" +
                ",116,74,42\n" +
                "2016-06-04,108,80,28\n" +
                ",134,93,41\n" +
                "2016-06-05,111,75,36\n" +
                ",121,88,33\n" +
                "2016-06-06,117,74,43\n" +
                ",125,85,40\n" +
                "2016-06-07,124,89,35\n" +
                ",130,90,40\n" +
                "2016-06-08,99,80,19\n" +
                "2016-06-09,110,96,14\n" +
                ",104,65,39\n" +
                "2016-06-10,124,92,32\n" +
                ",131,81,50\n" +
                "2016-06-11,134,88,46\n" +
                ",142,88,54\n" +
                "2016-06-12,104,80,24\n" +
                ",134,89,45\n" +
                "2016-06-13,132,91,41\n" +
                ",130,92,38\n" +
                "2016-06-14,120,81,39\n" +
                ",122,83,39\n" +
                "2016-06-15,133,91,42\n" +
                ",140,96,44\n" +
                "2016-06-16,129,97,32\n" +
                ",112,75,37\n" +
                "2016-06-17,140,87,53\n" +
                ",123,84,39\n" +
                "2016-06-18,114,77,37\n" +
                ",127,79,48\n" +
                "2016-06-19,102,78,24\n" +
                ",133,83,50\n" +
                "2016-06-20,111,86,25\n" +
                ",148,94,54\n" +
                "2016-06-21,108,81,27\n" +
                ",125,87,38\n" +
                "2016-06-22,143,92,51\n" +
                ",126,88,38\n" +
                "2016-06-23,133,90,43\n" +
                ",126,86,40\n" +
                "2016-06-24,118,96,22\n" +
                ",121,86,35\n" +
                "2016-06-25,139,102,37\n" +
                ",125,86,39\n" +
                "2016-06-26,136,86,50\n" +
                "2016-06-28,161,99,62\n" +
                ",128,87,41\n" +
                "2016-06-29,124,87,37\n" +
                "2016-06-30,134,98,36\n" +
                ",126,92,34\n" +
                "2016-07-04,131,85,46\n" +
                ",118,85,33\n" +
                "2016-07-05,129,90,39\n" +
                ",121,92,29\n" +
                "2016-07-06,123,91,32\n" +
                ",135,94,41\n" +
                "2016-07-07,136,91,45\n" +
                ",125,94,31\n" +
                "2016-07-08,134,93,41\n" +
                "2016-07-09,118,82,36\n" +
                ",109,79,30\n" +
                "2016-07-10,121,81,40\n" +
                ",126,88,38\n" +
                "2016-07-11,133,88,45\n" +
                "2016-07-12,137,97,40\n" +
                ",133,84,49\n" +
                "2016-07-13,129,88,41\n" +
                ",137,95,42\n" +
                "2016-07-14,144,93,51\n" +
                ",131,87,44\n" +
                "2016-07-15,129,87,42\n" +
                ",118,83,35\n" +
                "2016-07-16,111,77,34\n" +
                ",116,83,33\n" +
                "2016-07-17,116,84,32\n" +
                ",136,88,48\n" +
                "2016-07-18,138,89,49\n" +
                ",128,87,41\n" +
                "2016-07-19,129,93,36\n" +
                ",121,75,46\n" +
                "2016-07-20,127,94,33\n" +
                ",124,80,44\n" +
                "2016-07-21,123,91,32\n" +
                ",119,84,35\n" +
                "2016-07-22,115,80,35\n" +
                ",127,88,39\n" +
                "2016-07-23,119,78,41\n" +
                ",129,86,43\n" +
                "2016-07-25,128,82,46\n" +
                ",117,78,39\n" +
                "2016-07-26,122,82,40";

}
