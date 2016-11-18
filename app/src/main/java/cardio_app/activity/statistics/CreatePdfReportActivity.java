package cardio_app.activity.statistics;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import cardio_app.R;
import cardio_app.activity.filter.FilterActivity;
import cardio_app.databinding.ContentCreatePdfReportActivityBinding;
import cardio_app.db.DbHelper;
import cardio_app.filtering.DataFilter;
import cardio_app.filtering.DataFilterModeEnum;
import cardio_app.statistics.pdf_creation.PdfAsyncWorkerCreator;
import cardio_app.viewmodel.FileLocationViewModel;

public class CreatePdfReportActivity extends AppCompatActivity {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private DbHelper dbHelper;
    private static final DataFilterModeEnum DEFAULT_DATA_FILTER = DataFilterModeEnum.NO_FILTER;
    private DataFilter dataFilter = new DataFilter(DEFAULT_DATA_FILTER);
    private final FileLocationViewModel fileLocationViewModel = new FileLocationViewModel();

    private static String DEFAULT_LOCATION_FILE = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pdf_report);

        Intent intent = getIntent();
        dataFilter = intent.getParcelableExtra("filterdata");

        fileLocationViewModel.setFileLocation(DEFAULT_LOCATION_FILE);
        fileLocationViewModel.setFileName("reportPDF"); // TODO sth wrong with binding in this simple model

        ContentCreatePdfReportActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.content_create_pdf_report_activity);
        binding.setDateFromStr(dataFilter.getDateFromStr());
        binding.setDateToStr(dataFilter.getDateToStr());
        binding.setFileLocVM(fileLocationViewModel);
    }

    public void savePdf(View view){
        if (view.getId() != R.id.savePdfBtn){
            return;
        }
        verifyStoragePermissions(this);

        String locationFile = fileLocationViewModel.getFileLocation();
        String fileName = fileLocationViewModel.getFileName();

        if (locationFile == null || locationFile.isEmpty()) {
            Toast.makeText(this, getResources().getText(R.string.location_of_file_must_be_specified), Toast.LENGTH_LONG).show();
        } else if (fileName == null || fileName.isEmpty()){
            Toast.makeText(this, getResources().getText(R.string.name_of_file_must_be_specified), Toast.LENGTH_LONG).show();
        } else {
            String fileLocationStr = String.format("%s/%s.pdf", locationFile, fileName);
            PdfAsyncWorkerCreator pdfAsyncWorkerCreator = new PdfAsyncWorkerCreator(this, fileLocationStr);
            pdfAsyncWorkerCreator.execute();
        }
    }


    public void refreshStatisticsView() {
        // if here will be some content that needs refresh
    }

    @Override
    protected void onResume() {
        refreshStatisticsView();
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filter_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter_data: {
                onFilterDataClick();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void onFilterDataClick() {
        Intent intent = new Intent(this, FilterActivity.class);
        intent.putExtra("filterdata", dataFilter);
        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                DataFilter dataFilter = data.getParcelableExtra("filterdata");
                if (dataFilter != null)
                    this.dataFilter = dataFilter;
                Toast.makeText(this, this.dataFilter.getFilterMsg(), Toast.LENGTH_LONG).show(); // TODO remove it
                refreshStatisticsView();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
//                dataFilter.setMode(DEFAULT_DATA_FILTER);
                refreshStatisticsView();
            }
        }
    }
}
