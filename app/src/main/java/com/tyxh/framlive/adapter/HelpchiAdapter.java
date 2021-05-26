package com.tyxh.framlive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tyxh.framlive.R;

import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HelpchiAdapter extends RecyclerView.Adapter<HelpchiAdapter.ViewHolder> {
    private Context mContext;
    private List<Map<String, Object>> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public HelpchiAdapter(Context context, List<Map<String, Object>> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_helpchild, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        Map<String, Object> bean = mLists.get(position);
        vh.mItemHelpchTitle.setText("标题" + (position + 1));
        vh.mItemHelpchContent.setText((position + 1) + "告诉你就看那个io弄该哦问欧文ie工位哦哦各位能给我个io恩哦I给我了我诶i" +
                "n我鞥哦啊你哦那我io恩哦on给我咯恩哦I我跟咯ie怪我咯ie我咯I速度过来认识的那个人是当你噶啥你那里了我来两个人了");
        boolean select = (boolean) bean.get("select");
        vh.mItemHelpchImgv.setImageResource(select ? R.drawable.helpback_bt : R.drawable.helpback_ri);
        vh.mItemHelpchLine.setVisibility(select ? View.GONE : View.VISIBLE);
        vh.mItemHelpchContent.setVisibility(select ? View.VISIBLE : View.GONE);
        vh.mItemHelpchImgv.setTag(bean);
        vh.mItemHelpchTitle.setTag(bean);

        vh.mItemHelpchImgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> bean = (Map<String, Object>) view.getTag();
                toChange(position, bean);
            }
        });
        vh.mItemHelpchTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> bean = (Map<String, Object>) view.getTag();
                toChange(position, bean);
            }
        });
    }

    private void toChange(int pos, Map<String, Object> bean) {
        boolean select = (boolean) bean.get("select");
        bean.put("select",!select);
        notifyItemChanged(pos);
    }

    @Override
    public int getItemCount() {
        return mLists == null ? 0 : mLists.size();
    }

    public interface OnItemClickListener {
        void onItemClickListener(int pos);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_helpch_title)
        TextView mItemHelpchTitle;
        @BindView(R.id.item_helpch_imgv)
        ImageView mItemHelpchImgv;
        @BindView(R.id.item_helpch_line)
        View mItemHelpchLine;
        @BindView(R.id.item_helpch_content)
        TextView mItemHelpchContent;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
