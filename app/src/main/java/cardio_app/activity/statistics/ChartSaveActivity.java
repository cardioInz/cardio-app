package cardio_app.activity.statistics;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;

import ar.com.daidalos.afiledialog.FileChooserDialog;
import cardio_app.R;
import cardio_app.databinding.ActivityChartSaveBinding;
import cardio_app.pdf_creation.SaveImageChartAsyncWorker;
import cardio_app.pdf_creation.param_models.BitmapFromChart;
import cardio_app.util.BitmapUtil;
import cardio_app.util.PermissionUtil;

public class ChartSaveActivity extends AppCompatActivity {

    final BitmapFromChart bitmapViewModel = new BitmapFromChart();
    BitmapFromChart sourceBitmapFromChart;
    private static final String DEFAULT_SAVE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_save);

        bitmapViewModel.setPath(DEFAULT_SAVE_PATH);
        // bitmapViewModel.setFileName( ??? ); // TODO make some generic name maybe sth + date?

        Intent intent = getIntent();
        sourceBitmapFromChart = intent.getParcelableExtra("bitmapFromChart");

        ActivityChartSaveBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_chart_save);
        binding.setBitmapFromChart(bitmapViewModel);
    }

    public void saveImg(View view) {
        if (PermissionUtil.isStoragePermissionGranted(this)) {
            bitmapViewModel.setExt(BitmapUtil.EXT_IMG.JPEG);
            SaveImageChartAsyncWorker worker = new SaveImageChartAsyncWorker(this, sourceBitmapFromChart, bitmapViewModel);
            worker.setDeleteSourceOnSucceed(true); // because at first we save it in tmp dir
            worker.execute();
        }
    }

    public void onChangeLocationClick_InSaveChartActivity(View view) {
        if (PermissionUtil.isStoragePermissionGranted(this)) {
            FileChooserDialog dialog = new FileChooserDialog(this, bitmapViewModel.getPath());
            dialog.setFolderMode(true);
            dialog.addListener(new FileChooserDialog.OnFileSelectedListener() {

                @Override
                public void onFileSelected(Dialog source, File file) {
                    bitmapViewModel.setPath(file.getPath());
                    bitmapViewModel.notifyChange();
                    source.hide();
                }

                @Override
                public void onFileSelected(Dialog source, File folder, String name) {

                }
            });
            dialog.show();
        }
    }
}
