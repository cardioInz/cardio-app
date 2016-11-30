package cardio_app.pdf_creation;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cardio_app.R;
import cardio_app.pdf_creation.param_models.BitmapFromChart;
import cardio_app.pdf_creation.param_models.PdfChosenParams;
import cardio_app.pdf_creation.param_models.PdfRecordsContainer;
import cardio_app.util.CreatePdfUtil;
import cardio_app.util.BitmapUtil;
import cardio_app.util.DateTimeUtil;
import cardio_app.util.Defaults;
import cardio_app.util.PermissionUtil;

import static android.content.ContentValues.TAG;


public class PdfCreatorAsyncWorker extends AsyncTask<Void, Void, Void> {

    private static boolean ADD_EXTRA_CHARTS = true;
    private static boolean ADD_SIMPLE_CHARTS = false; // TODO , false because simple is too difficult :(

    private PdfRecordsContainer pdfRecordsContainer;
    private AppCompatActivity contextActivity = null;
    private static final String EXT_PDF = ".pdf";
    private Boolean isSendEmailMode = null;
    private String emailAddr = null;
    private String location = null;
    private String filename = null;
    private File file = null;

    public PdfCreatorAsyncWorker(AppCompatActivity contextActivity,
                                 boolean isSendEmailMode,
                                 PdfChosenParams pdfChosenParams,
                                 PdfRecordsContainer pdfRecordsContainer) {
        super();
        this.pdfRecordsContainer = pdfRecordsContainer;

        this.pdfRecordsContainer.setExtra_bitmapFromChartList(pdfChosenParams.getExtraBitmapFromChartList());
        this.contextActivity = contextActivity;
        this.isSendEmailMode = isSendEmailMode;

        String LOCALE_APP_TMP_DIR = PermissionUtil.getTmpDir(contextActivity);

        if (isSendEmailMode){
            location = LOCALE_APP_TMP_DIR;
            emailAddr = pdfChosenParams.getEmailAddr();
        } else {
            location = pdfChosenParams.getLocationSave();
        }

        filename = pdfChosenParams.getFileName() + EXT_PDF;
        file = new File(location, filename);
    }



    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        if (file.exists()) {
            if (isSendEmailMode){
                file.setReadable(true, false);
                file = PermissionUtil.writeFromContextFilesDirToExternal(contextActivity, filename);
                sendEmail();
                // TODO delete file after send action
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
        pdfRecordsContainer.initRecordsByHelper();
        initCharts();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (verifyFileNameAndLocation()){
            List<Image> imageList = new ArrayList<>();

            if (ADD_EXTRA_CHARTS) {
                List<BitmapFromChart> extraCharts = pdfRecordsContainer.getExtra_bitmapFromChartList();
                List<Image> extraImageList = prepareImagesToPdf(extraCharts);
                imageList.addAll(extraImageList);
            }

            if (ADD_SIMPLE_CHARTS) {
                List<BitmapFromChart> simpleCharts = pdfRecordsContainer.getSimple_bitmapFromChartList();
                List<Image> simpleImageList = prepareImagesToPdf(simpleCharts); // should be empty now
                imageList.addAll(simpleImageList);
            }


            String absolutePathStr = file.getAbsolutePath();
            CreatePdfUtil.createAndSavePdf(absolutePathStr, pdfRecordsContainer, imageList, contextActivity.getResources());
        }
        return null;
    }


    private void initCharts() {
        if (ADD_EXTRA_CHARTS) {
            pdfRecordsContainer.cleanExtraBitmapWithoutPaths();
            for (BitmapFromChart fromChart : pdfRecordsContainer.getExtra_bitmapFromChartList()) {
                if (!BitmapUtil.loadBitmapFromFile(fromChart))
                    continue;
                BitmapUtil.loadBitmapFromFile(fromChart);
            }
        }

        if (ADD_SIMPLE_CHARTS) {
            // TODO generate charts with dates from-to
        }
    }

    private static List<Image> prepareImagesToPdf(List<BitmapFromChart> list){
        List<Image> imageList = new ArrayList<>();
        for (BitmapFromChart bitmapFromChart : list) {
            try {
                Image image = bitmapFromChart.getImage();
                imageList.add(image);
                bitmapFromChart.setBitmap(null);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "doInBackground: ", e);
            }
        }
        return imageList;
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
        if (!PermissionUtil.isStoragePermissionGranted(contextActivity)){
            Log.e(TAG, "sendEmail: does not have parmision -> return");
            return;
        }

        try {
            Uri path = Uri.fromFile(file);
            Intent emailIntent = new Intent(Intent.ACTION_SEND);

            // set the type to 'email'
            emailIntent.setType("vnd.android.cursor.dir/email");
            String to[] = {emailAddr};
            emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
            // the attachment
            emailIntent.putExtra(Intent.EXTRA_STREAM, path);
            // the mail subject

            String subject = Defaults.prepareSubject(contextActivity.getResources());
            String body = Defaults.prepareBody(contextActivity.getResources());
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, body);

            contextActivity.startActivity(Intent.createChooser(emailIntent, contextActivity.getString(R.string.sending_email)));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(contextActivity, contextActivity.getText(R.string.pdf_report_successfuly_sent), Toast.LENGTH_LONG).show();
        }
    }


}
