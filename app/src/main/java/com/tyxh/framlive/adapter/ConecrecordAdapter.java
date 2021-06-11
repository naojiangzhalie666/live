package com.tyxh.framlive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tyxh.framlive.R;
import com.tyxh.framlive.bean.ConLivetctBean;

import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ConecrecordAdapter extends RecyclerView.Adapter<ConecrecordAdapter.ViewHolder> {
    private Context mContext;
    private List<ConLivetctBean.RetDataBean.ListBean> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public ConecrecordAdapter(Context context, List<ConLivetctBean.RetDataBean.ListBean> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_conrecord, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        ConLivetctBean.RetDataBean.ListBean bean = mLists.get(position);
        vh.mItemWalletType.setText("连线ID："+bean.getUserId());
        vh.mItemWalletConorder.setText("昵称："+bean.getNickname());
        vh.mItemWalletGoods.setText("连线时间："+bean.getCreateDate());
        vh.mItemWalletBz.setText("备注："+bean.getRemark());
        vh.mItemWalletContm.setText("订单编号："+bean.getUuId());
        vh.mItemWalletMoney.setOnClickListener(new View.OnClickListener() {
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

    static class ViewHolder   extends RecyclerView.ViewHolder{
        @BindView(R.id.item_wallet_type)
        TextView mItemWalletType;
        @BindView(R.id.item_wallet_conorder)
        TextView mItemWalletConorder;
        @BindView(R.id.item_wallet_goods)
        TextView mItemWalletGoods;
        @BindView(R.id.item_wallet_bz)
        TextView mItemWalletBz;
        @BindView(R.id.item_wallet_contm)
        TextView mItemWalletContm;
        @BindView(R.id.item_wallet_con)
        ConstraintLayout mItemWalletCon;
        @BindView(R.id.item_wallet_money)
        TextView mItemWalletMoney;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
