package io.tanjundang.github.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: TanJunDang
 * @Email: TanJunDang@126.com
 * @Date: 2017/5/29
 * @Description: 用于图片压缩
 */

public class PhotoCompress {
    private static Context context;

    public PhotoCompress(Context context) {
        this.context = context;
    }

    public File compress(String path) throws Exception {
        File outputFile = new File(path);
//        获取图片后缀
        String suffix = path.substring(path.lastIndexOf("."));
        BitmapFactory.Options options = new BitmapFactory.Options();
//        只获取待压缩图片大小不获取图片信息
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int width = options.outWidth;
        int height = options.outHeight;


        long filesize = outputFile.length();
        long maxFileSize = 200 * 1024;
        if (filesize > maxFileSize) {
            double scale = Math.sqrt(filesize / maxFileSize);
            options.inSampleSize = (int) (scale + 0.5);
            options.outWidth = (int) (width / scale);
            options.outHeight = (int) (height / scale);
//            获取图片信息及宽高
            options.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            outputFile = getCacheFile(context, suffix);
            FileOutputStream fos = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.close();
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
        } else {
            File tempFile = outputFile;
            outputFile = getCacheFile(context, suffix);
            copyFileByChannel(tempFile, outputFile);
        }
        return outputFile;
    }

    private File getCacheFile(Context context, String suffix) {
        String fileName = new SimpleDateFormat("yyyyMMdd_HH-mm-ssSE").format(new Date());
        fileName = "IMG_" + fileName;
        File file = null;
        try {
            file = File.createTempFile(fileName, suffix, context.getCacheDir());
//            file = new File(context.getCacheDir(), fileName + ".jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    private void copyFileByChannel(File source, File dest) throws Exception {
        FileChannel inputChannel;
        FileChannel outputChannel;
        inputChannel = new FileInputStream(source).getChannel();
        outputChannel = new FileOutputStream(dest).getChannel();
        inputChannel.transferTo(0, inputChannel.size(), outputChannel);
    }

}
