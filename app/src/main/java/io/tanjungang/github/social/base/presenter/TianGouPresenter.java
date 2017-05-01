package io.tanjungang.github.social.base.presenter;

import android.content.Context;

import io.tanjungang.github.social.base.model.TianGouModel;
import io.tanjungang.github.social.base.view.ITianGouView;
import io.tanjungang.github.social.base.entity.TianGouResp;

/**
 * Author: TanJunDang
 * Email: TanJunDang@126.com
 * Date:2017/4/24
 */

public class TianGouPresenter implements ITianGouPresenter<TianGouResp> {

    private ITianGouView iTianGouView;

    public TianGouPresenter(Context context, ITianGouView iTianGouView) {
        this.iTianGouView = iTianGouView;
        new TianGouModel(this, context).request();
    }

    @Override
    public void modelHandleSuccess(TianGouResp resp) {
        iTianGouView.refreshUI(resp);
    }

    @Override
    public void modelHandleFailure(String errorMsg) {
        iTianGouView.showFailureUI(errorMsg);
    }

    @Override
    public void release() {
        iTianGouView = null;
    }
}
