package io.tanjundang.github.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Creator: KevinSu kevinsu917@126.com
 * Date 2015-07-21-16:23
 * Description:第一个item是照相机
 */
public class CameraPhotoUtil {
    public static Uri getOutputMediaFileUri(Context context) {
        File mediaStorageDir = new File
                (Environment.getExternalStoragePublicDirectory
                        (Environment.DIRECTORY_PICTURES),
                        "TJDCamera");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("TJDCamera", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp
                + ".jpg");
//        适配7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, "io.tanjundang.github.fileprovider", mediaFile);
        } else {
            return Uri.fromFile(mediaFile);
        }
    }
}
