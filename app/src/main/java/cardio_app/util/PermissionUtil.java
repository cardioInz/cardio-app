package cardio_app.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by kisam on 22.11.2016.
 */

public class PermissionUtil {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_REQUIRED = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission_group.STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
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

        activity.runOnUiThread(() -> {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_REQUIRED,
                    REQUEST_EXTERNAL_STORAGE
            );
        });
    }


    public static File writeFromContextFilesDirToExternal(Context context, String filenameWithExt){
        // source: http://stackoverflow.com/questions/27956669/permission-denied-for-the-attachment-on-gmail-5-0-trying-to-attach-file-to-e
        // TODO rewrite to not use codes from internet
        try {
            File file = new File(context.getExternalFilesDir(null), filenameWithExt); //Get file location from external source
            InputStream is = new FileInputStream(context.getFilesDir() + File.separator + filenameWithExt); //get file location from internal
            OutputStream os = new FileOutputStream(file); //Open your OutputStream and pass in the file you want to write to
            byte[] toWrite = new byte[is.available()]; //Init a byte array for handing data transfer
            Log.i("Available ", is.available() + "");
            int result = is.read(toWrite); //Read the data from the byte array
            Log.i("Result", result + "");
            os.write(toWrite); //Write it to the output stream
            is.close(); //Close it
            os.close(); //Close it
            Log.i("Copying to", "" + context.getExternalFilesDir(null) + File.separator + filenameWithExt);
            Log.i("Copying from", context.getFilesDir() + File.separator + filenameWithExt + "");
            return file;
        } catch (Exception e) {
            Toast.makeText(context, "File write failed: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show(); //if there's an error, make a piece of toast and serve it up
            return null;
        }
    }

    public static String getTmpDir(Context context){
        // TODO make sure that is correct dir for tmp files (shared between activities)
        return context.getFilesDir().getAbsolutePath();
    }
}
