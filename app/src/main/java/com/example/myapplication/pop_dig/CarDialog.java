package com.example.myapplication.pop_dig;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.CarshowAdapter;
import com.example.myapplication.ui.OranizeActivity;
import com.example.myapplication.ui.ShowGoodsActivity;
import com.superc.yyfflibrary.utils.ToastUtil;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CarDialog extends Dialog {
    @BindView(R.id.dialog_car_head)
    ImageView mDialogCarHead;
    @BindView(R.id.dialog_car_pro)
    ProgressBar mDialogCarPro;
    @BindView(R.id.dialog_car_what)
    TextView mDialogCarWhat;
    @BindView(R.id.dialog_car_zhinan)
    TextView mDialogCarZhinan;
    @BindView(R.id.dialog_car_recy)
    RecyclerView mDialogCarRecy;
    @BindView(R.id.dialog_car_money)
    TextView mDialogCarMoney;
    @BindView(R.id.dialog_car_charge)
    TextView mDialogCarCharge;
    private Context mContext;
    private List<Map<String, Object>> mMapList;
    private CarshowAdapter mCarshowAdapter;


    public CarDialog(@NonNull Context context, List<Map<String, Object>> maps) {
        super(context);
        mContext = context;
        mMapList = maps;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_car);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        getWindow().setGravity(Gravity.BOTTOM);
        ButterKnife.bind(this);
        mCarshowAdapter = new CarshowAdapter(mContext, mMapList);
        LinearLayoutManager linea = new LinearLayoutManager(mContext);
        mDialogCarRecy.setLayoutManager(linea);
        mDialogCarRecy.setAdapter(mCarshowAdapter);
        mCarshowAdapter.setOnItemClickListener(new CarshowAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                mContext.startActivity(new Intent(mContext, ShowGoodsActivity.class));//商品套餐页面
            }

            @Override
            public void onTiyanClickListener(int pos) {
                ToastUtil.showToast(mContext, "进行购买" + pos);
            }
        });
        if (mMapList.size() > 5) {
            mDialogCarRecy.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 700));
        } else {
            mDialogCarRecy.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    @OnClick({R.id.dialog_car_zhinan, R.id.dialog_car_charge, R.id.dialog_car_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_car_zhinan:
                ToastUtil.showToast(mContext, "指南");
                break;
            case R.id.dialog_car_charge:
                mContext.startActivity(new Intent(mContext, BuyzActivity.class));
                break;
            case R.id.dialog_car_more:
                mContext.startActivity(new Intent(mContext, OranizeActivity.class));//咨询机构页面
                break;
        }
    }
}
