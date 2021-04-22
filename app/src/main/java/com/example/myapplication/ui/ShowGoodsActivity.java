package com.example.myapplication.ui;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ShowGoodsAdapter;
import com.example.myapplication.utils.TitleUtils;
import com.superc.yyfflibrary.base.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShowGoodsActivity extends BaseActivity {

    @BindView(R.id.personal_head)
    ImageView mPersonalHead;
    @BindView(R.id.showgoods_recy)
    RecyclerView mShowgoodsRecy;
    @BindView(R.id.showgoods_edt)
    ImageView mShowgoodsEdt;
    @BindView(R.id.showgoods_edtsure)
    TextView mShowgoodsEdtsure;
    @BindView(R.id.showgoods_shanchang)
    EditText mShowgoodsShanchang;
    @BindView(R.id.showgoods_zhendui)
    EditText mShowgoodsZhendui;
    @BindView(R.id.showgoods_service)
    TextView mShowgoodsService;
    @BindView(R.id.showgoods_note)
    TextView mShowgoodsNote;
    @BindView(R.id.showgoods_content)
    TextView mShowgoodsContent;
    @BindView(R.id.showgoods_nametv)
    TextView mShowgoodsNametv;
    @BindView(R.id.showgoods_name)
    TextView mShowgoodsName;
    @BindView(R.id.showgoods_title)
    TextView mShowgoodsTitle;
    @BindView(R.id.showgoods_zuanshi)
    TextView mShowgoodsZuanshi;
    @BindView(R.id.showgoods_buy)
    TextView mShowgoodsBuy;
    @BindView(R.id.showgoods_re)
    RelativeLayout mShowgoodsRe;

    private List<Map<String, Object>> mList_goods;
    private ShowGoodsAdapter mShowGoodsAdapter;
    private boolean is_user = false;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_show_goods;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(false, this);
        ButterKnife.bind(this);
        mShowgoodsNametv.setText("咨询机构。-");
        mShowgoodsName.setText("六年级");
        mShowgoodsTitle.setText("专属时间");
        mShowgoodsContent.setText("套餐内容。给睡觉了跟睡觉的你手机的各位哦ing");
        mShowgoodsShanchang.setText("进来看干嘛问哦ims过的人感受到快乐");
        mShowgoodsZhendui.setText("那个外哦没刚额我U按我那个的软件");
        mShowgoodsService.setText("你刚看见我俄爱莫噶诶我");
        mShowgoodsNote.setText("年末未噶美女速度挂机饿哦挖个；你是个哈看你空间撒大哥");
        mShowgoodsZuanshi.setText("50(还差550)");
        mShowgoodsBuy.setText("充值支付");
        if(is_user){
            mShowgoodsRe.setVisibility(View.VISIBLE);
            mShowgoodsEdt.setVisibility(View.GONE);
        }else{
            mShowgoodsRe.setVisibility(View.GONE);
            mShowgoodsEdt.setVisibility(View.VISIBLE);
        }
        mList_goods = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("select", i == 0 ? true : false);
            mList_goods.add(map);
        }
        mShowGoodsAdapter = new ShowGoodsAdapter(this, mList_goods, is_user);
        LinearLayoutManager linea = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        mShowgoodsRecy.setLayoutManager(linea);
        mShowgoodsRecy.setAdapter(mShowGoodsAdapter);
        mShowGoodsAdapter.setOnItemClickListener(new ShowGoodsAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                for (int i = 0; i < mList_goods.size(); i++) {
                    if (pos == i) {
                        mList_goods.get(i).put("select", true);
                    } else {
                        mList_goods.get(i).put("select", false);
                    }
                }
                mShowGoodsAdapter.notifyDataSetChanged();
                ToastShow("更换数据为"+pos);
            }
        });


    }

    @OnClick({R.id.personal_back, R.id.personal_share, R.id.showgoods_edt, R.id.showgoods_edtsure, R.id.showgoods_buy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.personal_back:
                finish();
                break;
            case R.id.personal_share:
                ToastShow("进行分享");
                break;
            case R.id.showgoods_edt:
                mShowgoodsEdtsure.setVisibility(View.VISIBLE);
                mShowgoodsEdt.setVisibility(View.GONE);
                mShowgoodsShanchang.setEnabled(true);
                mShowgoodsZhendui.setEnabled(true);
                break;
            case R.id.showgoods_edtsure:
                mShowgoodsEdtsure.setVisibility(View.GONE);
                mShowgoodsEdt.setVisibility(View.VISIBLE);
                mShowgoodsShanchang.setEnabled(false);
                mShowgoodsZhendui.setEnabled(false);
                break;
            case R.id.showgoods_buy:

                break;
        }
    }
}
