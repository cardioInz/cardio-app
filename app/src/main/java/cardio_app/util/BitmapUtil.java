package cardio_app.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

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


    static Bitmap generateBitmapFromView(View view, int width, int height) {
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        int widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);

        view.measure(widthSpec, heightSpec);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(canvas);

        return result;
    }

    private static Bitmap getBitmapFromView(View view) {
        int w = view.getWidth();
        int h = view.getHeight();

        if (w <= 0 || h <= 0)
            return null;
        int measuredW = View.MeasureSpec.makeMeasureSpec(w, View.MeasureSpec.EXACTLY);
        int measuredH = View.MeasureSpec.makeMeasureSpec(h, View.MeasureSpec.EXACTLY);

        view.measure(measuredW, measuredH);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        Bitmap b = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);

        view.draw(c);
        return b;
    }


    public static Bitmap getBitmapFromChartView(View abstractChartView) {
        Drawable lastBg = abstractChartView.getBackground();
        abstractChartView.setBackgroundColor(Color.WHITE);
        Bitmap bitmap = getBitmapFromView(abstractChartView);
        abstractChartView.setBackground(lastBg);
        return bitmap;
    }


    public static Image convertBitmapToImage(Bitmap bitmap, EXT_IMG ext) throws IOException, BadElementException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(ext.getCompression(), 100, baos);
        Image image = Image.getInstance(baos.toByteArray());
        image.setAlignment(Image.MIDDLE);
        return image;
    }

    private static Bitmap loadBitmapFromFile(String path, String fileName, EXT_IMG ext) {
        return loadBitmapFromFile(path + File.separator + fileName + ext.toString());
    }

    private static Bitmap loadBitmapFromFile(String filePath) {
        return BitmapFactory.decodeFile(filePath);
    }

    public static boolean loadBitmapFromFile(BitmapFromChart bitmapFromChart) {
        if (!bitmapFromChart.hasFilePathExt())
            return false;
        Bitmap bitmap = loadBitmapFromFile(bitmapFromChart.getFilePathWithExt());
        bitmapFromChart.setBitmap(bitmap);
        return bitmap != null;
    }

    private static boolean saveBitmapToFile(Bitmap bitmap, String filePath, EXT_IMG ext) {
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
            } catch (Exception e) {
                Log.e(TAG, "saveBitmapToFile: ", e);
            }
        }

        File file = new File(filePath);
        boolean result = file.exists();
        if (result)
            file.setReadable(true);
        return result;
    }

    private static boolean saveBitmapToFile(Bitmap bitmap, String path, String fileName, EXT_IMG ext) {
        return saveBitmapToFile(bitmap, path + File.separator + fileName + ext.toString(), ext);
    }

    public static boolean saveBitmapToFile(BitmapFromChart bfc) {
        if (!bfc.hasFilePathExt())
            return false;
        return saveBitmapToFile(bfc.getBitmap(), bfc.getFilePathWithExt(), bfc.getExt());
    }


    public enum EXT_IMG {

        //JPEG,
        PNG;

        private static final HashMap<EXT_IMG, String> mapStr = new HashMap<EXT_IMG, String>() {
            {
                //put(JPEG, ".jpeg");
                put(PNG, ".png");
            }
        };

        private static final HashMap<EXT_IMG, Bitmap.CompressFormat> mapToCompression = new HashMap<EXT_IMG, Bitmap.CompressFormat>() {
            {
                //put(JPEG, Bitmap.CompressFormat.JPEG);
                put(PNG, Bitmap.CompressFormat.PNG);
            }
        };

        public static EXT_IMG getExtFromStr(String str) {
            if (!mapStr.containsValue(str))
                return null;
            for (EXT_IMG key : mapStr.keySet()) {
                if (mapStr.get(key).equals(str))
                    return key;
            }
            return null;
        }

        @Override
        public String toString() {
            return mapStr.get(this);
        }

        public Bitmap.CompressFormat getCompression() {
            return mapToCompression.get(this);
        }
    }
}
