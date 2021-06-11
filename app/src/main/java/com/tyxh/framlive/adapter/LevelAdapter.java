package com.tyxh.framlive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tyxh.framlive.R;
import com.tyxh.framlive.bean.LevelBean;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.ViewHolder> {
    private Context mContext;
    private List<LevelBean.RetDataBean.RankPrivilegeBeansBean> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private int level;

    public LevelAdapter(Context context, List<LevelBean.RetDataBean.RankPrivilegeBeansBean> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_level, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        LevelBean.RetDataBean.RankPrivilegeBeansBean bean = mLists.get(position);
        vh.mItemLevelImgvsecond.setVisibility(View.INVISIBLE);
        vh.mItemLevelImgvthird.setVisibility(View.INVISIBLE);
        vh.mItemLevelLockOne.setVisibility(View.GONE);
        vh.mItemLevelLockTwo.setVisibility(View.GONE);
        vh.mItemLevelLockThree.setVisibility(View.GONE);
        vh.mItemLevelZz.setVisibility(View.GONE);
        vh.mItemLevelTitle.setText((position + 1) + "级");
        String giftICO = bean.getGiftICO();//礼物标
        String releaseICO = bean.getReleaseICO();//咨询卡标
        String rewardDiaICO = bean.getRewardDiaICO();//钻石标
        int giftId = bean.getGiftId();//礼物id
        int releaseId = bean.getReleaseId();//咨询卡id
        int rewardDiaNum = bean.getRewardDiaNum();//钻石id
        String one_url = "";
        String two_url = "";
        String third_url = "";
        if (rewardDiaNum <= 0) {
            if (giftId <= 0) {
                one_url = releaseICO;
            } else {
                one_url = giftICO;
                if (releaseId > 0) {
                    two_url = releaseICO;
                    vh.mItemLevelImgvsecond.setVisibility(View.VISIBLE);
                }
            }
        } else {
            one_url = rewardDiaICO;
            if (giftId <= 0) {
                if (releaseId > 0) {
                    two_url = releaseICO;
                    vh.mItemLevelImgvsecond.setVisibility(View.VISIBLE);
                }
            } else {
                two_url = giftICO;
                vh.mItemLevelImgvsecond.setVisibility(View.VISIBLE);
                if (releaseId > 0) {
                    third_url = releaseICO;
                    vh.mItemLevelImgvthird.setVisibility(View.VISIBLE);
                }
            }
        }
        Glide.with(mContext).load(one_url).error(R.drawable.live_defaultimg).placeholder(R.drawable.live_defaultimg).into(vh.mItemLevelImgvone);
        Glide.with(mContext).load(two_url).error(R.drawable.live_defaultimg).placeholder(R.drawable.live_defaultimg).into(vh.mItemLevelImgvsecond);
        Glide.with(mContext).load(third_url).error(R.drawable.live_defaultimg).placeholder(R.drawable.live_defaultimg).into(vh.mItemLevelImgvthird);
        if (position > (level - 1)) {
            vh.mItemLevelLockOne.setVisibility(View.VISIBLE);
            if (vh.mItemLevelImgvsecond.getVisibility() == View.VISIBLE) {
                vh.mItemLevelLockTwo.setVisibility(View.VISIBLE);
            }
            if (vh.mItemLevelImgvthird.getVisibility() == View.VISIBLE) {
                vh.mItemLevelLockThree.setVisibility(View.VISIBLE);
            }
            vh.mItemLevelZz.setVisibility(View.VISIBLE);
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
        @BindView(R.id.item_level_title)
        TextView mItemLevelTitle;
        @BindView(R.id.item_level_imgvone)
        ImageView mItemLevelImgvone;
        @BindView(R.id.item_level_imgvsecond)
        ImageView mItemLevelImgvsecond;
        @BindView(R.id.item_level_imgvthird)
        ImageView mItemLevelImgvthird;
        @BindView(R.id.lock_one)
        ImageView mItemLevelLockOne;
        @BindView(R.id.lock_two)
        ImageView mItemLevelLockTwo;
        @BindView(R.id.lock_three)
        ImageView mItemLevelLockThree;
        @BindView(R.id.item_level_zhezhao)
        View mItemLevelZz;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
