package cardio_app.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class PermissionUtil {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String TAG = PermissionUtil.class.toString();
    private static String[] PERMISSIONS_REQUIRED = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static File writeFromContextFilesDirToExternal(Context context, String filenameWithExt) {
        try {
            final String copyFromFilePath = context.getFilesDir() + File.separator + filenameWithExt;

            File file = new File(context.getExternalFilesDir(null), filenameWithExt);
            final String copyToFilePath = file.getAbsolutePath();

            InputStream is = new FileInputStream(copyFromFilePath);
            OutputStream os = new FileOutputStream(file);

            byte[] toWrite = new byte[is.available()];
            Log.i(TAG, "writeFromContextFilesDirToExternal: available: " + is.available());

            int result = is.read(toWrite);
            Log.i(TAG, "writeFromContextFilesDirToExternal: result: " + result);
            os.write(toWrite);

            is.close();
            os.close();

            String info = String.format("copying\n\tfrom:\t%s\n\tto:\t%s", copyFromFilePath, copyToFilePath);
            Log.i(TAG, "writeFromContextFilesDirToExternal: " + info);

            return file;
        } catch (Exception e) {
            Toast.makeText(context, "File write failed: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show(); //if there's an error, make a piece of toast and serve it up
            return null;
        }
    }

    public static String getTmpDir(Context context) {
        // TODO make sure that is correct dir for tmp files (shared between activities)
        return context.getFilesDir().getAbsolutePath();
    }


    public static boolean isStoragePermissionGranted(AppCompatActivity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            ArrayList<String> wantThisPermissionsList = new ArrayList<>();

            for (String perm : PERMISSIONS_REQUIRED) {
                int permissionState = ActivityCompat.checkSelfPermission(activity, perm);
                if (permissionState != PackageManager.PERMISSION_GRANTED) {
                    // We don't have permission so prompt the user
                    wantThisPermissionsList.add(perm);
                }
            }

            if (wantThisPermissionsList.isEmpty()) {
                Log.v(TAG, "Permissions is granted");
                return true;
            }

            Log.v(TAG, "Permissions is revoked");
            activity.runOnUiThread(() -> {
                ActivityCompat.requestPermissions(
                        activity,
                        PERMISSIONS_REQUIRED,
                        REQUEST_EXTERNAL_STORAGE
                );
            });
            return false;
        } else {
            Log.v(TAG, "Permissions is granted");
            return true;
        }

    }
}
