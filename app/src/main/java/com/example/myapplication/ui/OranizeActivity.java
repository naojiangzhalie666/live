package com.example.myapplication.ui;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.PersonImageAdapter;
import com.example.myapplication.adapter.PersonTwoAdapter;
import com.example.myapplication.adapter.PersonjgImageAdapter;
import com.example.myapplication.bean.JgBean;
import com.example.myapplication.utils.TitleUtils;
import com.luck.picture.lib.entity.LocalMedia;
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

public class OranizeActivity extends BaseActivity {

    @BindView(R.id.personal_head)
    ImageView mPersonalHead;
    @BindView(R.id.personal_name)
    TextView mPersonalName;
    @BindView(R.id.personal_jigou)
    TextView mPersonalJigou;
    @BindView(R.id.personal_id)
    TextView mPersonalId;
    @BindView(R.id.personal_oneedt)
    ImageView mPersonalOneedt;
    @BindView(R.id.personal_onesure)
    TextView mPersonalOnesure;
    @BindView(R.id.personal_onerecy)
    RecyclerView mPersonalOnerecy;
    @BindView(R.id.showgoods_edt)
    ImageView mPersonalTwoedt;
    @BindView(R.id.showgoods_edtsure)
    TextView mPersonalTwosure;
    @BindView(R.id.personal_threeedt)
    ImageView mPersonalThreeedt;
    @BindView(R.id.personal_threesure)
    TextView mPersonalThreesure;
    @BindView(R.id.personal_tworecy)
    RecyclerView mPersonalTworecy;
    @BindView(R.id.personal_geren)
    EditText mPersonalGeren;
    @BindView(R.id.personal_fouredt)
    ImageView mPersonalFouredt;
    @BindView(R.id.personal_foursure)
    TextView mPersonalFoursure;
    @BindView(R.id.personal_thiredrecy)
    RecyclerView mPersonalThiredrecy;
    @BindView(R.id.personal_fiveedt)
    ImageView mPersonalFiveedt;
    @BindView(R.id.personal_fivesure)
    TextView mPersonalFivesure;
    @BindView(R.id.personal_photorecy)
    RecyclerView mPersonalPhotorecy;

    private boolean is_user  =false;
    private List<JgBean> mLocalMedias;
    private List<LocalMedia>     mLocalMedias_bt;
    private PersonjgImageAdapter mPersonImageAdapter;
    private PersonImageAdapter mPersonImageAdapter_bt;

