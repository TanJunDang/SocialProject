package io.tanjungang.github.social.msg.presenter;

import android.content.Context;

import io.tanjungang.github.social.base.entity.MsgBean;
import io.tanjungang.github.social.msg.model.MsgModel;
import io.tanjungang.github.social.base.presenter.IMsgPresenter;
import io.tanjungang.github.social.base.view.IMsgView;

/**
 * Author: TanJunDang
 * Email: TanJunDang@126.com
 * Date:2017/4/24
 */

public class MsgPresenter implements IMsgPresenter<MsgBean> {

    private IMsgView iMsgView;

    public MsgPresenter(Context context, IMsgView iMsgView, String type) {
        this.iMsgView = iMsgView;
        new MsgModel(this, context).request(type);
    }

    @Override
    public void modelHandleSuccess(MsgBean resp) {
        iMsgView.refreshUI(resp);
    }

    @Override
    public void modelHandleFailure(String errorMsg) {
        iMsgView.showFailureUI(errorMsg);
    }

    @Override
    public void release() {
        iMsgView = null;
    }
}
