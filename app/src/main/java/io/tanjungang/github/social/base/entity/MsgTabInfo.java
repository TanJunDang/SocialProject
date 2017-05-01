package io.tanjungang.github.social.base.entity;

import android.support.design.widget.TabLayout;

/**
 * Author: TanJunDang
 * Email: TanJunDang@126.com
 * Date:2017/5/1
 */

public class MsgTabInfo {
    private String title;
    private String id;
    private TabLayout.Tab tab;


    public MsgTabInfo(String title, String id, TabLayout.Tab tab) {
        this.title = title;
        this.id = id;
        this.tab = tab;
        initTab();
    }

    public TabLayout.Tab getTab() {
        return tab;
    }

    public void setTab(TabLayout.Tab tab) {
        this.tab = tab;
    }

    private void initTab() {
        tab.setText(title);
        tab.setTag(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
