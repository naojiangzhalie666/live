package com.example.myapplication.adapter;

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
import com.example.myapplication.R;

import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CzAdapter extends RecyclerView.Adapter<CzAdapter.ViewHolder> {
    private Context mContext;
    private List<Map<String, Object>> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public CzAdapter(Context context, List<Map<String, Object>> stringList) {
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
        Map<String, Object> bean = mLists.get(position);
        vh.mItemCzTitle.setText("今年高考人数");
        RoundedCorners roundedCorners = new RoundedCorners(15);
        Glide.with(mContext).load(R.drawable.login_bg).apply(new RequestOptions().transform(new CenterCrop(),roundedCorners)).into(vh.mItemCzHead);

        vh.mItemCzZsnum.setText("钻石数量：" + "40+" + position);
        vh.mItemCzCreattime.setText("创建时间：" + "2010.02.02 12:22");
        vh.mItemCzCode.setText("订单编号：" + "124煞了苦痛23" + position);
        vh.mItemCzMoney.setText("1" + position);
        if (position % 2 == 0) {
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
