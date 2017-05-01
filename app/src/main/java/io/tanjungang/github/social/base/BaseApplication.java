package io.tanjungang.github.social.base;

import android.app.Application;

import com.facebook.stetho.Stetho;

import io.tanjundang.github.projectutils.utils.Functions;
import io.tanjundang.github.projectutils.utils.ImageLoaderTool;

/**
 * Author: TanJunDang on 2015/10/28.
 * 初始化全局配置
 * 导入so文件，在src/main 下面新建一个jniLibs文件夹 放入文件即可
 */
public class BaseApplication extends Application {
    public static String AUTHOR = "TanJunDang";

    @Override
    public void onCreate() {
        super.onCreate();
        Functions.init(getApplicationContext());
        ImageLoaderTool.initImageLoader(getApplicationContext());
        Stetho.initializeWithDefaults(this);
    }
}
