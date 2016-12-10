package cardio_app.activity.synchro;

import android.app.AlertDialog;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import ar.com.daidalos.afiledialog.FileChooserDialog;
import cardio_app.R;
import cardio_app.databinding.ActivityImportBinding;
import cardio_app.databinding.DialogWaitBinding;
import cardio_app.db.DbHelper;
import cardio_app.util.PermissionUtil;
import cardio_app.viewmodel.ImportExportViewModel;
import cardio_app.viewmodel.TitleViewModel;

public class ImportActivity extends AppCompatActivity {
    private static final String TAG = ImportActivity.class.getName();

    private ImportExportViewModel data;
    private DbHelper dbHelper;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = new ImportExportViewModel("", null);

        ActivityImportBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_import);
        binding.setData(data);
    }

    public void onChooseFileClick(View view) {
        if (PermissionUtil.isStoragePermissionGranted(this)) {
            FileChooserDialog dialog = new FileChooserDialog(this);
            dialog.addListener(new FileChooserDialog.OnFileSelectedListener() {
                @Override
                public void onFileSelected(Dialog source, File file) {
                    data.setPath(file.getPath());
                    data.notifyChange();
                    source.hide();
                }

                @Override
                public void onFileSelected(Dialog source, File folder, String name) {

                }
            });
            dialog.show();
        }
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

    public void onSaveClick(View view) {
        if (PermissionUtil.isStoragePermissionGranted(this)) {

            DialogWaitBinding binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_wait, null, false);
            binding.setText(new TitleViewModel(getResources().getString(R.string.import_data)));

            dialog = new AlertDialog.Builder(this)
                    .setView(binding.getRoot())
                    .setCancelable(false)
                    .show();

            new AsyncSave().execute();
        }
    }

    private class AsyncSave extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            BufferedReader in = null;
            StringBuilder builder = new StringBuilder();
            boolean success = true;
            try {
                in = new BufferedReader(new FileReader(data.getPath()));
                builder.append(in.readLine());

                getHelper().importFromJson(new JSONObject(builder.toString()));
            } catch (IOException | JSONException | ParseException | SQLException e) {
                e.printStackTrace();
                success = false;
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        success = false;
                    }
                }
            }

            return success;
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);

            if (dialog != null) {
                dialog.dismiss();
            }

            if (aVoid) {
                Toast.makeText(ImportActivity.this, getResources().getString(R.string.import_success), Toast.LENGTH_SHORT).show();
                onBackPressed();
            } else {
                Toast.makeText(ImportActivity.this, getResources().getString(R.string.import_error), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
