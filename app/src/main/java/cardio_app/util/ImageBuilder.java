package cardio_app.util;

import android.graphics.Bitmap;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;

import java.io.IOException;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class ImageBuilder {
    private LineChartView view;
    private ChartBuilder data;
    private int width = 1080;
    private int height = 1080;
    private int daysOnScreen = 4;

    public ImageBuilder(LineChartView view) {
        this.view = view;
    }

    public ChartBuilder getData() {
        return data;
    }

    public ImageBuilder setData(ChartBuilder data) {
        this.data = data;

        return this;
    }

    public ImageBuilder setWidth(int width) {
        this.width = width;

        return this;
    }

    public ImageBuilder setHeight(int height) {
        this.height = height;

        return this;
    }

    public ImageBuilder setDaysOnScreen(int daysOnScreen) {
        this.daysOnScreen = daysOnScreen;

        return this;
    }

    public Image build() throws IOException, BadElementException {
        view.setZoomType(ZoomType.HORIZONTAL);
        view.setLineChartData(data.build());
        Viewport viewport = view.getCurrentViewport();
        view.setZoomLevel(viewport.centerX(), viewport.centerY(), data.getDays() / daysOnScreen);
        Bitmap bitmap = BitmapUtil.generateBitmapFromView(view, width, height);

        return BitmapUtil.convertBitmapToImage(bitmap, BitmapUtil.EXT_IMG.PNG);
    }
}
