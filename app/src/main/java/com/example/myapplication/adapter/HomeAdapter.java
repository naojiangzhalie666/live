package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.xzb.utils.onlinelive.TCVideoInfo;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private Context mContext;
    private List<TCVideoInfo> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private OnLastClickListener mOnLastClickListener;

    public HomeAdapter(Context context, List<TCVideoInfo> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnLastClickListener(OnLastClickListener onLastClickListener) {
        mOnLastClickListener = onLastClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_homerecy, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        if (position != mLists.size()) {
            TCVideoInfo bean = mLists.get(position);
            vh.mItemHomerecyBiao.setText("人气主播");
            vh.mItemHomerecyRenqi.setText("333");
            vh.mItemHomerecyName.setText("苏联军队攻克" + (position + 1));
            vh.mItemHomerecyTitle.setText("了就是看见两个赛季格洛克十九公里是德国");
            switch (position) {
                case 1:
                    vh.mItemHomerecyImgv.setBackgroundResource(R.drawable.home_white);
                    vh.mItemHomerecyImgvState.setVisibility(View.VISIBLE);
                    vh.mItemHomerecyImgvState.setImageResource(R.drawable.home_media);
                    vh.mItemHomerecyName.setTextColor(mContext.getResources().getColor(R.color.black));
                    vh.mItemHomerecyLinear.setVisibility(View.GONE);
                    vh.mItemHomerecyTitle.setVisibility(View.GONE);
                    break;
                case 3:
                    vh.mItemHomerecyImgv.setBackgroundResource(R.drawable.home_white);
                    vh.mItemHomerecyImgvState.setVisibility(View.VISIBLE);
                    vh.mItemHomerecyImgvState.setImageResource(R.drawable.home_yuyin);
                    vh.mItemHomerecyName.setTextColor(mContext.getResources().getColor(R.color.black));
                    vh.mItemHomerecyLinear.setVisibility(View.GONE);
                    vh.mItemHomerecyTitle.setVisibility(View.GONE);
                    break;
                case 6:
                    vh.mItemHomerecyImgv.setBackgroundResource(R.drawable.home_white);
                    vh.mItemHomerecyImgvState.setVisibility(View.VISIBLE);
                    vh.mItemHomerecyImgvState.setImageResource(R.drawable.home_media);
                    vh.mItemHomerecyName.setTextColor(mContext.getResources().getColor(R.color.black));
                    vh.mItemHomerecyLinear.setVisibility(View.GONE);
                    vh.mItemHomerecyTitle.setVisibility(View.GONE);
                    break;
                case 9:
                    vh.mItemHomerecyImgv.setBackgroundResource(R.drawable.home_white);
                    vh.mItemHomerecyImgvState.setVisibility(View.VISIBLE);
                    vh.mItemHomerecyImgvState.setImageResource(R.drawable.home_media);
                    vh.mItemHomerecyName.setTextColor(mContext.getResources().getColor(R.color.black));
                    vh.mItemHomerecyLinear.setVisibility(View.GONE);
                    vh.mItemHomerecyTitle.setVisibility(View.GONE);
                    break;
            }
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mOnItemClickListener!=null)
                        mOnItemClickListener.onItemClickListener(position);
                }
            });
        } else {
            vh.mItemHomerecyImgv.setBackground(null);
            vh.mItemHomerecyImgv.setImageResource(R.drawable.home_more);
            vh.mItemHomerecyBiao.setVisibility(View.GONE);
            vh.mItemHomerecyRenqi.setVisibility(View.GONE);
            vh.mItemHomerecyName.setVisibility(View.GONE);
            vh.mItemHomerecyTitle.setVisibility(View.GONE);
            vh.mItemHomerecyLinear.setVisibility(View.GONE);
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mOnLastClickListener!=null)
                        mOnLastClickListener.onLastClickListener();
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return mLists == null ? 1 : mLists.size() + 1;
    }

    public interface OnItemClickListener {
        void onItemClickListener(int pos);
    }

    public interface OnLastClickListener{
        void onLastClickListener();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_homerecy_imgv)
        ImageView mItemHomerecyImgv;
        @BindView(R.id.item_homerecy_imgvstate)
        ImageView mItemHomerecyImgvState;
        @BindView(R.id.item_homerecy_biao)
        TextView mItemHomerecyBiao;
        @BindView(R.id.item_homerecy_renqi)
        TextView mItemHomerecyRenqi;
        @BindView(R.id.item_homerecy_name)
        TextView mItemHomerecyName;
        @BindView(R.id.item_homerecy_title)
        TextView mItemHomerecyTitle;
        @BindView(R.id.item_homelinear)
        LinearLayout mItemHomerecyLinear;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
