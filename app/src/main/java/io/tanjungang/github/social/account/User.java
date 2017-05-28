package io.tanjungang.github.social.account;

import cn.bmob.v3.BmobUser;

/**
 * @Author: TanJunDang
 * @Email: TanJunDang@126.com
 * @Date: 2017/5/28
 * @Description:
 */

public class User extends BmobUser {
    private String picurl;

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }
}
