package cardio_app.activity.util;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cardio_app.R;

public class ExportActivity extends AppCompatActivity {
    ObservableField<String> path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ActivityExportBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_export);

        path = new ObservableField<>(Environment.getExternalStorageDirectory().getPath());

    }
}
