package io.tanjungang.github.social.base.api;


import io.tanjungang.github.social.base.entity.MsgBean;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Author: TanJunDang
 * Email: TanJunDang@126.com
 * Date:2017/4/21
 */

public interface BusinessApi {
    @GET("toutiao/index?key=d7ae21a89286b57b7811a7229c38f46f")
    Call<MsgBean> getMsgData(@Query("type") String type);
}
