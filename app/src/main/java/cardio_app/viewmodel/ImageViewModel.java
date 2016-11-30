package cardio_app.viewmodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import cardio_app.pdf_creation.param_models.BitmapFromChart;
import cardio_app.util.BitmapUtil;


public class ImageViewModel extends ImageView {

    public ImageViewModel(Context context, BitmapFromChart bitmapFromChart){
        super(context);
        if(BitmapUtil.loadBitmapFromFile(bitmapFromChart)) {
            Bitmap bitmap = bitmapFromChart.getBitmap();
            Drawable d = new BitmapDrawable(getResources(), bitmap);
            this.setImageDrawable(d);
        }
    }

    public ImageViewModel(Context context) {
        super(context);
    }

    public ImageViewModel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewModel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        double scale = getMeasuredWidth() / widthMeasureSpec;
        int height = (int) (scale * heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), height); //Snap to width
    }
}
