package io.tanjungang.github.social.base;

import io.tanjundang.github.projectutils.BuildConfig;


/**
 * Author: TanJunDang
 * Date: 2015/10/29.
 * Email:TanJunDang@126.com
 * 全局变量
 * 使数据在不同的地方调用
 */
public class Global {
    public static boolean DEBUG = BuildConfig.LOG_DEBUG;

    public static Global getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        static Global INSTANCE = new Global();
    }

}
