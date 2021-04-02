package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ExchangeAdapter extends RecyclerView.Adapter<ExchangeAdapter.ViewHolder> {
    private Context mContext;
    private List<String> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private View onClickView;

    public ExchangeAdapter(Context context, List<String> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_exchange_recy, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        String bean = mLists.get(position);
        vh.mItemExZuanshi.setText(((position + 1) * 100) + "");
        vh.mItemExMoney.setText(((position + 1) * 10) + "元");
        if (position % 2 == 0) {
            vh.mItemExFrondz.setVisibility(View.VISIBLE);
            vh.mItemExAddzuanshi.setText("+" + (position * 2));
        }else{
            vh.mItemExFrondz.setVisibility(View.INVISIBLE);
            vh.mItemExAddzuanshi.setText("");
        }
        vh.itemView.setTag(position);
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view != onClickView) {
                    if (onClickView != null) {
                        ConstraintLayout cons_old = onClickView.findViewById(R.id.con);
                        cons_old.setBackgroundResource(R.drawable.bg_corner_ex_white);
                    }
                    ConstraintLayout cons_new = view.findViewById(R.id.con);
                    cons_new.setBackgroundResource(R.drawable.bg_corner_ex_red);
                    onClickView = view;
                }else{
                    ConstraintLayout cons_new = view.findViewById(R.id.con);
                    cons_new.setBackgroundResource(R.drawable.bg_corner_ex_white);
                    onClickView = null;
                }
                if(mOnItemClickListener!=null)
                    mOnItemClickListener.onItemClickListener(onClickView!=null?(int)onClickView.getTag():-1);
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
        @BindView(R.id.item_ex_zuanshi)
        TextView mItemExZuanshi;
        @BindView(R.id.item_ex_addzuanshi)
        TextView mItemExAddzuanshi;
        @BindView(R.id.item_ex_money)
        TextView mItemExMoney;
        @BindView(R.id.item_ex_frondz)
        TextView mItemExFrondz;
        @BindView(R.id.con)
        ConstraintLayout mItemCon;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
