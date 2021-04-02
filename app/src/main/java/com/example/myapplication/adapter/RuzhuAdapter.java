package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RuzhuAdapter extends RecyclerView.Adapter<RuzhuAdapter.ViewHolder> {
    private Context mContext;
    private List<String> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private View onClickView;
    private String content="";

    public RuzhuAdapter(Context context, List<String> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recyruzhu, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        String bean = mLists.get(position);
        vh.mItemNianTitle.setText(bean);
        vh.mItemNianTitle.setTag(bean);
        vh.mItemNianTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv_new = v.findViewById(R.id.item_nian_title);
                String now_con = (String) tv_new.getTag();
                if(content.contains(now_con)){
                    tv_new.setTextColor(mContext.getResources().getColor(R.color.sixsixsix));
                    tv_new.setBackgroundResource(R.drawable.bg_circle_stoke_gray);
                    String tag = (String) tv_new.getTag();
                    content= content.replaceAll(tag+",","");
                }else {
                    tv_new.setTextColor(mContext.getResources().getColor(R.color.white));
                    tv_new.setBackgroundResource(R.drawable.bg_circle_solder_setin);
                    content += (String) tv_new.getTag()+",";
                }
                if(mOnItemClickListener!=null)
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
        @BindView(R.id.item_nian_title)
        TextView mItemNianTitle;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
