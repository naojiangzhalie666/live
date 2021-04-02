package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NoviceAdapter extends RecyclerView.Adapter<NoviceAdapter.ViewHolder> {
    private Context mContext;
    private List<String> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private int type = 0;

    public NoviceAdapter(Context context, List<String> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_novice, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        String bean = mLists.get(position);
        vh.mItemNoviceTitle.setText("任务" + (position + 1));
        vh.mItemNoviceContent.setText("+就拿手机来的感觉");
        vh.mItemNoviceImgv.setVisibility(View.GONE);
        vh.mItemNoviceMuge.setVisibility(View.VISIBLE);
        vh.mItemNoviceState.setBackgroundResource(R.drawable.bg_circle_stoke_red);
        vh.mItemNoviceState.setText("前往");
        vh.mItemNoviceState.setTextColor(mContext.getResources().getColor(R.color.red));
        switch (type) {
            case 0://新手
                vh.mItemNoviceImgv.setVisibility(View.VISIBLE);
                RequestOptions requestOptions = new RequestOptions().circleCrop();
                Glide.with(mContext).load(R.drawable.login_wchat).apply(requestOptions).into(vh.mItemNoviceImgv);
                break;
            case 1://每日任务
                break;
            case 2://每日收益
                break;
            case 3://每月收益
                vh.mItemNoviceMuge.setVisibility(View.GONE);
                break;
        }
        switch (position) {
            case 1:
                vh.mItemNoviceState.setBackgroundResource(R.drawable.bg_circle_red);
                vh.mItemNoviceState.setText("领取");
                vh.mItemNoviceState.setTextColor(mContext.getResources().getColor(R.color.white));
                break;
            case 4:
                vh.mItemNoviceState.setBackgroundResource(R.drawable.bg_circle_stoke_red);
                vh.mItemNoviceState.setText("前往");
                vh.mItemNoviceState.setTextColor(mContext.getResources().getColor(R.color.red));
                break;
            case 9:
                vh.mItemNoviceState.setBackgroundResource(R.drawable.bg_circle_stoke_gray);
                vh.mItemNoviceState.setText("已完成");
                vh.mItemNoviceState.setTextColor(mContext.getResources().getColor(R.color.nineninenine));
                break;
        }



    }

    @Override
    public int getItemCount() {
        return mLists == null ? 0 : mLists.size();
    }

    public interface OnItemClickListener {
        void onItemClickListener(int pos);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_novice_imgv)
        ImageView mItemNoviceImgv;
        @BindView(R.id.item_novice_title)
        TextView mItemNoviceTitle;
        @BindView(R.id.item_novice_content)
        TextView mItemNoviceContent;
        @BindView(R.id.item_novice_muge)
        ImageView mItemNoviceMuge;
        @BindView(R.id.item_novice_titleright)
        TextView mItemNoviceTitleright;
        @BindView(R.id.item_novice_state)
        TextView mItemNoviceState;
        @BindView(R.id.textView12)
        View mTextView12;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
