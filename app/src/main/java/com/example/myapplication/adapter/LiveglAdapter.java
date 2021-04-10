package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LiveglAdapter extends RecyclerView.Adapter<LiveglAdapter.ViewHolder> {
    private Context mContext;
    private List<Map<String,Object>> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public LiveglAdapter(Context context, List<Map<String,Object>> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_livegl, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        Map<String,Object> bean = mLists.get(position);
        boolean select= (boolean) bean.get("select");
        vh.mItemLiveglTitle.setText("新人必读");
        vh.mItemLiveglImgv.setImageResource(select?R.drawable.fuchi_tp:R.drawable.fuchi_bt);
        vh.mItemLiveglLine.setVisibility(select?View.VISIBLE:View.GONE);
        vh.mItemLiveglRecy.setVisibility(select?View.VISIBLE:View.GONE);


        List<String> lch_lists = (List<String>) bean.get("data");
        LiveglchildAdapter lch_ada = new LiveglchildAdapter(mContext,lch_lists);
        LinearLayoutManager li_man = new LinearLayoutManager(mContext);
        vh.mItemLiveglRecy.setLayoutManager(li_man);
        vh.mItemLiveglRecy.setAdapter(lch_ada);

        vh.mItemLiveglImgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener!=null)
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

    static class ViewHolder   extends RecyclerView.ViewHolder{
        @BindView(R.id.item_livegl_title)
        TextView mItemLiveglTitle;
        @BindView(R.id.item_livegl_imgv)
        ImageView mItemLiveglImgv;
        @BindView(R.id.item_livegl_line)
        View mItemLiveglLine;
        @BindView(R.id.item_livegl_recy)
        RecyclerView mItemLiveglRecy;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
