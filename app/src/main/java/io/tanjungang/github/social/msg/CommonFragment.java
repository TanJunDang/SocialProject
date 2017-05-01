package io.tanjungang.github.social.msg;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.tanjundang.github.projectutils.base.Constant;
import io.tanjungang.github.social.R;
import io.tanjungang.github.social.base.BaseFragment;

public class CommonFragment extends BaseFragment {

    private TextView tvTest;
    private String type;

    public static CommonFragment newInstance(String type) {
        CommonFragment fragment = new CommonFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        type = getArguments().getString(Constant.TYPE);
        return R.layout.fragment_common;
    }

    @Override
    protected void initView(View view) {
        tvTest = (TextView) view.findViewById(R.id.tvTest);
    }

    @Override
    protected void intData() {
        tvTest.setText(type);
    }

}
