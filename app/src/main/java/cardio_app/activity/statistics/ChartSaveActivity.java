package cardio_app.activity.statistics;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import cardio_app.R;
import cardio_app.databinding.ActivityChartSaveBinding;
import cardio_app.pdf_creation.SaveImageChartAsyncWorker;
import cardio_app.pdf_creation.param_models.BitmapFromChart;
import cardio_app.util.BitmapUtil;

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
        bitmapViewModel.setExt(BitmapUtil.EXT_IMG.JPEG);
        SaveImageChartAsyncWorker worker = new SaveImageChartAsyncWorker(this, sourceBitmapFromChart, bitmapViewModel);
        worker.setDeleteSourceOnSucceed(true); // because at first we save it in tmp dir
        worker.execute();
    }
}
