package com.tyxh.framlive.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tyxh.framlive.R;
import com.tyxh.framlive.bean.MxdetailBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.ViewHolder> {
    private Context mContext;
    private List<MxdetailBean.RetDataBean.ListBean> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public WalletAdapter(Context context, List<MxdetailBean.RetDataBean.ListBean> stringList) {
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
        MxdetailBean.RetDataBean.ListBean bean = mLists.get(position);
        int moneyType = bean.getMoneyType();//1:进账  2：支出
        int proType = bean.getProType();
        //类型【moneyType=1:1-礼物;2-连线;3-私聊;4-任务；moneyType=2:1-收益兑钻】
        /*String title = "";
        if (moneyType == 1) {
            switch (proType) {
                case 1:
                    title = "礼物";
                    break;
                case 2:
                    title = "连线";
                    break;
                case 3:
                    title = "私聊";
                    break;
                case 4:
                    title = "任务";
                    break;
            }
        } else {
            title = "收益兑钻";
        }*/
        vh.mItemWalletType.setText(bean.getTitle());
        String createDate = bean.getCreateDate();
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createDate);
            vh.mItemWalletTptm.setText(new SimpleDateFormat("MM-dd").format(date));
            vh.mItemWalletLltm.setText(new SimpleDateFormat("HH:mm").format(date));
        } catch (ParseException e) {
            Log.e("WalletDiamondAdapter", "时间解析出现问题：" + e.toString());
        }

        vh.mItemWalletGoods.setText("商品名："+bean.getProName());
        vh.mItemWalletLlorder.setText("订单号：" + bean.getOrderNum());
        vh.mItemWalletConorder.setText("订单号：" + bean.getOrderNum());
        vh.mItemWalletBz.setText("备注：" + bean.getRemark());
        vh.mItemWalletContm.setText(bean.getCreateDate());

        vh.mItemWalletMoney.setText((moneyType == 1 ? "+" : "-") + bean.getAmount() + "");
        vh.mItemWalletMoney.setTextColor(mContext.getResources().getColor(moneyType == 1 ? R.color.qings : R.color.red));
        boolean show = bean.isShow();
        if (show) {
            vh.mItemWalletCon.setVisibility(View.VISIBLE);
            vh.mItemWalletLl.setVisibility(View.GONE);
            vh.mItemWalletTptm.setVisibility(View.GONE);
            vh.mItemWalletImg.setImageResource(R.drawable.wallet_top);
        } else {
            vh.mItemWalletCon.setVisibility(View.GONE);
            vh.mItemWalletLl.setVisibility(View.VISIBLE);
            vh.mItemWalletTptm.setVisibility(View.VISIBLE);
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
        @BindView(R.id.item_wallet_tptm)
        TextView mItemWalletTptm;
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
