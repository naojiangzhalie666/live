package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;

import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TimesAdapter extends RecyclerView.Adapter<TimesAdapter.ViewHolder> {
    private Context mContext;
    private List<Map<String, Object>> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public TimesAdapter(Context context, List<Map<String, Object>> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_times, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        Map<String, Object> bean = mLists.get(position);
        Glide.with(mContext).load(R.drawable.ruzhu_bg).apply(new RequestOptions().circleCrop()).into(vh.mItemTimesImg);
        vh.mItemTimesName.setText("王大拿" + position);
        vh.mItemTimesNum.setText("" + position);
        vh.mItemTimesContent.setText((position+1)+"分钟疏解");
        boolean select = (boolean) bean.get("select");
        vh.mItemTimesContent.setBackgroundResource(select?R.drawable.mine_grad:R.drawable.bg_circle_gray);
        vh.mItemTimesContent.setTextColor(mContext.getResources().getColor(select?R.color.login_txt:R.color.nineninenine));
        vh.itemView.setBackgroundResource(select?R.drawable.bg_stroke_setin:R.drawable.bg_stroke_ft);
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
        @BindView(R.id.item_times_img)
        ImageView mItemTimesImg;
        @BindView(R.id.item_times_name)
        TextView mItemTimesName;
        @BindView(R.id.item_times_num)
        TextView mItemTimesNum;
        @BindView(R.id.item_times_content)
        TextView mItemTimesContent;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
