package cardio_app.pdf_creation.param_models;

import android.content.res.Resources;
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

import cardio_app.util.BitmapUtil;
import lecho.lib.hellocharts.view.LineChartView;

import static android.content.ContentValues.TAG;


public class BitmapFromChart extends BaseObservable implements Parcelable, Comparable<BitmapFromChart> {

    private String path;
    private String fileName;
    private BitmapUtil.EXT_IMG ext;
    private Bitmap bitmap;
    private Date dateFrom;
    private Date dateTo;

    public BitmapFromChart(){

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

    public BitmapFromChart(LineChartView chartView, Resources resources) {
        bitmap = BitmapUtil.getBitmapFromChartView(chartView, resources);
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
        return BitmapUtil.convertBitmapToImage(bitmap);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public boolean hasCompletedValues(){
        return (hasBitmap() || hasFilePathExt()); // && hasDates();
    }

    public String getFilePathWithExt(){
        if (hasFilePathExt())
            return path + File.separator + fileName + ext;
        else
            return null;
    }

    private boolean hasDates(){
        return dateFrom != null && dateTo != null;
    }

    public boolean hasFilePathExt(){
        return ext != null && fileName != null && path != null && !fileName.isEmpty() && !path.isEmpty();
    }

    public boolean hasBitmap(){
        return bitmap != null;
    }

    public static ImageFromChartComparator getComparator(){
        return new ImageFromChartComparator();
    }

    @Override
    public int compareTo(@NonNull BitmapFromChart bitmapFromChart) {
        if (!bitmapFromChart.hasCompletedValues() && this.hasCompletedValues())
            return 1;
        else if (bitmapFromChart.hasCompletedValues() && !this.hasCompletedValues())
            return -1;
        int result = this.dateFrom.compareTo(bitmapFromChart.dateFrom);
        if (result != 0) return result;
        return this.dateTo.compareTo(bitmapFromChart.dateTo);
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

    private static class ImageFromChartComparator implements Comparator<BitmapFromChart>
    {
        public int compare(BitmapFromChart c1, BitmapFromChart c2)
        {
            if (c1 == null && c2 == null)
                return 0;
            else if (c1 == null)
                return 1;
            else if (c2 == null)
                return -1;
            else
                return c1.compareTo(c2);
        }
    }

    public String infoStrForLogger(){
        return String.format("bitmap: %s, fileName: %s, path: %s",
                bitmap == null ? "null" : "not null",
                fileName != null ? fileName : "",
                path != null ? path : "");
    }
}
