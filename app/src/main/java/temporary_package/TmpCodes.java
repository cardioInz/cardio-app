package temporary_package;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import cardio_app.db.model.PressureData;
import cardio_app.pdf_creation.param_models.BitmapFromChart;
import cardio_app.util.ChartBuilder;
import cardio_app.util.BitmapUtil;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.view.AbstractChartView;
import lecho.lib.hellocharts.view.LineChartView;

import static android.content.ContentValues.TAG;

/**
 * Created by kisam on 22.11.2016.
 */

class TmpCodes {



//    public static Image prepareChartImage(AppCompatActivity activity, List<PressureData> pressureList) {
//        try {
//            ChartBuilder chartBuilder = new ChartBuilder(pressureList, activity.getResources());
//            LineChartData data = chartBuilder.setMode(ChartBuilder.ChartMode.DISCRETE).build();
//            LineChartView lineChartView = new LineChartView(activity);
//            lineChartView.setLineChartData(data);
////            return getImageFromAbstractCharView(lineChartView);
//            return getImageFromAbstractCharView_vol2(lineChartView);
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e(TAG, "refreshStatisticsView: ", e);
//            return null;
//        }
//    }
//
//    private static Image getImageFromAbstractCharView(AbstractChartView abstractChartView) throws IOException, BadElementException {
//        abstractChartView.setDrawingCacheEnabled(true);
//        abstractChartView.buildDrawingCache();
//        Bitmap bmp = Bitmap.createBitmap( 200, 300, Bitmap.Config.ARGB_8888 );
//        Canvas canvas = new Canvas(bmp);
//        abstractChartView.draw(canvas);
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        bmp.compress( Bitmap.CompressFormat.JPEG, 97, out );
//        abstractChartView.setDrawingCacheEnabled(false);
//        try {
//            out.close();
//        } catch (IOException e) {
//            Log.e(TAG, "getImageFromAbstractCharView: ", e);
//            e.printStackTrace();
//        }
//        return Image.getInstance(out.toByteArray());
//    }
//
//    private static Image getImageFromAbstractCharView_vol2(AbstractChartView abstractChartView) throws IOException, BadElementException {
//        Bitmap bm = BitmapUtil.getBitmapFromChartView(abstractChartView);
//        return BitmapUtil.convertBitmapToImage(bm);
//    }
//
//
//    public void addImageFromChart(LineChartView chartView){
//        BitmapFromChart bitmapFromChart = new BitmapFromChart(chartView);
//        if (bitmapFromChart.hasCompletedValues())
//            listOfImages.add(bitmapFromChart);
//        else
//            Log.w(TAG, "addImageFromChart: has not completed values");
//    }


//    private static Image getJpegImageFromChart(JFreeChart chart, int width, int height){
//        try {
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            ChartUtilities.writeBufferedImageAsJPEG(out, chart.createBufferedImage(width, height));
//            bmp.compress( Bitmap.CompressFormat.JPEG, 97, out );
//            return Image.getInstance(out.toByteArray());
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e(TAG, "getImageFromChart: ", e);
//            return null;
//        }
//    }
}
