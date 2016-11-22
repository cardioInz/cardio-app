package cardio_app.pdf_creation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.itextpdf.text.Image;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cardio_app.R;
import cardio_app.pdf_creation.param_models.BitmapFromChart;
import cardio_app.pdf_creation.param_models.PdfCreationDataParam;
import cardio_app.pdf_creation.param_models.PdfRecordsContainer;
import cardio_app.util.FirstPDF;
import cardio_app.util.BitmapUtil;
import cardio_app.util.PermissionUtil;

import static android.content.ContentValues.TAG;

/**
 * Created by kisam on 18.11.2016.
 */

public class PdfCreatorAsyncWorker extends AsyncTask<Void, Void, Void> {
    private PdfRecordsContainer pdfRecordsContainer;
    private AppCompatActivity contextActivity = null;
    private static final String EXT_PDF = ".pdf";
    private Boolean isSendEmailMode = null;
    private String emailAddr = null;
    private String location = null;
    private String filename = null;
    private File file = null;

    private String DEFAULT_SUBJECT;
    private String DEFAULT_BODY;

    public PdfCreatorAsyncWorker(AppCompatActivity contextActivity,
                                 boolean isSendEmailMode,
                                 PdfCreationDataParam pdfDataModel,
                                 PdfRecordsContainer pdfRecordsContainer) {
        super();
        this.pdfRecordsContainer = pdfRecordsContainer;
        this.contextActivity = contextActivity;
        this.isSendEmailMode = isSendEmailMode;

        String LOCALE_APP_TMP_DIR = PermissionUtil.getTmpDir(contextActivity);

        if (isSendEmailMode){
            location = LOCALE_APP_TMP_DIR;
            emailAddr = pdfDataModel.getEmailAddr();
        } else {
            location = pdfDataModel.getLocationSave();
        }

        filename = pdfDataModel.getFileName() + EXT_PDF;
        file = new File(location, filename);

        DEFAULT_SUBJECT = prepareSubject(contextActivity.getResources()); // TODO subject nice
        DEFAULT_BODY = prepareBody(contextActivity.getResources()); // TODO email msg body

        PermissionUtil.verifyStoragePermissions(contextActivity);
    }

    private static String prepareBody(Resources resources) {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") final SimpleDateFormat DATETIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateTimeStr = DATETIME_FORMATTER.format(calendar.getTime());

        return String.format("%s %s",
                resources.getString(R.string.pdf_created),
                dateTimeStr);
    }

    private static String prepareSubject(Resources resources) {
        return String.format("%s - %s", resources.getString(R.string.app_name), resources.getString(R.string.pdf_report));
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        if (file.exists()) {
            if (isSendEmailMode){
                file.setReadable(true, false);
                file = PermissionUtil.writeFromContextFilesDirToExternal(contextActivity, filename);
                sendEmail();
                try {
                    file.deleteOnExit();
                } catch (Exception e){
                    // silent
                }
            } else {
                file.setReadable(true);
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(Uri.fromFile(file));
                contextActivity.sendBroadcast(intent);
                Toast.makeText(contextActivity, contextActivity.getText(R.string.pdf_report_successfuly_saved), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(contextActivity, contextActivity.getText(R.string.pdf_report_has_not_been_created), Toast.LENGTH_LONG).show();
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
        if (verifyFileNameAndLocation()){
            String absolutePathStr = file.getAbsolutePath();

            // TODO chart as image
            List<Image> imageList = new ArrayList<>();
            for (BitmapFromChart bitmapFromChart : pdfRecordsContainer.getBitmapFromChartList()) {
                try {
                    Image image = BitmapUtil.convertBitmapToImage(bitmapFromChart.getBitmap());
                    imageList.add(image);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "doInBackground: ", e);
                }
            }

            FirstPDF.createAndSavePdf(absolutePathStr, imageList);
        }
        return null;
    }

    private ProgressBar getProgressBar() {
        return (ProgressBar) contextActivity.findViewById(R.id.progressBar_create_pdf);
    }


    private boolean verifyFileNameAndLocation(){
        if (filename == null || filename.trim().equals(EXT_PDF)) {
            Toast.makeText(contextActivity, contextActivity.getResources().getText(R.string.file_name_is_probably_not_set), Toast.LENGTH_LONG).show();
            return false;
        }

        if (location == null || location.isEmpty()) {
            Toast.makeText(contextActivity, contextActivity.getResources().getText(R.string.location_of_file_must_be_specified), Toast.LENGTH_LONG).show();
            return false;
        }

        String absolutePathStr = file.getAbsolutePath();
        if (absolutePathStr.isEmpty()) {
            if (isSendEmailMode) {
                Toast.makeText(contextActivity, contextActivity.getResources().getText(R.string.app_could_not_use_cache_storage_give_permission_pls), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(contextActivity, contextActivity.getResources().getText(R.string.file_location_is_probably_not_set), Toast.LENGTH_LONG).show();
            }
            return false;
        }

        return true;
    }

    private void sendEmail() {
        try {
            Uri path = Uri.fromFile(file);
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            PermissionUtil.verifyStoragePermissions(contextActivity);

            // set the type to 'email'
            emailIntent.setType("vnd.android.cursor.dir/email");
            String to[] = {emailAddr};
            emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
            // the attachment
            emailIntent.putExtra(Intent.EXTRA_STREAM, path);
            // the mail subject
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, DEFAULT_SUBJECT);
            emailIntent.putExtra(Intent.EXTRA_TEXT, DEFAULT_BODY);
            contextActivity.startActivity(Intent.createChooser(emailIntent, contextActivity.getString(R.string.sending_email)));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(contextActivity, contextActivity.getText(R.string.pdf_report_successfuly_sent), Toast.LENGTH_LONG).show();
        }
    }


}
