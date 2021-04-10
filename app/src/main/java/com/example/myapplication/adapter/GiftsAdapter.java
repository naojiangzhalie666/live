package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GiftsAdapter extends RecyclerView.Adapter<GiftsAdapter.ViewHolder> {
    private Context mContext;
    private List<Map<String,Object>> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public GiftsAdapter(Context context, List<Map<String,Object>> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_gifts, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        Map<String,Object> bean = mLists.get(position);
        vh.mItemGiftsNum.setText(""+position);
        vh.mItemGiftsTitle.setText("玫瑰花"+position);
        Glide.with(mContext).load(R.drawable.mine_flower).into(vh.mItemGiftsImg);
        boolean select= (boolean) bean.get("select");
        vh.itemView.setBackgroundResource(select?R.drawable.bg_stroke_setin:0);
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

    static class ViewHolder   extends RecyclerView.ViewHolder{
        @BindView(R.id.item_gifts_img)
        ImageView mItemGiftsImg;
        @BindView(R.id.item_gifts_title)
        TextView mItemGiftsTitle;
        @BindView(R.id.item_gifts_num)
        TextView mItemGiftsNum;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
