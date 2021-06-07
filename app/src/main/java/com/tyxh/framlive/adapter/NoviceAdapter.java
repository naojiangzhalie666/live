package com.tyxh.framlive.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tyxh.framlive.R;
import com.tyxh.framlive.bean.TaskBean;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NoviceAdapter extends RecyclerView.Adapter<NoviceAdapter.ViewHolder> {
    private Context mContext;
    private List<TaskBean.RetDataBean> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private int type = 0;

    public NoviceAdapter(Context context, List<TaskBean.RetDataBean> stringList) {
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
        TaskBean.RetDataBean bean = mLists.get(position);
        vh.mItemNoviceTitle.setText(bean.getTaskName());
        StringBuilder strin_content =new StringBuilder();
        if(bean.getAwardEXP()>0){//经验值
            strin_content.append("+奖励"+bean.getAwardJewel()+"经验值");
        }
        if(bean.getAwardJewel()>0){//钻石
            strin_content.append("+"+bean.getAwardJewel()+"钻石");
        }
        if(bean.getAwardPropNum()>0){//道具
            strin_content.append("+"+bean.getPropName()+"*"+bean.getAwardPropNum());
        }
        if(bean.getRewardGiftNum()>0){//礼物
            strin_content.append("+"+bean.getGiftName()+"*"+bean.getRewardGiftNum());
        }
        if(!TextUtils.isEmpty(bean.getTaskAwardMoney())){//金额
            strin_content.append("+"+bean.getTaskAwardMoney());
        }
        vh.mItemNoviceContent.setText(strin_content.toString());
        /*vh.mItemNoviceImgv.setVisibility(View.GONE);
        vh.mItemNoviceMuge.setVisibility(View.GONE);
        vh.mItemNoviceState.setBackgroundResource(R.drawable.bg_circle_stoke_red);
        vh.mItemNoviceState.setText("前往");
        vh.mItemNoviceState.setTextColor(mContext.getResources().getColor(R.color.red));
        switch (type) {
            case 0://新手
                vh.mItemNoviceImgv.setVisibility(View.VISIBLE);
                RequestOptions requestOptions = new RequestOptions().circleCrop();
                Glide.with(mContext).load(R.drawable.pj_bg).apply(requestOptions).into(vh.mItemNoviceImgv);
                break;
            case 1://每日任务
                break;
            case 2://每日收益
                break;
            case 3://每月收益
                vh.mItemNoviceMuge.setVisibility(View.GONE);
                break;
        }*/
        int state = bean.getState();
        switch (state) {
            case 1://未完成
                vh.mItemNoviceState.setBackgroundResource(R.drawable.bg_circle_stoke_red_task);
                vh.mItemNoviceState.setText("前往");
                vh.mItemNoviceState.setTextColor(mContext.getResources().getColor(R.color.task_color));
                break;
            case 2://待领取
                vh.mItemNoviceState.setBackgroundResource(R.drawable.bg_circle_red_task);
                vh.mItemNoviceState.setText("领取");
                vh.mItemNoviceState.setTextColor(mContext.getResources().getColor(R.color.white));
                break;
            case 3://已完成
                vh.mItemNoviceState.setBackgroundResource(R.drawable.bg_circle_stoke_gray);
                vh.mItemNoviceState.setText("已完成");
                vh.mItemNoviceState.setTextColor(mContext.getResources().getColor(R.color.nineninenine));
                break;
        }
        vh.mItemNoviceState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener!=null)
                    mOnItemClickListener.onItemClickListener(position);
            }
        });



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
