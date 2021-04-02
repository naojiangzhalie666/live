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

public class BotReAdapter extends RecyclerView.Adapter<BotReAdapter.ViewHolder> {
    private Context mContext;
    private List<Map<String, Object>> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private View onClickView;

    public BotReAdapter(Context context, List<Map<String, Object>> stringList) {
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
                        mOnItemClickListener.onItemClickListener((String) tv_new.getTag());
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mLists == null ? 0 : mLists.size();
    }

    public interface OnItemClickListener {
        void onItemClickListener(String  content);
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