    private List<Map<String, Object>> mStr_listtwos;
    private List<Map<String, Object>> mStr_noSelects;
    private PersonTwoAdapter mPersonTwoAdapter;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_oranize;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true, this);
        ButterKnife.bind(this);
        mPersonalName.setText("角色经历多空");
        mPersonalJigou.setText("送到家里的思考");
        mPersonalId.setText("ID:213412");
        mPersonalGeren.setText("都回家啦会计岗位nowie投了几个啦圣诞节；啊");
        mPersonalGeren.setEnabled(false);
        if(is_user){
            mPersonalOneedt.setVisibility(View.GONE);
            mPersonalTwoedt.setVisibility(View.GONE);
            mPersonalThreeedt.setVisibility(View.GONE);
            mPersonalFouredt.setVisibility(View.GONE);
            mPersonalFiveedt.setVisibility(View.GONE);
        }
        /*第一个横向咨询师列表*/
        mLocalMedias = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            JgBean jgBean = new JgBean();
            jgBean.setName("");
            mLocalMedias.add(jgBean);
        }
        mPersonImageAdapter = new PersonjgImageAdapter(this, null);
        mPersonImageAdapter.setCan_caozuo(false);
        mPersonImageAdapter.setShow_add(false);
        mPersonImageAdapter.setShow_zdy(true);
        mPersonImageAdapter.setList(mLocalMedias);
        mPersonImageAdapter.setHasStableIds(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        mPersonalTworecy.setLayoutManager(linearLayoutManager);
        mPersonalTworecy.setAdapter(mPersonImageAdapter);
        mPersonImageAdapter.setOnEdtClickListener(new PersonjgImageAdapter.OnEdtClickListener() {
            @Override
            public void onEdtCLickListener(int pos, String content) {
                mLocalMedias.get(pos).setName(content);
            }
        });
        /*第二个竖向列表*/
        LinearLayoutManager linea_three = new LinearLayoutManager(this);
        mPersonalThiredrecy.setLayoutManager(linea_three);
        mStr_listtwos = new ArrayList<>();
        mStr_noSelects = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<>();
            if (i > 5) {
                map.put("select", false);
                mStr_noSelects.add(map);
            } else {
                map.put("select", true);
                mStr_listtwos.add(map);
            }
        }
        mPersonTwoAdapter = new PersonTwoAdapter(this, mStr_listtwos, is_user);
        mPersonalThiredrecy.setAdapter(mPersonTwoAdapter);
        mPersonTwoAdapter.setOnItemClickListener(new PersonTwoAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                if (is_user) {
                    ToastShow("进行询问");
                } else if (mPersonalFoursure.getVisibility() == View.VISIBLE) {
                    Map<String, Object> map = mStr_listtwos.get(pos);
                    boolean sel = (boolean) map.get("select");
                    map.put("select", !sel);
                    mPersonTwoAdapter.notifyItemChanged(pos);
                }
            }
        });
        /*第三个竖向列表*/
        mLocalMedias_bt = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            mLocalMedias_bt.add(new LocalMedia());
        }
        mPersonImageAdapter_bt = new PersonImageAdapter(this, null);
        mPersonImageAdapter_bt.setCan_caozuo(false);
        mPersonImageAdapter_bt.setShow_add(false);
        mPersonImageAdapter_bt.setShow_zdy(true);
        mPersonImageAdapter_bt.setList(mLocalMedias_bt);
        LinearLayoutManager linear_bt = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        mPersonalPhotorecy.setLayoutManager(linear_bt);
        mPersonalPhotorecy.setAdapter(mPersonImageAdapter_bt);


    }

    @OnClick({R.id.personal_back, R.id.personal_share, R.id.personal_oneedt, R.id.personal_onesure, R.id.showgoods_edt, R.id.showgoods_edtsure, R.id.personal_threeedt, R.id.personal_threesure, R.id.personal_fouredt, R.id.personal_foursure, R.id.personal_fiveedt, R.id.personal_fivesure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.personal_back:
                finish();
                break;
            case R.id.personal_share:
                ToastShow("进行分享");
                break;
            case R.id.personal_oneedt:
                mPersonalOnesure.setVisibility(View.VISIBLE);
                mPersonalOneedt.setVisibility(View.GONE);
                ToastShow("个人可编辑");
                break;
            case R.id.personal_onesure:
                mPersonalOnesure.setVisibility(View.GONE);
                mPersonalOneedt.setVisibility(View.VISIBLE);
                ToastShow("个人不可编辑");
                break;
            case R.id.showgoods_edt:
                mPersonalTwosure.setVisibility(View.VISIBLE);
                mPersonalTwoedt.setVisibility(View.GONE);
                ToastShow("相册可编辑");
                mPersonImageAdapter.setShow_add(true);
                mPersonImageAdapter.setCan_caozuo(true);
                break;
            case R.id.showgoods_edtsure:
                mPersonalTwosure.setVisibility(View.GONE);
                mPersonalTwoedt.setVisibility(View.VISIBLE);
                ToastShow("相册不可编辑");
                mPersonImageAdapter.setShow_add(false);
                mPersonImageAdapter.setCan_caozuo(false);
                break;
            case R.id.personal_threeedt:
                mPersonalThreesure.setVisibility(View.VISIBLE);
                mPersonalThreeedt.setVisibility(View.GONE);
                ToastShow("简介可编辑");
                mPersonalGeren.requestFocus();
                mPersonalGeren.setEnabled(true);
                mPersonalGeren.setSelection(mPersonalGeren.getText().toString().length());
                break;
            case R.id.personal_threesure:
                mPersonalThreesure.setVisibility(View.GONE);
                mPersonalThreeedt.setVisibility(View.VISIBLE);
                ToastShow("简介不可编辑");
                mPersonalGeren.setEnabled(false);
                break;
            case R.id.personal_fouredt:
                mPersonalFoursure.setVisibility(View.VISIBLE);
                mPersonalFouredt.setVisibility(View.GONE);
                ToastShow("服务项目可编辑");
                mStr_listtwos.addAll(mStr_noSelects);
                mPersonTwoAdapter.setIs_edt(true);
                mPersonTwoAdapter.notifyDataSetChanged();
                break;
            case R.id.personal_foursure:
                mPersonalFoursure.setVisibility(View.GONE);
                mPersonalFouredt.setVisibility(View.VISIBLE);
                ToastShow("服务项目不可编辑");
                toGetFuwu();
                break;
            case R.id.personal_fiveedt:
                mPersonalFivesure.setVisibility(View.VISIBLE);
                mPersonalFiveedt.setVisibility(View.GONE);
                ToastShow("相册可编辑");
                mPersonImageAdapter_bt.setShow_add(true);
                mPersonImageAdapter_bt.setCan_caozuo(true);
                break;
            case R.id.personal_fivesure:
                mPersonalFivesure.setVisibility(View.GONE);
                mPersonalFiveedt.setVisibility(View.VISIBLE);
                ToastShow("相册不可编辑");
                mPersonImageAdapter_bt.setShow_add(false);
                mPersonImageAdapter_bt.setCan_caozuo(false);
                break;
        }
    }

    private void toGetFuwu(){
        List<Map<String,Object>> maps =new ArrayList<>();
        mStr_noSelects.clear();
        for (int i = 0; i < mStr_listtwos.size(); i++) {
            boolean select = (boolean) mStr_listtwos.get(i).get("select");
            if(select){
                maps.add(mStr_listtwos.get(i));
            }else{
                mStr_noSelects.add(mStr_listtwos.get(i));
            }
        }
        mStr_listtwos.clear();
        mStr_listtwos.addAll(maps);
        mPersonTwoAdapter.setIs_edt(false);
        mPersonTwoAdapter.notifyDataSetChanged();
    }
}
