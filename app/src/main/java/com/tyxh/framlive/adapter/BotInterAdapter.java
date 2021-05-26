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

public class BotInterAdapter extends RecyclerView.Adapter<BotInterAdapter.ViewHolder> {
    private Context mContext;
    private List<InterestBean.RetDataBean> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private String content = "";
    private String content_id = "";

    public BotInterAdapter(Context context, List<InterestBean.RetDataBean> stringList) {
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
        vh.mItemBtTv.setTag(bean);
        vh.mItemBtTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv_new = v.findViewById(R.id.item_bt_tv);
                InterestBean.RetDataBean tag = (InterestBean.RetDataBean) v.getTag();
                String new_titl =tag.getLabelName();
                int new_id =tag.getId();
                if (content.contains(new_titl)) {
                    tv_new.setBackgroundResource(R.drawable.home_ft);
                    content = content.replaceAll(new_titl + ",", "");
                    content_id = content_id.replaceAll(new_id + ",", "");
                } else {
                    tv_new.setBackgroundResource(R.drawable.home_bf);
                    content += new_titl + ",";
                    content_id += new_id + ",";
                }
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClickListener(content,content_id);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mLists == null ? 0 : mLists.size();
    }

    public interface OnItemClickListener {
        void onItemClickListener(String content,String id);
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
