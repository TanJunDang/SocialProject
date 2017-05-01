package io.tanjungang.github.social.base.model;

import android.content.Context;

import io.tanjundang.github.projectutils.network.HttpCallback;
import io.tanjungang.github.social.base.presenter.TianGouPresenter;
import io.tanjungang.github.social.base.entity.TianGouResp;

/**
 * Author: TanJunDang
 * Email: TanJunDang@126.com
 * Date:2017/4/24
 */

public class TianGouModel extends HttpCallback<TianGouResp> implements ITianGouModel {

    private TianGouPresenter presenter;
    private Context context;

    public TianGouModel(TianGouPresenter presenter, Context context) {
        this.presenter = presenter;
        this.context = context;
    }

    @Override
    public void request() {

    }

    @Override
    public void onSuccess(TianGouResp resp) {
        presenter.modelHandleSuccess(resp);
    }

    @Override
    public void onFailure(String error) {
        presenter.modelHandleFailure(error);
    }
}
