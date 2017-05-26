package io.tanjungang.github.social.msg;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import io.tanjundang.github.projectutils.base.Constant;
import io.tanjundang.github.projectutils.utils.LogTool;
import io.tanjungang.github.social.R;
import io.tanjungang.github.social.base.BaseFragment;
import io.tanjungang.github.social.base.entity.MsgBean;
import io.tanjungang.github.social.base.entity.MsgTabInfo;
import io.tanjungang.github.social.base.presenter.MsgPresenter;
import io.tanjungang.github.social.base.view.IMsgView;
import io.tanjungang.github.social.base.widget.ItemDivider;

public class CommonFragment extends Fragment implements IMsgView<MsgBean> {

    private TextView tvTest;
    private RecyclerView recyclerview;
    private String type;
    private MsgPresenter presenter;
    private boolean isShowPage = true;

    private NewsAdapter mAdapter;
    private ArrayList<MsgBean.MsgRespon.MsgInfo> list = new ArrayList<>();

    public static CommonFragment newInstance(String type) {
        CommonFragment fragment = new CommonFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
//        if (isVisibleToUser) {
//            intData();
//        } else {
//            isShowPage = false;
//        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        type = getArguments().getString(Constant.TYPE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common, container, false);
        tvTest = (TextView) view.findViewById(R.id.tvTest);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.addItemDecoration(new ItemDivider(Color.TRANSPARENT, ItemDivider.VERTICAL));

        mAdapter = new NewsAdapter();
        recyclerview.setAdapter(mAdapter);
//        if (isShowPage) {
//        }
        intData();
        return view;
    }

    protected void intData() {
        if (tvTest == null) {
            isShowPage = true;
        } else {
            tvTest.setText(type);
            presenter = new MsgPresenter(getContext(), this, type);
            isShowPage = false;
        }
    }

    @Override
    public void refreshUI(MsgBean data) {
        list.addAll(data.getResult().getData());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showFailureUI(String errorMsg) {

    }

    int lastPosition = -1;

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R
                    .anim.item_bottom_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onDestroy() {
        if (presenter != null)
            presenter.release();
        super.onDestroy();
    }

    class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {

        int dataTag = R.layout.item_news;

        @Override
        public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_news, parent, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String website = (String) v.getTag(dataTag);
                    Uri uri = Uri.parse(website);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
            return new NewsHolder(view);
        }

        @Override
        public void onBindViewHolder(NewsHolder holder, int position) {
            MsgBean.MsgRespon.MsgInfo info = list.get(position);
            Glide.with(getContext()).load(info.getThumbnail_pic_s()).into(holder.ivIcon);
            holder.tvTitle.setText(info.getTitle());
            holder.tvDate.setText(info.getDate());
            holder.tvSource.setText(info.getAuthor_name());
            setAnimation(holder.rootview, position);
            holder.rootview.setTag(dataTag, info.getUrl());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class NewsHolder extends RecyclerView.ViewHolder {

            private View rootview;
            private ImageView ivIcon;
            private TextView tvTitle;
            private TextView tvDate;
            private TextView tvSource;

            public NewsHolder(View itemView) {
                super(itemView);
                rootview = itemView;
                ivIcon = (ImageView) rootview.findViewById(R.id.ivIcon);
                tvTitle = (TextView) rootview.findViewById(R.id.tvTitle);
                tvDate = (TextView) rootview.findViewById(R.id.tvDate);
                tvSource = (TextView) rootview.findViewById(R.id.tvSource);
            }
        }
    }
}
