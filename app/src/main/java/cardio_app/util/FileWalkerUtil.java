package cardio_app.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cardio_app.pdf_creation.param_models.BitmapFromChart;

import static android.content.ContentValues.TAG;

public class FileWalkerUtil {

    private static String PREFIX_PATH = "cardio_charts";
    private static String DIRECTORY_TO_COLLECT_CHARTS = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()
            + File.separator + PREFIX_PATH + File.separator + "to_pdf";


    public static List<BitmapFromChart> getBitmapFromChartList_fromSavedDir() {
        List<BitmapFromChart> list = new ArrayList<>();
        File dir = new File(DIRECTORY_TO_COLLECT_CHARTS);

        if (!dir.exists())
            return list;

        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.isFile()) {
                    BitmapFromChart bitmapFromChart = new BitmapFromChart();
                    try {
                        String absPath = file.getAbsolutePath();

                        String path = file.getParentFile().getAbsolutePath();
                        String ext = getExtFromAbsPath(absPath);
                        String fileName = getFileNameWithoutExt(file.getName());

                        bitmapFromChart.setPath(path);
                        bitmapFromChart.setExt(BitmapUtil.EXT_IMG.getExtFromStr(ext));
                        bitmapFromChart.setFileName(fileName);

                        if (bitmapFromChart.hasFilePathExt()) {
                            list.add(bitmapFromChart);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "getBitmapFromChartList_fromSavedDir: ", e);
                    }
                }
            }
        }
        return list;
    }

    private static String getSthUnique() {
        Calendar calendar = Calendar.getInstance();
        return String.valueOf(calendar.getTimeInMillis());
    }

    private static String getFileNameWithoutExt(String filename) {
        int i = filename.lastIndexOf('.');
        if (i > 0) {
            return filename.substring(0, i);
        }
        return null;
    }

    private static String getExtFromAbsPath(String absPath) {
        int i = absPath.lastIndexOf('.');
        if (i > 0) {
            return absPath.substring(i);
        }
        return null;
    }

    public static String getDirToSaveCharts() {
        // directory not to create pdf, just to save
        final String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()
                + File.separator + PREFIX_PATH + File.separator + "saved_charts";
        File file = new File(directory);
        if (!file.exists()) {
            file.mkdirs();
        }
        return directory;
    }

    public static String getDirectoryToCollectCharts() {
        File file = new File(DIRECTORY_TO_COLLECT_CHARTS);
        if (!file.exists()) {
            file.mkdirs();
        }
        return DIRECTORY_TO_COLLECT_CHARTS;
    }

    private static void deleteAllContentFromDir(String path) {
        File outputFolder = new File(path);
        if (outputFolder.isDirectory()) {
            final File[] files = outputFolder.listFiles();
            for (File file : files) {
                delete(file);
            }

        }
    }

    private static void delete(File file) {
        if (file.isDirectory()) {
            for (File c : file.listFiles())
                delete(c);
        }

        try {
            if (!file.delete())
                Log.e(TAG, "delete: failed for:" + file.getAbsolutePath());
            ;
        } catch (Exception e) {
            Log.e(TAG, "delete: exception", e);
        }

    }

    public static void deleteAllCollectedCharts() {
        if (DIRECTORY_TO_COLLECT_CHARTS != null)
            deleteAllContentFromDir(DIRECTORY_TO_COLLECT_CHARTS);
    }

    public static String getSomeUniqueImageName() {
        // return values without extension
        String sthUnique = getSthUnique();
        return "chart_src" + (sthUnique.isEmpty() ? "" : "_" + sthUnique);
    }

    public static String getSomeUniquePdfName(String from, String to) {
// TODO below code makes pdf name unique, but until we won't delete file after sending email, it will be better to override it every time
//        String sthUnique = getSthUnique();
//        return String.format("%s_%s_%s%s",
//                "report", from, to, sthUnique.isEmpty() ? "" : "_" + sthUnique);

        return "report";
    }

    public static boolean createDirIfNoExists(String path) {
        File file = new File(path);
        boolean result = createDirIfNoExists(file);
        if (result)
            Log.i(TAG, "createDirIfNoExists: " + path + " exists or just created");
        return result;
    }

    private static boolean createDirIfNoExists(File file) {
        try {
            if (file.isDirectory()) {
                return file.exists() || file.mkdirs();
            } else {
                return createDirIfNoExists(file.getParentFile());
            }
        } catch (Exception e) {
            Log.e(TAG, "createDirIfNoExists: ", e);
            return false;
        }
    }
}
