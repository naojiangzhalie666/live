package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BotInterAdapter extends RecyclerView.Adapter<BotInterAdapter.ViewHolder> {
    private Context mContext;
    private List<Map<String, Object>> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private String content = "";
    private String content_id = "";

    public BotInterAdapter(Context context, List<Map<String, Object>> stringList) {
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
        Map<String, Object> bean = mLists.get(position);
        String title = (String) bean.get("title");
        vh.mItemBtTv.setText(title);
        vh.mItemBtTv.setTag(bean);
        vh.mItemBtTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv_new = v.findViewById(R.id.item_bt_tv);
                Map<String, Object> tag = (Map<String, Object>) v.getTag();
                String new_titl = (String) tag.get("title");
                int new_id = (int) tag.get("id");
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
                    mOnItemClickListener.onItemClickListener(content);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mLists == null ? 0 : mLists.size();
    }

    public interface OnItemClickListener {
        void onItemClickListener(String content);
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
