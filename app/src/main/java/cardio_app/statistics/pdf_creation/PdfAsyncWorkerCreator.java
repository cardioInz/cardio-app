package cardio_app.statistics.pdf_creation;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import cardio_app.R;

/**
 * Created by kisam on 18.11.2016.
 */

public class PdfAsyncWorkerCreator extends AsyncTask<Void, Void, Void> {

    private AppCompatActivity contextActivity = null;
    private String fileLocation = null;

    public PdfAsyncWorkerCreator(AppCompatActivity contextActivity, String fileLocation) {
        super();
        this.contextActivity = contextActivity;
        this.fileLocation = fileLocation;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        if(contextActivity == null)
            return;

        // TODO action after background work
        getProgressBar().setVisibility(View.GONE);
        Toast.makeText(contextActivity, contextActivity.getText(R.string.pdf_report_successfuly_created), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (contextActivity == null)
            return;

        // TODO show progress here
        getProgressBar().setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (fileLocation == null || fileLocation.isEmpty()) {
            Toast.makeText(contextActivity, contextActivity.getResources().getText(R.string.file_location_is_probably_not_set), Toast.LENGTH_LONG).show();
        } else {
            FirstPDF.createAndSavePdf(fileLocation);
        }
        return null;
    }

    private ProgressBar getProgressBar() {
        return (ProgressBar) contextActivity.findViewById(R.id.progressBar_create_pdf);
    }
}
