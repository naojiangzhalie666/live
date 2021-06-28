package com.tyxh.framlive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tyxh.framlive.R;
import com.tyxh.framlive.bean.NoticeBean;
import com.superc.yyfflibrary.utils.DateUtil;
import com.tencent.qcloud.tim.uikit.utils.DateTimeUtil;

import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {
    private Context mContext;
    private List<NoticeBean.RetDataBean.ListBean> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public NoticeAdapter(Context context, List<NoticeBean.RetDataBean.ListBean> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_notice, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        NoticeBean.RetDataBean.ListBean bean = mLists.get(position);
        vh.mItemNoticeTitle.setText("通知");
        vh.mItemNoticeContent.setText(bean.getNoticeContent());
        vh.mItemNoticeTm.setText(DateTimeUtil.getTimeFormatText(new Date(Long.parseLong(DateUtil.getTimeLong( bean.getCreateDate(),"yyyy-MM-dd HH:mm:ss")) * 1000)));
        Glide.with(mContext).load(bean.getIco()).placeholder(R.drawable.live_defaultimg).error(R.drawable.live_defaultimg).into(vh.mItemNoticeImg);

    }

    @Override
    public int getItemCount() {
        return mLists == null ? 0 : mLists.size();
    }

    public interface OnItemClickListener {
        void onItemClickListener(int pos);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_notice_title)
        TextView mItemNoticeTitle;
        @BindView(R.id.item_notice_content)
        TextView mItemNoticeContent;
        @BindView(R.id.item_notice_tm)
        TextView mItemNoticeTm;    @BindView(R.id.imageView11)
        ImageView mItemNoticeImg;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
