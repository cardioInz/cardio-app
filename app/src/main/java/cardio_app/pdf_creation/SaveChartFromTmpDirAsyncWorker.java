package cardio_app.pdf_creation;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;

import cardio_app.R;
import cardio_app.pdf_creation.param_models.BitmapFromChart;
import cardio_app.util.BitmapUtil;
import cardio_app.util.FileWalkerUtil;
import cardio_app.util.PermissionUtil;

import static android.content.ContentValues.TAG;


public class SaveChartFromTmpDirAsyncWorker extends AsyncTask<Void, Void, Void> {

    private BitmapFromChart source;
    private BitmapFromChart dest;
    private AppCompatActivity contextActivity;
    private boolean isSuccess;
    private boolean deleteSourceOnSucceed = false;

    public SaveChartFromTmpDirAsyncWorker(AppCompatActivity activity, BitmapFromChart source, BitmapFromChart dest) {
        this.contextActivity = activity;
        this.source = source;
        this.dest = dest;

        FileWalkerUtil.createDirIfNoExists(dest.getFilePathWithExt());
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        if (isSuccess) {
            File destFile = new File(dest.getFilePathWithExt());
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(destFile));
            contextActivity.sendBroadcast(intent);
            Toast.makeText(contextActivity, contextActivity.getText(R.string.chart_save_successfully), Toast.LENGTH_LONG).show();
            if (deleteSourceOnSucceed) {
                try {
                    File sourceFile = new File(source.getFilePathWithExt());
                    sourceFile.delete();
                } catch (Exception e) {
                    // silent
                }
            }
        } else {
            Toast.makeText(contextActivity, contextActivity.getText(R.string.chart_has_not_been_saved), Toast.LENGTH_LONG).show();
        }

        getProgressBar().setVisibility(View.GONE);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        getProgressBar().setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (!PermissionUtil.isStoragePermissionGranted(contextActivity)) {
            Log.e(TAG, "doInBackground: doesn't have permission to do that -> return");
            return null;
        }

        boolean isLoaded = BitmapUtil.loadBitmapFromFile(source);
        if (isLoaded) {
            dest.setBitmap(source.getBitmap());
            isSuccess = BitmapUtil.saveBitmapToFile(dest);
        } else {
            isSuccess = false;
        }
        return null;
    }

    private ProgressBar getProgressBar() {
        return (ProgressBar) contextActivity.findViewById(R.id.progressBar_save_chart_img);
    }


    public void setDeleteSourceOnSucceed(boolean deleteSourceOnSucceed) {
        this.deleteSourceOnSucceed = deleteSourceOnSucceed;
    }
}
