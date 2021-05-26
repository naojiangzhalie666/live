package com.tyxh.framlive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tyxh.framlive.R;
import com.tyxh.framlive.bean.UserDetailBean;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CarshowAdapter extends RecyclerView.Adapter<CarshowAdapter.ViewHolder> {
    private Context mContext;
    private List<UserDetailBean.RetDataBean.ServicePackagesBean> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public CarshowAdapter(Context context, List<UserDetailBean.RetDataBean.ServicePackagesBean> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_dialog_car, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        UserDetailBean.RetDataBean.ServicePackagesBean bean = mLists.get(position);
        vh.mItemDigcarTitle.setText(bean.getProTitle());
        vh.mItemDigcarTime.setText("");
        vh.mItemDigcarZuansh.setText(String.valueOf(bean.getLowestPrice()));
        vh.mItemDigcarTiyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onTiyanClickListener(position);
            }
        });
        vh.itemView.setOnClickListener(new View.OnClickListener() {
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
        void onTiyanClickListener(int pos);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_digcar_title)
        TextView mItemDigcarTitle;
        @BindView(R.id.item_digcar_zuansh)
        TextView mItemDigcarZuansh;
        @BindView(R.id.item_digcar_time)
        TextView mItemDigcarTime;
        @BindView(R.id.item_digcar_tiyan)
        TextView mItemDigcarTiyan;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
