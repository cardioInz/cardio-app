package cardio_app.pdf_creation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import cardio_app.R;
import cardio_app.activity.statistics.ChartSaveActivity;
import cardio_app.pdf_creation.param_models.BitmapFromChart;
import cardio_app.util.BitmapUtil;
import cardio_app.util.FileWalkerUtil;

import static android.content.ContentValues.TAG;


public class SaveBitmapFromChartAsyncWorker extends AsyncTask<Void, Void, Void> {

    private BitmapFromChart bitmapFromChart;
    private boolean moveToSaveImageActivity;
    private AppCompatActivity activity;
    private boolean isSucceed = false;

    public SaveBitmapFromChartAsyncWorker(AppCompatActivity activity, BitmapFromChart bitmapFromChart, boolean moveToSaveImageActivity) {
        this.moveToSaveImageActivity = moveToSaveImageActivity;
        this.bitmapFromChart = bitmapFromChart;
        this.activity = activity;

        FileWalkerUtil.createDirIfNoExists(bitmapFromChart.getFilePathWithExt());
    }

    private static boolean assignBitmapValueFromChartView(AppCompatActivity activity, BitmapFromChart bitmapFromChart) {
        try {
            final Bitmap[] bitmap = new Bitmap[1];
            activity.runOnUiThread(() -> {
                bitmap[0] = BitmapUtil.getBitmapFromChartView(bitmapFromChart.getChartView()); // TODO make it async "in future"
                bitmapFromChart.setBitmap(bitmap[0]);
            });


            return true;
        } catch (Exception e) {
            Log.e(TAG, "BitmapFromChart: exception while trying to get bitmap from chartView", e);
            bitmapFromChart.setBitmap(null);
            return false;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        isSucceed = assignBitmapValueFromChartView(activity, bitmapFromChart);
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        if (isSucceed) {
            if (moveToSaveImageActivity) {
                Intent intent = new Intent(activity, ChartSaveActivity.class);
                intent.putExtra("bitmapFromChart", bitmapFromChart);
                activity.startActivity(intent);
            } else {
                Toast.makeText(activity, R.string.chart_save_successfully, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity, R.string.chart_could_not_be_saved, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (isSucceed) {
            isSucceed = BitmapUtil.saveBitmapToFile(bitmapFromChart);
        }
        return null;
    }
}
