package io.tanjungang.github.social.base.view;

/**
 * Author: TanJunDang
 * Email: TanJunDang@126.com
 * Date:2017/4/24
 */

public interface ITianGouView<T> {
    void refreshUI(T data);

    void showFailureUI(String errorMsg);
}
