package cardio_app.activity.synchro;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

import ar.com.daidalos.afiledialog.FileChooserDialog;
import cardio_app.R;
import cardio_app.databinding.ActivityExportBinding;
import cardio_app.db.DbHelper;
import cardio_app.util.PermissionUtil;
import cardio_app.viewmodel.ImportExportViewModel;

public class ExportActivity extends AppCompatActivity {
    private static final String TAG = ExportActivity.class.getName();
    private static final String EXTENSION = ".json";
    private static final String DEFAULT_FILE_NAME = "db_copy";

    ImportExportViewModel viewModel;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityExportBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_export);

        viewModel = new ImportExportViewModel(Environment.getExternalStorageDirectory().getPath(), DEFAULT_FILE_NAME);
        binding.setView(viewModel);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (dbHelper != null) {
            OpenHelperManager.releaseHelper();
            dbHelper = null;
        }
    }

    public DbHelper getHelper() {
        if (dbHelper == null) {
            dbHelper = OpenHelperManager.getHelper(this, DbHelper.class);
        }

        return dbHelper;
    }

    public void onChangeLocationClick(View view) {
        if (PermissionUtil.isStoragePermissionGranted(this)) {
            FileChooserDialog dialog = new FileChooserDialog(this, viewModel.getPath());
            dialog.setFolderMode(true);
            dialog.addListener(new FileChooserDialog.OnFileSelectedListener() {

                @Override
                public void onFileSelected(Dialog source, File file) {
                    viewModel.setPath(file.getPath());
                    viewModel.notifyChange();
                    source.hide();
                }

                @Override
                public void onFileSelected(Dialog source, File folder, String name) {

                }
            });
            dialog.show();
        }
    }

    public void onSaveClick(View view) {
        if (PermissionUtil.isStoragePermissionGranted(this)) {
            String path = viewModel.getPath() + "/" + viewModel.getFileName() + EXTENSION;

            BufferedWriter out = null;
            boolean successfullWrite = true;
            try {
                JSONObject object = getHelper().exportToJson();
                out = new BufferedWriter(new FileWriter(new File(path)));
                out.write(object.toString());
            } catch (SQLException | JSONException | IOException e) {
                e.printStackTrace();
                successfullWrite = false;
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        successfullWrite = false;
                        e.printStackTrace();
                    }
                }
            }

            if (successfullWrite) {
                Toast.makeText(this, R.string.toast_export, Toast.LENGTH_SHORT).show();
                onBackPressed();
            } else {
                Toast.makeText(this, R.string.export_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
