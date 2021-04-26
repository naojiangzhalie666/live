package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;

import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SjAdapter extends RecyclerView.Adapter<SjAdapter.ViewHolder> {
    private Context mContext;
    private List<Map<String, Object>> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public SjAdapter(Context context, List<Map<String, Object>> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_sj, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        Map<String, Object> bean = mLists.get(position);
        vh.mItemSjTitle.setText("是谁给当时的");
        RoundedCorners roundedCorners = new RoundedCorners(16);
        Glide.with(mContext).load(R.drawable.mine_bg).apply(new RequestOptions().transform(new CenterCrop(),roundedCorners)).into(vh.mItemSjHead);

        vh.mItemSjZsnum.setText("疏解时长：" + "40+" + position);
        vh.mItemSjCreattime.setText("创建时间：" + "2010.02.02 12:22");
        vh.mItemSjCode.setText("订单编号：" + "124煞了苦痛23" + position);
        vh.mItemSjZuan.setText("-1" + position);
        if (position % 2 == 0) {
            vh.mItemSjState.setText("已完成");
            vh.mItemSjState.setTextColor(mContext.getResources().getColor(R.color.nineninenine));
            vh.mItemSjState.setBackgroundResource(R.drawable.bg_solder_nin);
            vh.mItemSjBtll.setVisibility(View.VISIBLE);
            vh.mItemSjLeftm.setText("100分钟");
            vh.mItemSjPj.setVisibility(View.VISIBLE);
            vh.mItemSjStart.setVisibility(View.GONE);
        } else {
            vh.mItemSjBtll.setVisibility(View.INVISIBLE);
            vh.mItemSjState.setText("待支付");
            vh.mItemSjState.setTextColor(mContext.getResources().getColor(R.color.order_ef));
            vh.mItemSjState.setBackgroundResource(R.drawable.bg_solder_ef);
            vh.mItemSjPj.setVisibility(View.GONE);
            vh.mItemSjStart.setVisibility(View.VISIBLE);
            vh.mItemSjStart.setNumStars(position%3);
        }
        vh.mItemSjTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClickListener(position);
            }
        });
        vh.mItemSjPj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemPjClickListener(position);
            }
        });
        vh.mItemSjTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemSxClickListener(position);
            }
        });
        vh.mItemSjAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemAgainClickListener(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mLists == null ? 0 : mLists.size();
    }

    public interface OnItemClickListener {
        void onItemClickListener(int pos);

        void onItemPjClickListener(int pos);

        void onItemSxClickListener(int pos);

        void onItemAgainClickListener(int pos);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_sj_code)
        TextView mItemSjCode;
        @BindView(R.id.item_sj_title)
        TextView mItemSjTitle;
        @BindView(R.id.item_sj_state)
        TextView mItemSjState;
        @BindView(R.id.item_sj_head)
        ImageView mItemSjHead;
        @BindView(R.id.item_sj_zsnum)
        TextView mItemSjZsnum;
        @BindView(R.id.item_sj_creattime)
        TextView mItemSjCreattime;
        @BindView(R.id.item_sj_zuan)
        TextView mItemSjZuan;
        @BindView(R.id.item_sj_again)
        TextView mItemSjAgain;
        @BindView(R.id.item_sj_talk)
        TextView mItemSjTalk;
        @BindView(R.id.item_sj_pj)
        TextView mItemSjPj;
        @BindView(R.id.item_sj_start)
        RatingBar mItemSjStart;
        @BindView(R.id.item_sj_leftm)
        TextView mItemSjLeftm;
        @BindView(R.id.item_sj_btll)
        LinearLayout mItemSjBtll;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
