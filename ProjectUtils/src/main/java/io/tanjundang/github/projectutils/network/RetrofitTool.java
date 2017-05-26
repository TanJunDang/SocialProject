package io.tanjundang.github.projectutils.network;

import android.util.Log;

import java.io.IOException;

import io.tanjundang.github.projectutils.utils.LogTool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author: TanJunDang
 * Email: TanJunDang@126.com
 * Date:2017/4/21
 */

public class RetrofitTool {

    private Retrofit retrofit;
    private static String baseUrl = "http://v.juhe.cn";

    private static class Holder {
        private static RetrofitTool INSTANCE = new RetrofitTool();
    }

    public static RetrofitTool getInstance() {
        return Holder.INSTANCE;
    }

    RetrofitTool() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(getOKHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(baseUrl) //02采用链式结构绑定Base url
                    .build();
        }
    }

    private OkHttpClient getOKHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                //打印retrofit日志
                LogTool.i("RetrofitLog", "retrofitBack = " + message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException, java.io.IOException {
                        Request request = chain
                                .request()
                                .newBuilder()
//                        .addHeader("Source", "android")
//                        .addHeader("Accept", "application/json,text/javascript,*/*")
                                .build();
                        return chain.proceed(request);
                    }
                });
        return builder.build();
    }

    public <T> T createApi(Class<T> clazz) {
        return retrofit.create(clazz);
    }
}
