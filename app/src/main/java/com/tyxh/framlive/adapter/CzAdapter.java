package com.tyxh.framlive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.tyxh.framlive.R;
import com.tyxh.framlive.bean.OrderBean;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CzAdapter extends RecyclerView.Adapter<CzAdapter.ViewHolder> {
    private Context mContext;
    private List<OrderBean.RetDataBean.ListBean> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public CzAdapter(Context context, List<OrderBean.RetDataBean.ListBean> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_cz, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        OrderBean.RetDataBean.ListBean bean = mLists.get(position);
        vh.mItemCzCreattime.setText("创建时间：" + bean.getCreateDate());
        vh.mItemCzCode.setText("订单编号：" + bean.getOrderSn());
        vh.mItemCzMoney.setText(bean.getAmount() + "");
      /*  int orderType = bean.getOrderType();
        Object payType = bean.getPayType();
        if(orderType==1){
            if(null!=payType){
                vh.mItemCzTitle.setText((Double)payType==1?"微信充值":"支付宝充值");
            }
        }else{
            vh.mItemCzTitle.setText("收益兑钻");
        }*/
        OrderBean.RetDataBean.ListBean.DiamondBagBean diamondBag = bean.getDiamondBag();
        RoundedCorners roundedCorners = new RoundedCorners(15);
        Glide.with(mContext).load(R.drawable.live_defaultimg).apply(new RequestOptions().transform(new CenterCrop(), roundedCorners)).into(vh.mItemCzHead);
        vh.mItemCzZsnum.setText("钻石数量：" + diamondBag.getProNum());
        vh.mItemCzTitle.setText(diamondBag.getProName()+(bean.getOrderType()==2?"收益兑钻":""));
        int discountType = diamondBag.getDiscountType();
        switch (discountType) {//1:加赠;2:折扣;3:特价)
            case 1:
                vh.mItemCzZsnum.setText("钻石数量：" + diamondBag.getProNum() + "+" + bean.getGiveNum());
                break;
            case 2:
                break;
            case 3:
                break;
        }
        vh.mItemCzBtstate.setText("更多充值");
        int orderStatus = bean.getOrderStatus();
        switch (orderStatus) {//(1:待支付;2:支付成功;3:关闭;4:退款;5:已评价)
            case 1:
                vh.mItemCzTopstate.setText("待支付");
                vh.mItemCzTopstate.setTextColor(mContext.getResources().getColor(R.color.order_ef));
                vh.mItemCzTopstate.setBackgroundResource(R.drawable.bg_solder_ef);
                vh.mItemCzBtstate.setText("支付");
                break;
            case 2:
                vh.mItemCzTopstate.setText("已完成");
                vh.mItemCzTopstate.setTextColor(mContext.getResources().getColor(R.color.nineninenine));
                vh.mItemCzTopstate.setBackgroundResource(R.drawable.bg_solder_nin);
//                if (position == 2) {
//                    vh.mItemCzBtstate.setText("更多充值");
//                } else {
//                vh.mItemCzBtstate.setText("再次购买");
//                }
                break;
            case 3:
                vh.mItemCzTopstate.setText("关闭");
                vh.mItemCzTopstate.setTextColor(mContext.getResources().getColor(R.color.nineninenine));
                vh.mItemCzTopstate.setBackgroundResource(R.drawable.bg_solder_nin);
//                if (position == 2) {
//                    vh.mItemCzBtstate.setText("更多充值");
//                } else {
//                vh.mItemCzBtstate.setText("再次购买");
//                }
                break;
            case 4:
                vh.mItemCzTopstate.setText("退款");
                vh.mItemCzTopstate.setTextColor(mContext.getResources().getColor(R.color.nineninenine));
                vh.mItemCzTopstate.setBackgroundResource(R.drawable.bg_solder_nin);
//                if (position == 2) {
//                    vh.mItemCzBtstate.setText("更多充值");
//                } else {
//                vh.mItemCzBtstate.setText("再次购买");
//                }
                break;
            case 5:
                break;
        }
        vh.mItemCzBtstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener!=null)
                    mOnItemClickListener.onItemClickListener(position);
            }
        });
       /* if (position % 2 == 0) {
            vh.mItemCzTopstate.setText("已完成");
            vh.mItemCzTopstate.setTextColor(mContext.getResources().getColor(R.color.nineninenine));
            vh.mItemCzTopstate.setBackgroundResource(R.drawable.bg_solder_nin);
            if (position == 2) {
                vh.mItemCzBtstate.setText("更多充值");
            } else {
                vh.mItemCzBtstate.setText("再次购买");
            }
        } else {
            vh.mItemCzTopstate.setText("待支付");
            vh.mItemCzTopstate.setTextColor(mContext.getResources().getColor(R.color.order_ef));
            vh.mItemCzTopstate.setBackgroundResource(R.drawable.bg_solder_ef);
            vh.mItemCzBtstate.setText("支付");
        }

*/
    }

    @Override
    public int getItemCount() {
        return mLists == null ? 0 : mLists.size();
    }

    public interface OnItemClickListener {
        void onItemClickListener(int pos);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_cz_code)
        TextView mItemCzCode;
        @BindView(R.id.item_cz_title)
        TextView mItemCzTitle;
        @BindView(R.id.item_cz_topstate)
        TextView mItemCzTopstate;
        @BindView(R.id.item_cz_head)
        ImageView mItemCzHead;
        @BindView(R.id.item_cz_zsnum)
        TextView mItemCzZsnum;
        @BindView(R.id.item_cz_creattime)
        TextView mItemCzCreattime;
        @BindView(R.id.item_cz_money)
        TextView mItemCzMoney;
        @BindView(R.id.item_cz_btstate)
        TextView mItemCzBtstate;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
