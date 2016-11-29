package cardio_app.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import cardio_app.pdf_creation.param_models.BitmapFromChart;

public class BitmapUtil {

    private static final String TAG = BitmapUtil.class.toString();

    public static Bitmap generateBitmapFromView(View view, int width, int height) {
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        int widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);

        view.measure(widthSpec, heightSpec);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(canvas);

        return result;
    }

    private static Bitmap getBitmapFromView(View view)
    {
        // TODO small rewrite of this code

        //Get the dimensions of the view so we can re-layout the view at its current size
        //and create a bitmap of the same size
        int width = view.getWidth();
        int height = view.getHeight();

        if (width > 0 && height > 0 ) {
            int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
            int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);

            //Cause the view to re-layout
            view.measure(measuredWidth, measuredHeight);
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

            //Create a bitmap backed Canvas to draw the view into
            Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);

            //Now that the view is laid out and we have a canvas, ask the view to draw itself into the canvas
            view.draw(c);
            return b;
        } else {
            return null;
        }
    }


    private static Bitmap getBitmapWithSpecifiedSize(View chartView, int x, int y) {
        ViewGroup.LayoutParams paramsBackup = chartView.getLayoutParams();
        ViewGroup.LayoutParams params = chartView.getLayoutParams();
        params.height = y;
        params.width = x;
        chartView.setLayoutParams(params);
        Bitmap bitmap = getBitmapFromView(chartView);
        chartView.setLayoutParams(paramsBackup);
        return bitmap;
    }


    public static Bitmap getBitmapFromChartView(View abstractChartView) {
        Drawable lastBg = abstractChartView.getBackground();
        abstractChartView.setBackgroundColor(Color.WHITE);
        Bitmap bitmap = getBitmapFromView(abstractChartView);
        abstractChartView.setBackground(lastBg);
        return bitmap;
    }



//    private static Image compressBitmapToImage(Bitmap bitmap) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress( Bitmap.CompressFormat.JPEG, 97, baos );
//        try {
//            baos.close();
//        } catch (IOException e) {
//            Log.e(TAG, "compressBitmapToImage: ", e);
//            e.printStackTrace();
//        }
//        try {
//            return Image.getInstance(baos.toByteArray());
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e(TAG, "compressBitmapToImage: ", e);
//            return null;
//        }
//    }

    public static Image convertBitmapToImage(Bitmap bitmap, EXT_IMG ext) throws IOException, BadElementException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(ext.getCompression(), 100 , baos);
        Image image = Image.getInstance(baos.toByteArray());
        image.setAlignment(Image.MIDDLE);
        return image;
    }

    private static Bitmap loadBitmapFromFile(String path, String fileName, EXT_IMG ext){
        return loadBitmapFromFile(path + File.separator + fileName + ext.toString());
    }

    private static Bitmap loadBitmapFromFile(String filePath){
        return BitmapFactory.decodeFile(filePath);
    }

    public static boolean loadBitmapFromFile(BitmapFromChart bitmapFromChart){
        if (!bitmapFromChart.hasFilePathExt())
            return false;
        Bitmap bitmap = loadBitmapFromFile(bitmapFromChart.getFilePathWithExt());
        bitmapFromChart.setBitmap(bitmap);
        return bitmap != null;
    }

    private static boolean saveBitmapToFile(Bitmap bitmap, String filePath, EXT_IMG ext){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            bitmap.compress(ext.getCompression(), 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (Exception e){
                Log.e(TAG, "saveBitmapToFile: ", e);
            }
        }

        File file = new File(filePath);
        boolean result = file.exists();
        if(result)
            file.setReadable(true);
        return result;
    }

    private static boolean saveBitmapToFile(Bitmap bitmap, String path, String fileName, EXT_IMG ext){
        return saveBitmapToFile(bitmap, path + File.separator + fileName + ext.toString(), ext);
    }

    public static boolean saveBitmapToFile(BitmapFromChart bfc){
        if (!bfc.hasFilePathExt())
            return false;
        return saveBitmapToFile(bfc.getBitmap(), bfc.getFilePathWithExt(), bfc.getExt());
    }


    public enum EXT_IMG {

//        JPEG,
        PNG;

        private static final HashMap<EXT_IMG, String> mapStr = new HashMap<EXT_IMG, String>(){
            {
//                put(JPEG, ".jpeg");
                put(PNG, ".png");
            }
        };

        private static final HashMap<EXT_IMG, Bitmap.CompressFormat> mapToCompression = new HashMap<EXT_IMG, Bitmap.CompressFormat>(){
            {
//                put(JPEG, Bitmap.CompressFormat.JPEG);
                put(PNG, Bitmap.CompressFormat.PNG);
            }
        };

        // Bitmap.CompressFormat.PNG
        @Override
        public String toString(){
            return mapStr.get(this);
        }

        public static EXT_IMG getExtFromStr(String str){
            if (!mapStr.containsValue(str))
                return null;
            for (EXT_IMG key : mapStr.keySet()) {
                if (mapStr.get(key).equals(str))
                    return key;
            }
            return null;
        }

        public Bitmap.CompressFormat getCompression(){
            return mapToCompression.get(this);
        }
    }


//    public static Bitmap getBitmapFromChartView_oldWay(AbstractChartView abstractChartView, AppCompatActivity activity) throws Exception {
//        // in this method we need to have layout already displayed
//
//        try {
//            abstractChartView.setDrawingCacheEnabled(true);
//            Drawable lastBg = abstractChartView.getBackground();
////            abstractChartView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
//            abstractChartView.setBackgroundColor(Color.WHITE);
//            abstractChartView.buildDrawingCache();
//            Bitmap bm = abstractChartView.getDrawingCache();
//            Bitmap bitmap = bm.copy(bm.getConfig(), true);
//            abstractChartView.setDrawingCacheEnabled(false);
//            abstractChartView.destroyDrawingCache();
//            abstractChartView.setBackground(lastBg);
//            return bitmap;
//        } catch (Exception e) {
//            Log.e(TAG, "getBitmapFromChartView: ", e);
//            throw e; // possible exception while getting bitmap from drawing cache
//        }
//    }



//    public static Bitmap getBitmapFromView_v2(View v){
//        try {
//            if (v.getMeasuredHeight() <= 0) {
//                return getBitmapWithSpecifiedSize(v, 842, 595);
//            } else {
//                Bitmap b = Bitmap.createBitmap(v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
//                Canvas c = new Canvas(b);
//                v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
//                v.draw(c);
//                return b;
//            }
//
//        } catch (Exception e) {
//            Log.e(TAG, "getBitmapFromView: ", e);
//            return null;
//        }
//    }
}
