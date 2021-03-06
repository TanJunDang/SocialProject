package io.tanjungang.github.social.msg.model;

import android.content.Context;

import io.tanjundang.github.projectutils.network.HttpCallback;
import io.tanjundang.github.projectutils.network.HttpReqTool;
import io.tanjungang.github.social.base.api.BusinessApi;
import io.tanjungang.github.social.base.entity.MsgBean;
import io.tanjungang.github.social.base.model.IBaseModel;
import io.tanjungang.github.social.msg.presenter.MsgPresenter;

/**
 * Author: TanJunDang
 * Email: TanJunDang@126.com
 * Date:2017/4/24
 */

public class MsgModel extends HttpCallback<MsgBean> implements IBaseModel {

    private MsgPresenter presenter;
    private Context context;

    public MsgModel(MsgPresenter presenter, Context context) {
        this.presenter = presenter;
        this.context = context;
    }

    @Override
    public void request(String type) {
        HttpReqTool.getInstance().setApi(BusinessApi.class).getMsgData(type).enqueue(this);
    }

    @Override
    public void onSuccess(MsgBean resp) {
        presenter.modelHandleSuccess(resp);
    }

    @Override
    public void onFailure(String error) {
        presenter.modelHandleFailure(error);
    }
}
