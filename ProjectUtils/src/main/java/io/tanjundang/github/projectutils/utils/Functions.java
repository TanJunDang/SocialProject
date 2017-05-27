package io.tanjundang.github.projectutils.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import io.tanjundang.github.projectutils.R;


/**
 * Author: TanJunDang on 2015/10/13.
 * Email: TanJunDang324@126.com
 */
public class Functions {

    public static int MALE = 0;//男
    public static int FEMALE = 1;//女
    public static int REQ_PICTURE = 0XFF;
    private static Context appContext;

    public static void init(Context context) {
        appContext = context;
    }

    public static void toast(CharSequence text) {
        if (appContext == null) {
            throw new RuntimeException("Function need to be init");
        }
        Toast.makeText(appContext, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 根据值来显示男女
     */

    public static String getStringByGender(int gender) {
        Resources resources = appContext.getResources();
        String sex = "";
        if (gender == MALE) {
            sex = resources.getString(R.string.common_sex_male);
        } else {
            sex = resources.getString(R.string.common_sex_female);
        }
        return sex;
    }

    /**
     * 隐藏软键盘
     *
     * @param
     * @param view
     */
    public static void hideInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) appContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 显示软键盘
     *
     * @param context
     * @param view
     */
    public static void showInputMethodForQuery(Context context, View view) {

        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, 0);
        }
    }


    /**
     * 根据名字获取对应的图片资源id
     * 不需要R.drawable前缀
     *
     * @param s
     * @return
     */
    public static int getResourceIdByString(String s) {
        try {
            Field field = R.drawable.class.getField(s);
            return Integer.parseInt(field.get(null).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 此方法只是获取手机的当前时间
     * 必须要加上服务器下发的时间跟系统的时间差(offset = serverTime - systemTime)。
     *
     * @return
     */
    public static long getCurrentFixTime() {
        int offset = 0;
        return Calendar.getInstance().getTimeInMillis() + offset;
    }

    /**
     * 获取屏幕的高度
     *
     * @return
     */
    public static int getScreenHeight() {
        WindowManager manager = (WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth() {
        WindowManager manager = (WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    /**
     * dp 转 px
     *
     * @param dpVal
     * @return
     */
    public static int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, appContext.getResources().getDisplayMetrics());
    }


    /**
     * px 转  dp
     *
     * @param pxVal
     * @return
     */
    public static float px2dp(float pxVal) {
        final float scale = appContext.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * 将一个String集合进行逗号拼装
     *
     * @param list
     * @return
     */
    public static String getStringListByPoint(ArrayList<String> list) {
        String result = "";
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            String text = (String) iterator.next();
            result += text + ",";
        }
        result = result.substring(0, result.length() - 1);
        return result;
    }

    /**
     * 获取图片的File路径
     *
     * @param uri
     * @return
     */
    @SuppressLint("NewApi")
    private static String getPath(final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(appContext, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(appContext, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(appContext, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(appContext, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * 需要在onactivityresult里用REQ_PICTURE接收本地图片数据
     *
     * @param context
     */
    public static void SkipToImagePickActivity(Context context) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        ((Activity) context).startActivityForResult(Intent.createChooser(intent, "Choose Picture"), REQ_PICTURE);
    }

//    /**
//     * 从Intent获取本地path
//     * 用法：用在onActivityResult里
//     *
//     * @param data
//     * @return
//     */
//    public static void getImageFromIntent(Intent data, PictureCallBack callback) {
//        String path = "";
//        Uri uri = data.getData();
//        path = Functions.getPath(uri);
////        upload(path, callback);
//    }


    /**
     * 获取sd卡路径,如果没有sd卡路径,就在/data/data下创建
     *
     * @return
     */
    public static String getSDPath() {
        String sdPath = "";

        if (isSDCardExist()) {
            sdPath = Environment.getExternalStorageDirectory() + "/TanJunDang_Movie/";
        } else {
            sdPath = Environment.getDataDirectory() + "/TanJunDang_Movie/";
        }

        File file = new File(sdPath); //创建路径
        if (!file.exists()) {
            file.mkdirs();
        }

        return sdPath;
    }

    /**
     * 判断SD卡是否存在
     *
     * @return
     */
    public static boolean isSDCardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 查看APK版本 展示所用 1.2.0
     */
    public static String getAppVersionName() {
        String versionName = "";
        PackageManager manager = appContext.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(appContext.getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

//    public static String getChannel() {
//        String chanel = "Offcial";
//        String gitVersion = "";
//        try {
//            ApplicationInfo info = appContext.getPackageManager().getApplicationInfo(appContext.getPackageName(), PackageManager.GET_META_DATA);
//            gitVersion = info.metaData.getString("GIT");
////            chanel = info.metaData.getString("UMENG_CHANNEL");
////            chanel = String.format("%s-%s-%s", chanel, gitVersion, BuildConfig.BUILD_TYPE);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        chanel = String.format("%s-%s-%s", BuildConfig.FLAVOR, gitVersion, BuildConfig.BUILD_TYPE);
//        return chanel;
//    }

    /**
     * 获取版本ID 用于与服务端版本号对比，从而决定是否需要升级
     * <p/>
     * 可使用BuildType直接获取版本号、版本、渠道号、BuildType等数据
     *
     * @param
     * @return
     */
    public static int getAppVersionCode() {
        int versionCode = 0;
        PackageManager manager = appContext.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(appContext.getPackageName(), 0);
            versionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public static void SkipToAppDetail() {
        //跳转到当前应用详情页面
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.fromParts("package", appContext.getPackageName(), null));
        appContext.startActivity(intent);
    }


    // 通过文件头来判断是否gif
    public static boolean isGifByFile(File file) {
        try {
            int length = 10;
            InputStream is = new FileInputStream(file);
            byte[] data = new byte[length];
            is.read(data);
            String type = getType(data);
            is.close();

            if (type.equals("gif")) {
                return true;
            }
        } catch (Exception e) {
            LogTool.v("GIF", e.getMessage());
        }

        return false;
    }

    private static String getType(byte[] data) {
        String type = "";
        if (data[1] == 'P' && data[2] == 'N' && data[3] == 'G') {
            type = "png";
        } else if (data[0] == 'G' && data[1] == 'I' && data[2] == 'F') {
            type = "gif";
        } else if (data[6] == 'J' && data[7] == 'F' && data[8] == 'I'
                && data[9] == 'F') {
            type = "jpg";
        }
        return type;
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        int statusBarHeight1 = -1;
        int resourceId = appContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = appContext.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight1;
    }

    /**
     * 获取actionbar高度
     *
     * @return
     */
    public int getActionBarHeight() {
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (appContext.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, appContext.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }
}
