package cardio_app.pdf_creation.param_models;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;

import java.io.IOException;
import java.util.Comparator;
import java.util.Date;

import cardio_app.pdf_creation.utils.ImageUtil;
import lecho.lib.hellocharts.view.LineChartView;


public class ImageFromChart implements Comparable<ImageFromChart> {

    private Bitmap bitmap;
    private Date dateFrom;
    private Date dateTo;

    public ImageFromChart(LineChartView chartView) {
        Bitmap bitmap = ImageUtil.getBitmapFromChartView(chartView);
    }

    public Image getImage() throws IOException, BadElementException {
        return ImageUtil.convertBitmapToImage(bitmap);
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
        return bitmap != null; // && dateFrom != null && dateTo != null;
    }

    public static ImageFromChartComparator getComparator(){
        return new ImageFromChartComparator();
    }

    @Override
    public int compareTo(@NonNull ImageFromChart imageFromChart) {
        if (!imageFromChart.hasCompletedValues() && this.hasCompletedValues())
            return 1;
        else if (imageFromChart.hasCompletedValues() && !this.hasCompletedValues())
            return -1;
        int result = this.dateFrom.compareTo(imageFromChart.dateFrom);
        if (result != 0) return result;
        return this.dateTo.compareTo(imageFromChart.dateTo);
    }

    static class ImageFromChartComparator implements Comparator<ImageFromChart>
    {
        public int compare(ImageFromChart c1, ImageFromChart c2)
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
}
