package cardio_app.statistics.pdf_creation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cardio_app.R;
import cardio_app.viewmodel.pdf_creation.PdfCreationViewModel;

/**
 * Created by kisam on 18.11.2016.
 */

public class PdfAsyncWorkerCreator extends AsyncTask<Void, Void, Void> {
    private AppCompatActivity contextActivity = null;
    private static final String EXT = ".pdf";
    private Boolean isSendEmailMode = null;
    private String emailAddr = null;
    private String location = null;
    private String filename = null;
    private File file = null;

    private String DEFAULT_SUBJECT;
    private String DEFAULT_BODY;

    public PdfAsyncWorkerCreator(AppCompatActivity contextActivity, boolean isSendEmailMode, PdfCreationViewModel pdfCreationViewModel) {
        super();
        this.contextActivity = contextActivity;
        this.isSendEmailMode = isSendEmailMode;

        String LOCALE_APP_TMP_DIR = contextActivity.getCacheDir().getAbsolutePath();

        if (isSendEmailMode){
//            location = LOCALE_APP_TMP_DIR;
            location = pdfCreationViewModel.getLocationSave();
            emailAddr = pdfCreationViewModel.getEmailAddr();
        } else {
            location = pdfCreationViewModel.getLocationSave();
        }

        filename = pdfCreationViewModel.getFileName() + EXT;
        file = new File(location, filename);

        DEFAULT_SUBJECT = prepareSubject(contextActivity); // TODO subject nice
        DEFAULT_BODY = prepareBody(contextActivity); // TODO email msg body

        verifyStoragePermissions(contextActivity);
    }

    private static String prepareBody(Context contextActivity) {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") final SimpleDateFormat DATETIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateTimeStr = DATETIME_FORMATTER.format(calendar.getTime());

        return String.format("%s %s",
                contextActivity.getString(R.string.pdf_created),
                dateTimeStr);
    }

    private static String prepareSubject(Context contextActivity) {
        return String.format("%s - %s", contextActivity.getString(R.string.app_name), contextActivity.getString(R.string.pdf_report));
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        if (file.exists()) {

            if (isSendEmailMode){
                file.setReadable(true, false);
                sendEmail();
                file.deleteOnExit(); // TODO remove file after send
            } else {
                file.setReadable(true);
//                Toast.makeText(contextActivity, contextActivity.getText(R.string.pdf_report_successfuly_created), Toast.LENGTH_SHORT).show();
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
            FirstPDF.createAndSavePdf(absolutePathStr);
        }
        return null;
    }

    private ProgressBar getProgressBar() {
        return (ProgressBar) contextActivity.findViewById(R.id.progressBar_create_pdf);
    }


    private boolean verifyFileNameAndLocation(){
        if (filename == null || filename.trim().equals(EXT)) {
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
        contextActivity.runOnUiThread(() -> {
            try {
                Uri path = Uri.fromFile(file);
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                verifyStoragePermissions(contextActivity);

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
        });
    }



    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_REQUIRED = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission_group.STORAGE
    };

    private static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission

        ArrayList<String> wantThisPermissionsList = new ArrayList<>();

        for (String perm : PERMISSIONS_REQUIRED) {
            int permissionState = ActivityCompat.checkSelfPermission(activity, perm);
            if (permissionState != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                wantThisPermissionsList.add(perm);
            }
        }

        if (wantThisPermissionsList.isEmpty())
            return;

//        String [] WANT_PERMISSIONS = (String[]) wantThisPermissionsList.toArray();
        activity.runOnUiThread(() -> {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_REQUIRED,
                    REQUEST_EXTERNAL_STORAGE
            );
        });
    }
}
