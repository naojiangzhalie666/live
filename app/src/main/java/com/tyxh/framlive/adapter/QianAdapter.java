package com.tyxh.framlive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tyxh.framlive.bean.InterestBean;
import com.tyxh.xzb.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class QianAdapter extends RecyclerView.Adapter<QianAdapter.ViewHolder> {
    private Context mContext;
    private List<InterestBean.RetDataBean> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public QianAdapter(Context context, List< InterestBean.RetDataBean> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_qian, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, final int position) {
        InterestBean.RetDataBean bean = mLists.get(position);
        vh.mtv_title.setText(bean.getLabelName());
        boolean select =  bean.isSelect();
        vh.itemView.setBackgroundResource(select?R.drawable.bg_qian_un:R.drawable.bg_qian_se);
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

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mtv_title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mtv_title = itemView.findViewById(R.id.item_qian_tv);
        }
    }
}

