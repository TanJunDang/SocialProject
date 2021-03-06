package io.tanjungang.github.social.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;

/**
 * Author: TanJunDang
 * Email: TanJunDang@126.com
 * Date: 2016/4/3
 */
public abstract class BaseFragment extends Fragment {

    protected final String TAG = getClass().getName();
    protected final String AUTHOR = "TanJunDang";

    protected abstract int getLayoutId();

    protected abstract void initView(View view);

    protected abstract void intData();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        initView(view);
        intData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getSimpleName());
    }
}
