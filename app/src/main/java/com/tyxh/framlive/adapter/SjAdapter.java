package com.tyxh.framlive.adapter;

import android.content.Context;
import android.text.TextUtils;
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
import com.tyxh.framlive.R;
import com.tyxh.framlive.bean.SjBean;
import com.tyxh.framlive.utils.Common.util.DateUtils;

import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SjAdapter extends RecyclerView.Adapter<SjAdapter.ViewHolder> {
    private Context mContext;
    private List<SjBean.RetDataBean.ListBean> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public SjAdapter(Context context, List<SjBean.RetDataBean.ListBean> stringList) {
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
        SjBean.RetDataBean.ListBean bean = mLists.get(position);
        int type = bean.getType();//连线类型[1:视频；2:语音]
        vh.mItemSjTitle.setText(bean.getTitle());
        long l = DateUtils.calculateDifference( DateUtils.parseDate(bean.getStartDate()), TextUtils.isEmpty(bean.getEndDate())?new Date():DateUtils.parseDate(bean.getEndDate()), DateUtils.Minute);
        vh.mItemSjZsnum.setText("疏解时长：" +l+"分钟");
        vh.mItemSjCreattime.setText("创建时间：" +bean.getCreateDate());
        vh.mItemSjCode.setText("订单编号：" + bean.getUuId());
        RoundedCorners roundedCorners = new RoundedCorners(16);
        Glide.with(mContext).load(R.drawable.live_defaultimg).apply(new RequestOptions().transform(new CenterCrop(),roundedCorners)).error(R.drawable.live_defaultimg)
                .placeholder(R.drawable.live_defaultimg).into(vh.mItemSjHead);
        int star = bean.getStar();
        if(star<=0){
            vh.mItemSjState.setText("待评价");
            vh.mItemSjState.setTextColor(mContext.getResources().getColor(R.color.order_ye));
            vh.mItemSjState.setBackgroundResource(R.drawable.bg_solder_ye);
            vh.mItemSjPj.setVisibility(View.VISIBLE);
            vh.mItemSjStart.setVisibility(View.GONE);
        }else{
            vh.mItemSjState.setText("已评价");
            vh.mItemSjState.setTextColor(mContext.getResources().getColor(R.color.nineninenine));
            vh.mItemSjState.setBackgroundResource(R.drawable.bg_solder_nin);
            vh.mItemSjStart.setVisibility(View.VISIBLE);
            vh.mItemSjStart.setNumStars(star);
            vh.mItemSjPj.setVisibility(View.GONE);
        }
        if(bean.getProHistory()!=null) {
            vh.mItemSjZuan.setText("-" + bean.getProHistory().getBusinessDiaNum());
            vh.mItemSjZuan.setVisibility(View.VISIBLE);
        }else{
            vh.mItemSjZuan.setVisibility(View.GONE);
        }

        if (position % 2 == 0) {
            vh.mItemSjBtll.setVisibility(View.VISIBLE);
            vh.mItemSjLeftm.setText("100分钟");

        } else {
            vh.mItemSjBtll.setVisibility(View.INVISIBLE);
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
