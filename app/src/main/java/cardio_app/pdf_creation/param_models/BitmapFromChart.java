package cardio_app.pdf_creation.param_models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.Date;

import cardio_app.BR;
import cardio_app.util.BitmapUtil;
import lecho.lib.hellocharts.view.LineChartView;

import static android.content.ContentValues.TAG;


public class BitmapFromChart extends BaseObservable implements Parcelable{

    private String path;
    private String fileName;
    private BitmapUtil.EXT_IMG ext;
    private Bitmap bitmap;
    private LineChartView chartView;

    public BitmapFromChart(){

    }

    public BitmapFromChart(Bitmap bitmap, String path, String fileName, BitmapUtil.EXT_IMG ext){
        this.bitmap = bitmap;
        this.path = path;
        this.fileName = fileName;
        this.ext = ext;
    }

    public BitmapFromChart(Parcel in){
        try {
            path = in.readString();
            fileName = in.readString();
            ext = BitmapUtil.EXT_IMG.getExtFromStr(in.readString());
        } catch (Exception e) {
            Log.e(TAG, "BitmapFromChart: ", e);
        }
    }

    public BitmapFromChart(LineChartView chartView) {
        this.chartView = chartView;
    }



    public static final Creator<BitmapFromChart> CREATOR = new Creator<BitmapFromChart>() {
        @Override
        public BitmapFromChart createFromParcel(Parcel in) {
            return new BitmapFromChart(in);
        }

        @Override
        public BitmapFromChart[] newArray(int size) {
            return new BitmapFromChart[size];
        }
    };

    public Image getImage() throws IOException, BadElementException {
        return BitmapUtil.convertBitmapToImage(bitmap, ext);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }


    public String getFilePathWithExt(){
        if (hasFilePathExt())
            return path + File.separator + fileName + ext;
        else
            return null;
    }

    public boolean hasFilePathExt(){
        return ext != null && fileName != null && path != null && !fileName.isEmpty() && !path.isEmpty();
    }

    public boolean hasBitmap(){
        return bitmap != null;
    }

    @Bindable
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Bindable
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(path);
        parcel.writeString(fileName);
        parcel.writeString(ext.toString());
    }

    public BitmapUtil.EXT_IMG getExt() {
        return ext;
    }

    public void setExt(BitmapUtil.EXT_IMG ext) {
        this.ext = ext;
    }


    public LineChartView getChartView() {
        return chartView;
    }


    String infoStrForLogger(){
        return String.format("bitmap: %s, fileName: %s, path: %s",
                bitmap == null ? "null" : "not null",
                fileName != null ? fileName : "",
                path != null ? path : "");
    }

    public void setExtPNG(){
        this.ext = BitmapUtil.EXT_IMG.PNG;
    }

//    public void setExtJPEG(){
//        this.ext = BitmapUtil.EXT_IMG.JPEG;
//    }

}
