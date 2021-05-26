package com.tyxh.framlive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tyxh.framlive.R;

import java.util.List;
import java.util.Map;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.ViewHolder> {
    private Context mContext;
    private List<Map<String, Object>> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public WalletAdapter(Context context, List<Map<String, Object>> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_wallet_recy, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        Map<String, Object> bean = mLists.get(position);
        vh.mItemWalletType.setText("充值");
        vh.mItemWalletLltm.setText("12:00");
        vh.mItemWalletLlorder.setText("订单号：124124124");
        vh.mItemWalletConorder.setText("订单号：12312312312");
        vh.mItemWalletGoods.setText("商品名：gdsjl");
        vh.mItemWalletBz.setText("备注：赶紧啥贷款");
        vh.mItemWalletContm.setText("2019.01.22");
        vh.mItemWalletMoney.setText("+4" + position);
        vh.mItemWalletMoney.setTextColor(mContext.getResources().getColor(position % 2 == 0 ? R.color.qings : R.color.red));
        boolean show = (boolean) bean.get("show");
        if (show) {
            vh.mItemWalletCon.setVisibility(View.VISIBLE);
            vh.mItemWalletLl.setVisibility(View.GONE);
            vh.mItemWalletImg.setImageResource(R.drawable.wallet_top);
        } else {
            vh.mItemWalletCon.setVisibility(View.GONE);
            vh.mItemWalletLl.setVisibility(View.VISIBLE);
            vh.mItemWalletImg.setImageResource(R.drawable.wallet_bt);
        }
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null)
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
        @BindView(R.id.item_wallet_lltm)
        TextView mItemWalletLltm;
        @BindView(R.id.item_wallet_llorder)
        TextView mItemWalletLlorder;
        @BindView(R.id.item_wallet_ll)
        LinearLayout mItemWalletLl;
        @BindView(R.id.item_wallet_money)
        TextView mItemWalletMoney;
        @BindView(R.id.item_wallet_img)
        ImageView mItemWalletImg;
        @BindView(R.id.textView62)
        View mTextView62;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
