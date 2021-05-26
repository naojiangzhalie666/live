package com.tyxh.framlive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tyxh.framlive.R;
import com.tyxh.framlive.bean.InterestBean;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BotReAdapter extends RecyclerView.Adapter<BotReAdapter.ViewHolder> {
    private Context mContext;
    private List<InterestBean.RetDataBean> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private View onClickView;

    public BotReAdapter(Context context, List<InterestBean.RetDataBean> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_bot_recy, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        InterestBean.RetDataBean bean = mLists.get(position);
        String title = bean.getLabelName();
        vh.mItemBtTv.setText(title);
        vh.mItemBtTv.setTag(title);
        vh.mItemBtTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view != onClickView){
                    if(onClickView!=null){
                        TextView tv_old = onClickView.findViewById(R.id.item_bt_tv);
                        tv_old.setBackgroundResource(R.drawable.home_ft);
                    }
                    TextView tv_new = view.findViewById(R.id.item_bt_tv);
                    tv_new.setBackgroundResource(R.drawable.home_bf);
                    onClickView = view;
                    if(mOnItemClickListener!=null)
                        mOnItemClickListener.onItemClickListener((String) tv_new.getTag(),position);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mLists == null ? 0 : mLists.size();
    }

    public static abstract class OnItemClickListener {
        public void onItemClickListener(String  content,int pos){};
        public void onItemClickListener(String  content){};
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_bt_tv)
        TextView mItemBtTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
