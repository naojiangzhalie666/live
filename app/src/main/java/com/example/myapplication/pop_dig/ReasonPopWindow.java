package com.example.myapplication.pop_dig;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ReasonAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ReasonPopWindow extends PopupWindow {

    private View mV;
    private RecyclerView mreason_recy;
    private List<String> mStrings;
    private ReasonAdapter mReasonAdapter;
    private OnItemClick mOnItemClick;


    public ReasonPopWindow(Context context, int width, int height) {
        super(width, height);
        initViews(context);
        setContent();
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        mOnItemClick = onItemClick;
    }

    private void initViews(Context context) {
        mV = LayoutInflater.from(context).inflate(R.layout.pop_reason, null);
        mreason_recy = mV.findViewById(R.id.pop_reasion_recy);
        mStrings = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            mStrings.add("色情暴" + (i + 1));
        }
        mReasonAdapter = new ReasonAdapter(context, mStrings);
        LinearLayoutManager l_v = new LinearLayoutManager(context);
        mreason_recy.setLayoutManager(l_v);
        mreason_recy.setAdapter(mReasonAdapter);
        mReasonAdapter.setOnItemClickListener(new ReasonAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                String name = mStrings.get(pos);
                dismiss();
                if(mOnItemClick!=null)
                    mOnItemClick.onItemclick(name);
            }
        });


    }

    private void setContent() {
        setContentView(mV);
        setOutsideTouchable(true);

    }

    public interface OnItemClick{
        void onItemclick(String content);
    }


}
