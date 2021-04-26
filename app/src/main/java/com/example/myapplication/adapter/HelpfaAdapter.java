package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HelpfaAdapter extends RecyclerView.Adapter<HelpfaAdapter.ViewHolder> {
    private Context mContext;
    private List<String> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;


    public HelpfaAdapter(Context context, List<String> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_helpfat, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        String bean = mLists.get(position);
        vh.mHelpfatTitle.setText("这里是运营的一些问题" + position);

        LinearLayoutManager l_vertical = new LinearLayoutManager(mContext);
        vh.mHelpfatRecy.setLayoutManager(l_vertical);
        List<Map<String,Object>> maps_chi = new ArrayList<>();
        for (int i = 0; i < (position + 1); i++) {
            Map<String,Object > map = new HashMap<>();
            map.put("select",false);
            maps_chi.add(map);
        }
        HelpchiAdapter helpchiAdapter= new HelpchiAdapter(mContext,maps_chi);
        vh.mHelpfatRecy.setAdapter(helpchiAdapter);


    }

    @Override
    public int getItemCount() {
        return mLists == null ? 0 : mLists.size();
    }

    public interface OnItemClickListener {
        void onItemClickListener(int pos);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.helpfat_title)
        TextView mHelpfatTitle;
        @BindView(R.id.helpfat_recy)
        RecyclerView mHelpfatRecy;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
