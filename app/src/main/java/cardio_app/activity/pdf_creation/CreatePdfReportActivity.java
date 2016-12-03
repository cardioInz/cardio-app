package cardio_app.activity.pdf_creation;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.io.File;
import java.util.Date;

import ar.com.daidalos.afiledialog.FileChooserDialog;
import cardio_app.R;
import cardio_app.activity.filter.FilterActivity;
import cardio_app.databinding.ActivityCreatePdfReportBinding;
import cardio_app.db.DbHelper;
import cardio_app.filtering.DataFilter;
import cardio_app.pdf_creation.PdfCreatorAsyncWorker;
import cardio_app.pdf_creation.param_models.PdfChosenParams;
import cardio_app.pdf_creation.param_models.PdfRecordsContainer;
import cardio_app.util.Defaults;
import cardio_app.util.FileWalkerUtil;
import cardio_app.util.PermissionUtil;
import cardio_app.viewmodel.pdf_creation.DataFilterForPdfCreationViewModel;
import cardio_app.viewmodel.pdf_creation.PdfCreationViewModel;
import lecho.lib.hellocharts.view.LineChartView;

public class CreatePdfReportActivity extends AppCompatActivity {
    private static final String TAG = CreatePdfReportActivity.class.toString();
    private static String DEFAULT_LOCATION_FILE = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    private final PdfCreationViewModel pdfCreationViewModel = new PdfCreationViewModel();
    private DbHelper dbHelper;
    private DataFilter dataFilter = Defaults.getDefaultDataFilter();
    private DataFilterForPdfCreationViewModel dataFilterForPdfCreationViewModel = new DataFilterForPdfCreationViewModel();

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

    public void changePdfCreation_SendeSaveOptMode(View view) {
        int id = view.getId();
        switch (id) {
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

        pdfCreationViewModel.setLocationSave(DEFAULT_LOCATION_FILE);
        pdfCreationViewModel.setEmailAddr(Defaults.DEFAULT_EMAIL);
        dataFilterForPdfCreationViewModel.setDataFilter(dataFilter);
        setGenericFileName();
        pdfCreationViewModel.setExtraChartsList(FileWalkerUtil.getBitmapFromChartList_fromSavedDir());

        ActivityCreatePdfReportBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_create_pdf_report);
        binding.setDatesFromFilter(dataFilterForPdfCreationViewModel);
        binding.setPdfCreationVM(pdfCreationViewModel);

        correctVisibilities(true, false);
    }

    private void setGenericFileName() {
        pdfCreationViewModel.setFileName(FileWalkerUtil.getSomeUniquePdfName(
                dataFilterForPdfCreationViewModel.getDateFromStr(),
                dataFilterForPdfCreationViewModel.getDateToStr()
        ));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                DataFilter dataFilter = data.getParcelableExtra("filterdata");
                if (dataFilter != null) {
                    this.dataFilterForPdfCreationViewModel.copyValuesFrom(dataFilter);
                    setGenericFileName();
                }
                refreshContentView();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                refreshContentView();
            }
        }
    }

    public PdfRecordsContainer getPdfRecordContainer() {
        return new PdfRecordsContainer(getHelper(), dataFilter.getDateFrom(), dataFilter.getDateTo());
    }

    public void savePdf(View view) {
        if (PermissionUtil.isStoragePermissionGranted(this)) {
            if (view.getId() != R.id.savePdfBtn) {
                return;
            }

            String locationFile = pdfCreationViewModel.getLocationSave();
            String fileName = pdfCreationViewModel.getFileName();

            if (locationFile == null || locationFile.isEmpty()) {
                Toast.makeText(this, getResources().getText(R.string.location_of_file_must_be_specified), Toast.LENGTH_LONG).show();
            } else if (fileName == null || fileName.isEmpty()) {
                Toast.makeText(this, getResources().getText(R.string.name_of_file_must_be_specified), Toast.LENGTH_LONG).show();
            } else {
                PdfChosenParams pdfDataModel = pdfCreationViewModel.getPdfDataModel();
                PdfRecordsContainer pdfRecordsContainer = getPdfRecordContainer();
                PdfCreatorAsyncWorker pdfCreatorAsyncWorker = new PdfCreatorAsyncWorker(this, false, pdfDataModel, pdfRecordsContainer, new LineChartView(this));
                pdfCreatorAsyncWorker.execute();
            }
        }
    }

    public void sendPdf(View view) {
        if (PermissionUtil.isStoragePermissionGranted(this)) {
            if (view.getId() != R.id.sendPdfBtn) {
                return;
            }

            String email = pdfCreationViewModel.getEmailAddr();
            String attachedPdfFileName = pdfCreationViewModel.getFileName();

            if (email == null || email.isEmpty()) {
                Toast.makeText(this, getResources().getText(R.string.email_addr_must_be_specified), Toast.LENGTH_LONG).show();
            } else if (attachedPdfFileName == null || attachedPdfFileName.isEmpty()) {
                Toast.makeText(this, getResources().getText(R.string.name_of_file_must_be_specified), Toast.LENGTH_LONG).show();
            } else {
                PdfChosenParams pdfDataModel = pdfCreationViewModel.getPdfDataModel();
                PdfRecordsContainer pdfRecordsContainer = getPdfRecordContainer();
                PdfCreatorAsyncWorker pdfCreatorAsyncWorker = new PdfCreatorAsyncWorker(this, true, pdfDataModel, pdfRecordsContainer, new LineChartView(this));
                pdfCreatorAsyncWorker.execute();
            }
        }
    }

    public void onChangeLocationClick_InCreatePdfReportActivity(View view) {
        if (PermissionUtil.isStoragePermissionGranted(this)) {
            FileChooserDialog dialog = new FileChooserDialog(this, pdfCreationViewModel.getLocationSave());
            dialog.setFolderMode(true);
            dialog.addListener(new FileChooserDialog.OnFileSelectedListener() {

                @Override
                public void onFileSelected(Dialog source, File file) {
                    pdfCreationViewModel.setLocationSave(file.getPath());
                    pdfCreationViewModel.notifyChange();
                    source.hide();
                }

                @Override
                public void onFileSelected(Dialog source, File folder, String name) {

                }
            });
            dialog.show();
        }
    }


    public void refreshContentView() {
        boolean isSaveOpt = ((RadioButton) findViewById(R.id.radio_pdf_save_btn)).isChecked();
        pdfCreationViewModel.setExtraChartsList(FileWalkerUtil.getBitmapFromChartList_fromSavedDir());
        findViewById(R.id.chosen_charts_cnt_text_view).invalidate();
        ActivityCreatePdfReportBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_create_pdf_report);
        binding.setPdfCreationVM(pdfCreationViewModel);
        binding.setDatesFromFilter(dataFilterForPdfCreationViewModel);
        correctVisibilities(!isSaveOpt, isSaveOpt);
    }

    private DbHelper getHelper() {
        if (dbHelper == null) {
            dbHelper = OpenHelperManager.getHelper(this, DbHelper.class);
        }

        return dbHelper;
    }

    @Override
    protected void onResume() {
        refreshContentView();
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filter_data, menu);

        MenuItem menuItem = menu.findItem(R.id.menu_item_chart);
        menuItem.setEnabled(false);
        menuItem.setVisible(false);

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

    public void getChartsOnClick(View view) {
        Intent intent = new Intent(this, CollectedChartsActivity.class);
        startActivity(intent);
    }
}
