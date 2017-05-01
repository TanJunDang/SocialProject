package io.tanjungang.github.social;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import io.tanjungang.github.social.base.BaseActivity;
import io.tanjungang.github.social.base.BaseFragment;
import io.tanjungang.github.social.msg.MsgFragment;
import io.tanjungang.github.social.setting.SettingFragment;
import io.tanjungang.github.social.share.ShareFragment;
import io.tanjungang.github.social.video.VideoFragment;

/**
 * Fragment相关问题
 * https://yrom.net/blog/2013/03/10/fragment-switch-not-restart/
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private BaseFragment mContent;

    private String[] fragments = new String[]{"MsgFragment", "VideoFragment", "SettingFragment", "ShareFragment"};
    private FragmentManager fm = getSupportFragmentManager();
    private FragmentTransaction fts = fm.beginTransaction();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void intData() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stateCheck(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public synchronized void switchContent(BaseFragment from, BaseFragment to, int position) {
        if (mContent != to) {
            mContent = to;
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            if (!to.isAdded()) { // 先判断是否被add过
                transaction.hide(from).add(R.id.flContent, to, fragments[position]).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    private void selected(final int position) {
        BaseFragment to = (BaseFragment) fm.findFragmentByTag(fragments[position]);
        if (to == null) {
            to = initFragment(position);
        }
        switchContent(mContent, to, position);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_msg) {
            selected(0);
        } else if (id == R.id.item_video) {
            selected(1);
        } else if (id == R.id.item_setting) {
            selected(2);
        } else if (id == R.id.item_share) {
            selected(3);
        } else if (id == R.id.item_send) {
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 状态检测 用于内存不足的时候保证fragment不会重叠
     *
     * @param savedInstanceState
     */
    private void stateCheck(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            MsgFragment msgFragment = (MsgFragment) initFragment(0);
            mContent = msgFragment;
            fts.add(R.id.flContent, msgFragment, fragments[0]);
            fts.commit();
        } else {
            if (fm.findFragmentByTag(fragments[0]) != null) {
                fts.show(fm.findFragmentByTag(fragments[0]));
                mContent = (BaseFragment) fm.findFragmentByTag(fragments[0]);
            }
            if (fm.findFragmentByTag(fragments[1]) != null) {
                fts.hide(fm.findFragmentByTag(fragments[1]));
            }
            if (fm.findFragmentByTag(fragments[2]) != null) {
                fts.hide(fm.findFragmentByTag(fragments[2]));
            }
            if (fm.findFragmentByTag(fragments[3]) != null) {
                fts.hide(fm.findFragmentByTag(fragments[3]));
            }
            fts.commit();
        }
    }

    private BaseFragment initFragment(int position) {
        BaseFragment fragment = null;
        switch (position) {
            case 0:
                fragment = new MsgFragment();
                break;
            case 1:
                fragment = new VideoFragment();
                break;
            case 2:
                fragment = new SettingFragment();
                break;
            case 3:
                fragment = new ShareFragment();
                break;
        }
        return fragment;
    }
}
