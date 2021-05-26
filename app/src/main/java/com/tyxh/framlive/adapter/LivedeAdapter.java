package com.tyxh.framlive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tyxh.framlive.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LivedeAdapter extends RecyclerView.Adapter<LivedeAdapter.ViewHolder> {
    private Context mContext;
    private List<String> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private boolean is_day;

    public LivedeAdapter(Context context, List<String> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setIs_day(boolean is_day) {
        this.is_day = is_day;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_live, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        String bean = mLists.get(position);
        vh.mItemLiveDays.setVisibility(is_day ? View.GONE : View.VISIBLE);
        vh.mItemLiveDate.setText("2021.03");
        vh.mItemLiveTime.setText("1"+position);
        vh.mItemLiveGet.setText("10"+position);
        vh.mItemLivePeople.setText("12"+position);
        vh.mItemLiveDays.setText(""+position);
        vh.itemView.setBackgroundColor(mContext.getResources().getColor(position%2==0?R.color.white:R.color.fninfnie));


    }

    @Override
    public int getItemCount() {
        return mLists == null ? 0 : mLists.size();
    }

    public interface OnItemClickListener {
        void onItemClickListener(int pos);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_live_days)
        TextView mItemLiveDays;
        @BindView(R.id.item_live_date)
        TextView mItemLiveDate;
        @BindView(R.id.item_live_time)
        TextView mItemLiveTime;
        @BindView(R.id.item_live_get)
        TextView mItemLiveGet;
        @BindView(R.id.item_live_people)
        TextView mItemLivePeople;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
