package io.tanjundang.github.projectutils.network;

/**
 * Author: TanJunDang
 * Email: TanJunDang@126.com
 * Date:2017/4/22
 * 请求工具
 */

public class HttpReqTool {

    private static class Holder {
        private static HttpReqTool INSTANCE = new HttpReqTool();
    }

    public static HttpReqTool getInstance() {
        return Holder.INSTANCE;
    }

    public <T> T setApi(Class<T> cls) {
        T api = RetrofitTool.getInstance().createApi(cls);
        return api;
    }

    /**
     * @param url      待下载文件的URL
     * @param savePath 保存位置
     * @param name     保存的文件名
     * @param callback 下载回调接口
     */
    public void fileDownload(String url, String savePath, String name, FileDownloadCallback callback) {
    }

}
