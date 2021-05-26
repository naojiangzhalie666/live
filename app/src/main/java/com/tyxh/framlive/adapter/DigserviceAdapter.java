package com.tyxh.framlive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.tyxh.framlive.R;
import com.tyxh.framlive.bean.UserDetailBean;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DigserviceAdapter extends RecyclerView.Adapter<DigserviceAdapter.ViewHolder> {
    private Context mContext;
    private List<UserDetailBean.RetDataBean.ServicePackagesBean> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public DigserviceAdapter(Context context, List<UserDetailBean.RetDataBean.ServicePackagesBean> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_dig_service, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        UserDetailBean.RetDataBean.ServicePackagesBean bean = mLists.get(position);
        RoundedCorners roundedCorners = new RoundedCorners(15);
        Glide.with(mContext).load(bean.getProPicImg()).apply(new RequestOptions().transform(roundedCorners)).error(R.drawable.live_defaultimg).placeholder(R.drawable.live_defaultimg).into(vh.mItemServiceHead);
        vh.mItemServiceTitle.setText(bean.getProTitle());
        vh.mItemServiceNum.setText(String.valueOf(bean.getLowestPrice()));
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener!=null)
                    mOnItemClickListener.onItemClickListener(position,bean.getProTitle());
            }
        });


    }

    @Override
    public int getItemCount() {
        return mLists == null ? 0 : mLists.size();
    }

    public interface OnItemClickListener {
        void onItemClickListener(int pos,String title);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_service_head)
        ImageView mItemServiceHead;
        @BindView(R.id.item_service_title)
        TextView mItemServiceTitle;
        @BindView(R.id.item_service_num)
        TextView mItemServiceNum;
        @BindView(R.id.item_service_go)
        TextView mItemServiceGo;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
