package io.tanjungang.github.social.base.presenter;

/**
 * Author: TanJunDang
 * Email: TanJunDang@126.com
 * Date:2017/4/24
 */

public interface ITianGouPresenter<T> {
    // Model请求成功的回调，参数同View接口
    void modelHandleSuccess(T resp);

    // model请求失败的回调
    void modelHandleFailure(String errorMsg);

    void release();

}
