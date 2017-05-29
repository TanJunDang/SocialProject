package io.tanjungang.github.social.base.network;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;
import io.tanjundang.github.ImageInfo;
import io.tanjundang.github.projectutils.network.FileUploadCallback;
import io.tanjundang.github.projectutils.utils.Functions;
import io.tanjundang.github.projectutils.utils.LogTool;
import io.tanjundang.github.utils.PhotoCompress;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @Author: TanJunDang
 * @Email: TanJunDang@126.com
 * @Date: 2017/5/29
 * @Description: 使用bmob上的一些服务
 */

public class BmobApi {

    public static void upload(final Context context, ArrayList<ImageInfo> data, final FileUploadCallback callback) {
        final PhotoCompress compress = new PhotoCompress(context);
        if (data.isEmpty()) {
            return;
        }
        final int[] pos = {0};
        Observable.from(data)
                .map(new Func1<ImageInfo, String>() {
                    @Override
                    public String call(ImageInfo imageInfo) {
                        String path = imageInfo.path.replace("file://", "");
                        return path;
                    }
                })
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String path) {
                        String zipPath = null;
                        try {
                            zipPath = compress.compress(path).getPath();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return zipPath;
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onNext(String path) {
                        try {
                            upload(path, callback, pos[0]);
                            pos[0]++;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private static void upload(String path, final FileUploadCallback callback, final int pos) {
        final BmobFile file = new BmobFile(new File(path));
        file.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    callback.uploadSuccess(file.getFileUrl(), pos);
                } else {
                    callback.uploadFailure(e.getMessage());
                }
            }

            @Override
            public void onProgress(Integer value) {
                super.onProgress(value);
                callback.progress(value, pos);
            }

            @Override
            public void doneError(int code, String msg) {
                super.doneError(code, msg);
                callback.uploadFailure(msg);
            }
        });
    }
}
