package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DigsjbgAdapter extends RecyclerView.Adapter<DigsjbgAdapter.ViewHolder> {
    private Context mContext;
    private List<String> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public DigsjbgAdapter(Context context, List<String> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_dig_sjbg, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        String bean = mLists.get(position);
        if(position%2==0){
            vh.mItemServiceHead.setImageResource(R.drawable.sjbg_video);
        }else{
            vh.mItemServiceHead.setImageResource(R.drawable.sjbg_line);
        }
        vh.mItemServiceTitle.setText("这里是录入"+position);
        vh.mItemServiceNum.setText("2021.03.2"+position);
        vh.mItemServiceGo.setText((position+1)+"0分钟");
        vh.itemView.setOnClickListener(new View.OnClickListener() {
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

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_service_head)
        ImageView mItemServiceHead;
        @BindView(R.id.item_service_title)
        TextView mItemServiceTitle;
        @BindView(R.id.item_service_num)
        TextView mItemServiceNum;
        @BindView(R.id.item_service_go)
        TextView mItemServiceGo;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
