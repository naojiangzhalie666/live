package com.tyxh.framlive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tyxh.framlive.R;
import com.tyxh.xzb.ui.TCSimpleUserInfo;
import com.tyxh.xzb.utils.TCUtils;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OnlineAdapter extends RecyclerView.Adapter<OnlineAdapter.ViewHolder> {
    private Context mContext;
    private List<TCSimpleUserInfo> mUserAvatarList;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private View clickView;
    private String mPusherId;//主播id
    private final static int TOP_STORGE_MEMBER = 50;    //最大容纳量

    public OnlineAdapter(Context context, List<TCSimpleUserInfo> stringList, String pusherId) {
        mContext = context;
        mUserAvatarList = stringList;
        mPusherId = pusherId;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_online, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }
    public void onReset(){
        clickView = null;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        TCSimpleUserInfo bean = mUserAvatarList.get(position);
//        boolean state = (boolean) bean.get("state");
//        Glide.with(mContext).load(bean.avatar).error(R.drawable.man_se).placeholder(R.drawable.man_se).into(vh.mItemOnlineHead);
        TCUtils.showPicWithUrl(mContext, vh.mItemOnlineHead, mUserAvatarList.get(position).avatar, com.tyxh.xzb.R.drawable.face);
        vh.mItemOnlineName.setText(bean.nickname);
        vh.mItemOnlineState.setText(position % 2 == 0 ? "连线过" : "未连线");
        vh.itemView.setBackgroundResource(R.color.white);
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view != clickView) {
                    if (clickView != null) {
                        LinearLayout ll_old = clickView.findViewById(R.id.item_online_ll);
                        ll_old.setBackgroundResource(R.color.white);
                    }
                    LinearLayout ll_old = view.findViewById(R.id.item_online_ll);
                    ll_old.setBackgroundResource(R.color.ftwo);
                    clickView = view;
                }
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClickListener(position);
            }
        });

    }

    /**
     * 添加用户信息
     *
     * @param userInfo 用户基本信息
     * @return 存在重复或头像为主播则返回false
     */
    public boolean addItem(TCSimpleUserInfo userInfo) {

        //去除主播头像
        if (userInfo.userid.equals(mPusherId))
            return false;

        //去重操作
        for (TCSimpleUserInfo tcSimpleUserInfo : mUserAvatarList) {
            if (tcSimpleUserInfo.userid.equals(userInfo.userid))
                return false;
        }

        //始终显示新加入item为第一位
        mUserAvatarList.add(0, userInfo);
        //超出时删除末尾项
        if (mUserAvatarList.size() > TOP_STORGE_MEMBER) {
            mUserAvatarList.remove(TOP_STORGE_MEMBER);
            notifyItemRemoved(TOP_STORGE_MEMBER);
        }
        notifyItemInserted(0);
        return true;
    }

    public void removeItem(String userId) {
        TCSimpleUserInfo tempUserInfo = null;

        for (TCSimpleUserInfo userInfo : mUserAvatarList)
            if (userInfo.userid.equals(userId))
                tempUserInfo = userInfo;


        if (null != tempUserInfo) {
            mUserAvatarList.remove(tempUserInfo);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return mUserAvatarList == null ? 0 : mUserAvatarList.size();
    }

    public interface OnItemClickListener {
        void onItemClickListener(int pos);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_online_head)
        ImageView mItemOnlineHead;
        @BindView(R.id.item_online_name)
        TextView mItemOnlineName;
        @BindView(R.id.item_online_state)
        TextView mItemOnlineState;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}