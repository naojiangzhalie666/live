package com.example.myapplication.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.VpAdapter;
import com.example.myapplication.pop_dig.CodeDialog;
import com.example.myapplication.pop_dig.LogoutDialog;
import com.example.myapplication.ui.AdviceActivity;
import com.example.myapplication.ui.FollowActivity;
import com.example.myapplication.ui.HelpabackActivity;
import com.example.myapplication.ui.MybackpActivity;
import com.example.myapplication.ui.NormalActivity;
import com.example.myapplication.ui.SetActivity;
import com.example.myapplication.ui.SetInActivity;
import com.superc.yyfflibrary.utils.ShareUtil;
import com.superc.yyfflibrary.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment {

    @BindView(R.id.mine_zuanshi)
    TextView mMineZuanshi;
    @BindView(R.id.mine_cons_eyezuanshi)
    ImageView mMineConsEyezuanshi;
    @BindView(R.id.mine_cons_zuanshi)
    ConstraintLayout mMineConsZuanshi;
    @BindView(R.id.mine_money_tv)
    TextView mMineMoneyTv;
    @BindView(R.id.mine_cons_eyemoney)
    ImageView mMineConsEyemoney;
    @BindView(R.id.mine_cons_money)
    ConstraintLayout mMineConsMoney;
    @BindView(R.id.mine_title)
    TextView mMineTitle;
    @BindView(R.id.mine_id)
    TextView mMineId;
    @BindView(R.id.mine_level)
    TextView mMineLevel;
    @BindView(R.id.mine_daojutv)
    TextView mMineDaojutv;
    @BindView(R.id.mine_cons_daoju)
    ConstraintLayout mMineConsDaoju;
    @BindView(R.id.constraintLayout2)
    ConstraintLayout mConstraintLayout2;
    @BindView(R.id.mine_head)
    ImageView mMineHead;
    @BindView(R.id.mine_vp)
    ViewPager mMineVp;
    @BindView(R.id.mine_edtline)
    View mMineEdtline;
    @BindView(R.id.mine_edt)
    TextView mMineEdt;
    @BindView(R.id.mine_ruzhu)
    RelativeLayout mMineRuzhu;
    @BindView(R.id.mine_logout)
    Button mMineLogout;
    private Unbinder unbinder;
    private boolean show_zuanshi = true, show_money = true;
    private XfFragment mXfFragment;
    private FwFragment mFwFragment;
    private List<Fragment> mFragments;
    private int mPower;
    private CodeDialog mCodeDialog;
    private LogoutDialog mLogoutDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @OnClick({R.id.mine_set, R.id.mine_cons_eyezuanshi, R.id.mine_cons_eyemoney, R.id.mine_guanzhu, R.id.mine_guanzhugzongh, R.id.mine_help,
            R.id.mine_edt, R.id.mine_ruzhu, R.id.mine_logout, R.id.mine_money_tv, R.id.textView16, R.id.mine_zuanshi, R.id.textView14,
            R.id.mine_daojutv, R.id.textView17})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mine_set:
                startActivity(new Intent(getActivity(), SetActivity.class));
                break;
            case R.id.mine_daojutv:
            case R.id.textView17:
                startActivity(new Intent(getActivity(), MybackpActivity.class));
                break;
            case R.id.mine_cons_eyezuanshi:
                if (show_zuanshi) {
                    mMineConsEyezuanshi.setImageResource(R.drawable.mine_bi);
                    mMineZuanshi.setText("****");
                } else {
                    mMineConsEyezuanshi.setImageResource(R.drawable.mine_kai);
                    mMineZuanshi.setText("1244");
                }
                show_zuanshi = !show_zuanshi;
                break;
            case R.id.mine_cons_eyemoney:
                if (show_money) {
                    mMineConsEyemoney.setImageResource(R.drawable.mine_bi);
                    mMineMoneyTv.setText("****");
                } else {
                    mMineConsEyemoney.setImageResource(R.drawable.mine_kai);
                    mMineMoneyTv.setText("1244");
                }
                show_money = !show_money;
                break;
            case R.id.mine_guanzhu:
                startActivity(new Intent(getActivity(), FollowActivity.class));
                break;
            case R.id.mine_guanzhugzongh:
                mCodeDialog.show();
                ToastUtil.showToast(getActivity(), "公众号关注");
                break;
            case R.id.mine_help:
                startActivity(new Intent(getActivity(), HelpabackActivity.class));
                break;
            case R.id.mine_edt:
                ToastUtil.showToast(getActivity(), "修改个人");
                break;
            case R.id.mine_ruzhu:
                startActivity(new Intent(getActivity(), SetInActivity.class));
                break;
            case R.id.mine_logout:
                mLogoutDialog.show();
                break;
            case R.id.mine_zuanshi:
            case R.id.textView14:
                if (mPower == 0) {//普通
                    startActivity(new Intent(getActivity(), NormalActivity.class));
                } else {//咨询师/机构
                    startActivity(new Intent(getActivity(), AdviceActivity.class));
                }
                break;
            case R.id.mine_money_tv:
            case R.id.textView16:
                startActivity(new Intent(getActivity(), AdviceActivity.class));
                break;
        }
    }

    private void init() {
        mLogoutDialog = new LogoutDialog(getActivity());
        mCodeDialog = new CodeDialog(getActivity());
        mFragments = new ArrayList<>();
        mXfFragment = new XfFragment();
        mPower = (int) ShareUtil.getInstance(getActivity()).get("power", 0);
        if (mPower == 0) {//普通
            mMineEdt.setVisibility(View.GONE);
            mMineEdtline.setVisibility(View.GONE);
            mFragments.add(mXfFragment);
            mMineConsMoney.setVisibility(View.GONE);
        } else {//咨询师
            mFwFragment = new FwFragment();
            mMineRuzhu.setVisibility(View.GONE);
            mMineConsMoney.setVisibility(View.VISIBLE);
            mFragments.add(mXfFragment);
            mFragments.add(mFwFragment);
        }
        mMineVp.setAdapter(new VpAdapter(getFragmentManager(), 1, mFragments));

        /*mMineSmart.setEnablePureScrollMode(true);//是否启用纯滚动模式
        mMineSmart.setEnableNestedScroll(true);//是否启用嵌套滚动;
        mMineSmart.setEnableOverScrollDrag(true);//是否启用越界拖动（仿苹果效果）1.0.4
        mMineSmart.setEnableOverScrollBounce(true);//是否启用越界回弹*/

        mMineZuanshi.setText("334");
        mMineMoneyTv.setText("2334");
        mMineDaojutv.setText("33");
        mMineTitle.setText("谁告诉你空间");
        mMineId.setText("ID 12233");
        mMineLevel.setText("lv 3");
        mMineHead.setImageResource(R.drawable.man_se);

        mLogoutDialog.setOnLogoutClickListener(new LogoutDialog.OnLogoutClickListener() {
            @Override
            public void onLogoutClickListener() {
                getActivity().finish();
            }
        });


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
