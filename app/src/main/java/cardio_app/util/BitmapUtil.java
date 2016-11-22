package cardio_app.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import cardio_app.R;
import cardio_app.pdf_creation.param_models.BitmapFromChart;
import lecho.lib.hellocharts.view.AbstractChartView;

/**
 * Created by kisam on 20.11.2016.
 */

public class BitmapUtil {

    private static final String TAG = BitmapUtil.class.toString();

    public static Bitmap getBitmapFromChartView(AbstractChartView abstractChartView, Resources resources){
        abstractChartView.setDrawingCacheEnabled(true);
        Drawable lastBg = abstractChartView.getBackground();
        abstractChartView.setBackgroundColor(resources.getColor(R.color.white));
        abstractChartView.buildDrawingCache();
        Bitmap bm = abstractChartView.getDrawingCache();
        Bitmap bitmap = bm.copy(bm.getConfig(), true);
        abstractChartView.setDrawingCacheEnabled(false);
        abstractChartView.destroyDrawingCache();
        abstractChartView.setBackground(lastBg);
        return bitmap;
    }

    private static Image compressBitmapToImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.JPEG, 97, baos );
        try {
            baos.close();
        } catch (IOException e) {
            Log.e(TAG, "compressBitmapToImage: ", e);
            e.printStackTrace();
        }
        try {
            return Image.getInstance(baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "compressBitmapToImage: ", e);
            return null;
        }
    }

    public static Image convertBitmapToImage(Bitmap bitmap) throws IOException, BadElementException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , baos);
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

    private static boolean saveBitmapToFile(Bitmap bitmap, String filePath){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e) {
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
        return saveBitmapToFile(bitmap, path + File.separator + fileName + ext.toString());
    }

    public static boolean saveBitmapToFile(BitmapFromChart bfc){
        if (!bfc.hasFilePathExt())
            return false;
        return saveBitmapToFile(bfc.getBitmap(), bfc.getFilePathWithExt());
    }


    public enum EXT_IMG {
        PNG,
        JPEG;

        private static HashMap<EXT_IMG, String> mapStr = new HashMap<EXT_IMG, String>(){
            {
                put(JPEG, ".jpeg");
                put(PNG, ".png");
            }
        };

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
    }




}
