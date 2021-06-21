package com.tyxh.framlive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tyxh.framlive.R;
import com.tyxh.framlive.bean.DiamondBean;

import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BuyAdapter extends RecyclerView.Adapter<BuyAdapter.ViewHolder> {
    private Context mContext;
    private List<DiamondBean.RetDataBean.ListBean> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private boolean look_more = false;
    private View onClicView;

    public BuyAdapter(Context context, List<DiamondBean.RetDataBean.ListBean> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setLook_more(boolean look_more) {
        this.look_more = look_more;
        notifyDataSetChanged();
        onClicView = null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_buyzuan, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        if (!look_more && position == 3) {
            vh.mItemBuyCon.setVisibility(View.INVISIBLE);
            vh.mDigBuyRel.setVisibility(View.VISIBLE);
            vh.mDigBuyRtp.setVisibility(View.GONE);
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null)
                        mOnItemClickListener.onLookMoreListener();
                }
            });
        } else {
            DiamondBean.RetDataBean.ListBean bean = mLists.get(position);
            boolean select = bean.isSelect();
            vh.mItemBuyCon.setBackgroundResource(select ? R.drawable.buy_bg_se : R.drawable.buy_bg_unse);
            vh.mItemBuyCon.setVisibility(View.VISIBLE);
            vh.mDigBuyRel.setVisibility(View.GONE);
            vh.mDigBuyZuan.setText(bean.getProNum() + "钻石");
            int discountType = bean.getDiscountType();
            vh.mDigBuyMoney.setText(bean.getOriginalPrice() + "元");
            switch (discountType) {//优惠类型(1:加赠;2:折扣;3:特价)
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    vh.mDigBuyMoney.setText(bean.getSpecialOffer() + "元");
                    break;
            }
            int firstCharge = bean.getFirstCharge();
            if(firstCharge ==1){//首充  1：是  0：否
                vh.mDigBuyRtp.setVisibility(View.VISIBLE);
                vh.mDigBuyRtp.setText("首充");
                vh.mDigBuyRtp.setBackgroundResource(R.drawable.cz_grad_shouc);
            }else{
                vh.mDigBuyRtp.setVisibility(View.GONE);
            }
            //                R.drawable.cz_grad_chaoh  超嗨

            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view != onClicView) {
                        if (onClicView != null) {
                            ConstraintLayout con_old = onClicView.findViewById(R.id.item_buy_con);
                            con_old.setBackgroundResource(R.drawable.buy_bg_unse);
                        }
                        ConstraintLayout con_new = view.findViewById(R.id.item_buy_con);
                        con_new.setBackgroundResource(R.drawable.buy_bg_se);
                        onClicView = view;
                    }
                    if (mOnItemClickListener != null)
                        mOnItemClickListener.onItemClickListener(position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mLists == null ? 0 : (look_more ? mLists.size() : (mLists.size() > 4 ? 4 : mLists.size()));
    }

    public interface OnItemClickListener {
        void onItemClickListener(int pos);

        void onLookMoreListener();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.dig_buy_zuan)
        TextView mDigBuyZuan;
        @BindView(R.id.buyzuan_righttp)
        TextView mDigBuyRtp;
        @BindView(R.id.dig_buy_money)
        TextView mDigBuyMoney;
        @BindView(R.id.item_buy_con)
        ConstraintLayout mItemBuyCon;
        @BindView(R.id.dig_buy_rel)
        RelativeLayout mDigBuyRel;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
