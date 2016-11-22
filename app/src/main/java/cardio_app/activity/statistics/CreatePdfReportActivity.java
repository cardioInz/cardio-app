package cardio_app.activity.statistics;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.Toast;

import com.itextpdf.text.Image;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import cardio_app.R;
import cardio_app.activity.filter.FilterActivity;
import cardio_app.databinding.ActivityCreatePdfReportBinding;
import cardio_app.db.DbHelper;
import cardio_app.db.model.PressureData;
import cardio_app.filtering.DataFilter;
import cardio_app.filtering.DataFilterModeEnum;
import cardio_app.pdf_creation.PdfCreatorAsyncWorker;
import cardio_app.pdf_creation.param_models.PdfCreationDataParam;
import cardio_app.pdf_creation.param_models.PdfRecordsContainer;
import cardio_app.util.BitmapUtil;
import cardio_app.viewmodel.pdf_creation.DataFilterForPdfCreationViewModel;
import cardio_app.viewmodel.pdf_creation.PdfCreationViewModel;

public class CreatePdfReportActivity extends AppCompatActivity {
    private static final String TAG = CreatePdfReportActivity.class.toString();
    private DbHelper dbHelper;
    private static final DataFilterModeEnum DEFAULT_DATA_FILTER = DataFilterModeEnum.NO_FILTER;
    private DataFilter dataFilter = new DataFilter(DEFAULT_DATA_FILTER);
    private DataFilterForPdfCreationViewModel dataFilterForPdfCreationViewModel = new DataFilterForPdfCreationViewModel();
    private final PdfCreationViewModel pdfCreationViewModel = new PdfCreationViewModel();

    private static String DEFAULT_LOCATION_FILE = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    private static String DEFAULT_EMAIL_ADDR = "cardio.inzynierka@gmail.com";
    private static String DEFAULT_PDF_NAME = "report_PDF_TEST"; // TODO name with some dates from -> to

    private void correctVisibilities(boolean isSendOpt, boolean isSaveOpt) {
        int sendVisib = isSendOpt ? View.VISIBLE : View.GONE;
        int saveVisib = isSaveOpt ? View.VISIBLE : View.GONE;

        Button saveBtn = (Button) findViewById(R.id.savePdfBtn);
        Button sendBtn = (Button) findViewById(R.id.sendPdfBtn);
        TableLayout saveTableLayout = (TableLayout) findViewById(R.id.create_pdf_save_mode_table_layout);
        TableLayout sendTableLayout = (TableLayout) findViewById(R.id.create_pdf_send_mode_table_layout);

        saveBtn.setVisibility(saveVisib);
        sendBtn.setVisibility(sendVisib);
        saveTableLayout.setVisibility(saveVisib);
        sendTableLayout.setVisibility(sendVisib);
    }

    public void changePdfCreationMode(View view){
        int id = view.getId();
        switch (id){
            case R.id.radio_pdf_save_btn:
                pdfCreationViewModel.setSaveOpt(true);
                correctVisibilities(false, true);
                break;
            case R.id.radio_pdf_send_btn:
                pdfCreationViewModel.setSendEmailOpt(true);
                correctVisibilities(true, false);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pdf_report);

        Intent intent = getIntent();
        DataFilter df = intent.getParcelableExtra("filterdata");
        if (df != null)
            dataFilter = df;

        try {
            Date dateFrom = getHelper().getFirstDateFromPressureDataTable();
            Date dateTo = getHelper().getLastDateFromPressureDataTable();

           dataFilterForPdfCreationViewModel.setDatesBoundary(dateFrom, dateTo);
        } catch (Exception e) {
            dataFilterForPdfCreationViewModel.setDatesBoundary(null, null);
        }

        // TODO sth wrong with binding in this simple model
        pdfCreationViewModel.setLocationSave(DEFAULT_LOCATION_FILE);
        pdfCreationViewModel.setFileName(DEFAULT_PDF_NAME);
        pdfCreationViewModel.setEmailAddr(DEFAULT_EMAIL_ADDR);
        dataFilterForPdfCreationViewModel.setDataFilter(dataFilter);

        ActivityCreatePdfReportBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_create_pdf_report);
        binding.setDatesFromFilter(dataFilterForPdfCreationViewModel);
        binding.setPdfCreationVM(pdfCreationViewModel);

        correctVisibilities(true, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                DataFilter dataFilter = data.getParcelableExtra("filterdata");
                if (dataFilter != null) {
                    this.dataFilterForPdfCreationViewModel.copyValuesFrom(dataFilter);
                }
                refreshStatisticsView();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                refreshStatisticsView();
            }
        }
    }

    public PdfRecordsContainer getPdfRecordContainer(){
        return new PdfRecordsContainer(getHelper(), dataFilter.getDateFrom(), dataFilter.getDateTo());
    }

    public void savePdf(View view){
        if (view.getId() != R.id.savePdfBtn){
            return;
        }

        String locationFile = pdfCreationViewModel.getLocationSave();
        String fileName = pdfCreationViewModel.getFileName();

        if (locationFile == null || locationFile.isEmpty()) {
            Toast.makeText(this, getResources().getText(R.string.location_of_file_must_be_specified), Toast.LENGTH_LONG).show();
        } else if (fileName == null || fileName.isEmpty()){
            Toast.makeText(this, getResources().getText(R.string.name_of_file_must_be_specified), Toast.LENGTH_LONG).show();
        } else {
            PdfCreationDataParam pdfDataModel = pdfCreationViewModel.getPdfDataModel();
            PdfRecordsContainer pdfRecordsContainer = getPdfRecordContainer();
            PdfCreatorAsyncWorker pdfCreatorAsyncWorker = new PdfCreatorAsyncWorker(this, false, pdfDataModel, pdfRecordsContainer);
            pdfCreatorAsyncWorker.execute();
        }
    }

    public void sendPdf(View view) {
        if (view.getId() != R.id.sendPdfBtn){
            return;
        }

        String email = pdfCreationViewModel.getEmailAddr();
        String attachedPdfFileName = pdfCreationViewModel.getFileName();

        if (email == null || email.isEmpty()){
            Toast.makeText(this, getResources().getText(R.string.email_addr_must_be_specified), Toast.LENGTH_LONG).show();
        } else if (attachedPdfFileName == null || attachedPdfFileName.isEmpty()){
            Toast.makeText(this, getResources().getText(R.string.name_of_file_must_be_specified), Toast.LENGTH_LONG).show();
        } else {
            PdfCreationDataParam pdfDataModel = pdfCreationViewModel.getPdfDataModel();
            PdfRecordsContainer pdfRecordsContainer = getPdfRecordContainer();
            PdfCreatorAsyncWorker pdfCreatorAsyncWorker = new PdfCreatorAsyncWorker(this, true, pdfDataModel, pdfRecordsContainer);
            pdfCreatorAsyncWorker.execute();
        }
    }


    public void refreshStatisticsView() {

    }

    private DbHelper getHelper() {
        if (dbHelper == null) {
            dbHelper = OpenHelperManager.getHelper(this, DbHelper.class);
        }

        return dbHelper;
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
            case R.id.menu_item_filter_data: {
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
        intent.putExtra("filterdata", dataFilterForPdfCreationViewModel.getDataFilter());
        startActivityForResult(intent, 1);
    }
}
