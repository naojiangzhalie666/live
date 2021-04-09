package com.example.myapplication.pop_dig;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.BuyAdapter;
import com.superc.yyfflibrary.utils.ToastUtil;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Constraints;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BuyzDialog extends Dialog {
    @BindView(R.id.dig_buy_lastzuan)
    TextView mDigBuyLastzuan;
    @BindView(R.id.dig_buy_recy)
    RecyclerView mDigBuyRecy;
    private Context mContext;
    private List<Map<String, Object>> mMapList;
    private BuyAdapter mBuyAdapter;
    private String select="";

    public BuyzDialog(@NonNull Context context, List<Map<String, Object>> mpList) {
        super(context);
        mContext = context;
        mMapList = mpList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_buyzuan);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(Constraints.LayoutParams.MATCH_PARENT, Constraints.LayoutParams.WRAP_CONTENT);
        mDigBuyLastzuan.setText("1000000");

        mBuyAdapter = new BuyAdapter(mContext, mMapList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        mDigBuyRecy.setLayoutManager(gridLayoutManager);
        mDigBuyRecy.setAdapter(mBuyAdapter);

        mBuyAdapter.setOnItemClickListener(new BuyAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
             /*   for (int i = 0; i < mMapList.size(); i++) {
                    if(i==pos){
                        mMapList.get(i).put("select",true);
                    }else{
                        mMapList.get(i).put("select",false);
                    }
                }
                mBuyAdapter.notifyDataSetChanged();*/
                select = "充值"+pos;
            }

            @Override
            public void onLookMoreListener() {
                select="";
                mBuyAdapter.setLook_more(true);
            }
        });
    }

    @Override
    public void show() {
        super.show();
        select="";
        mBuyAdapter.setLook_more(false);
    }

    @OnClick({R.id.dig_buy_xieyi, R.id.dig_buy_buy, R.id.dig_buy_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dig_buy_xieyi:
                ToastUtil.showToast(mContext, "弹出协议");
                break;
            case R.id.dig_buy_buy:
                ToastUtil.showToast(mContext, "充值"+select);
                break;
            case R.id.dig_buy_cancel:
                dismiss();
                break;
        }
    }
}
