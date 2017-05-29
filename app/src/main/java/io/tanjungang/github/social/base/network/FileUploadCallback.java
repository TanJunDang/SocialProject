package io.tanjundang.github.projectutils.network;

/**
 * @Author: TanJunDang
 * @Email: TanJunDang@126.com
 * @Date: 2017/5/29
 * @Description:
 */

public interface FileUploadCallback {

    void uploadSuccess(String path, int pos);

    void uploadFailure(String error);

    void progress(int progress, int pos);
}
