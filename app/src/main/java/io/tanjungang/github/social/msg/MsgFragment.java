package io.tanjungang.github.social.msg;


import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TableLayout;

import java.util.ArrayList;

import io.tanjungang.github.social.R;
import io.tanjungang.github.social.base.BaseFragment;
import io.tanjungang.github.social.base.entity.MsgTabInfo;


public class MsgFragment extends BaseFragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private MsgTabAdapter mAdapter;

    private ArrayList<MsgTabInfo> titles = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_msg;
    }

    @Override
    protected void initView(View view) {
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
    }

    @Override
    protected void intData() {
        titles.add(new MsgTabInfo("头条", "toutiao", tabLayout.newTab()));
        titles.add(new MsgTabInfo("社会", "shehui", tabLayout.newTab()));
        titles.add(new MsgTabInfo("国内", "guonei", tabLayout.newTab()));
        titles.add(new MsgTabInfo("国际", "guoji", tabLayout.newTab()));
        titles.add(new MsgTabInfo("娱乐", "yule", tabLayout.newTab()));
        titles.add(new MsgTabInfo("体育", "tiyu", tabLayout.newTab()));
        titles.add(new MsgTabInfo("军事", "junshi", tabLayout.newTab()));
        titles.add(new MsgTabInfo("时尚", "shishang", tabLayout.newTab()));
        titles.add(new MsgTabInfo("财经", "caijing", tabLayout.newTab()));

        for (MsgTabInfo info : titles) {
            tabLayout.addTab(info.getTab());
        }
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mAdapter = new MsgTabAdapter(getFragmentManager());
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    class MsgTabAdapter extends FragmentPagerAdapter {

        public MsgTabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return CommonFragment.newInstance(titles.get(position).getId());
        }

        @Override
        public int getCount() {
            return titles.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position).getTitle();
        }
    }
}
