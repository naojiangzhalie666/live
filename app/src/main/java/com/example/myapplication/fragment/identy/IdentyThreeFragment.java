package com.example.myapplication.fragment.identy;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.ui.IdentyActivity;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class IdentyThreeFragment extends Fragment {


    @BindView(R.id.denty_three_img)
    ImageView mDentyThreeImg;
    @BindView(R.id.denty_three_state)
    TextView mDentyThreeState;
    @BindView(R.id.denty_three_miaoshu)
    TextView mDentyThreeMiaoshu;
    @BindView(R.id.identy_next)
    Button mIdentyNext;
    @BindView(R.id.identy_cancel)
    Button mIdentyCancel;
    private Unbinder unbinder;
    private IdentyActivity mActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_identy_three, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (IdentyActivity) getActivity();
        init();
    }

    private void init() {
        mDentyThreeImg.setImageResource(R.drawable.denti_failed);
        mDentyThreeState.setText("验证失败");
        mIdentyNext.setText("重新认证");
        mIdentyCancel.setVisibility(View.VISIBLE);
        mDentyThreeState.setTextColor(getResources().getColor(R.color.red));
        mDentyThreeMiaoshu.setText("身份验证失败\n请重新验证");


    }

    @OnClick({R.id.identy_next, R.id.identy_cancel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.identy_next:
//                if (false) {//验证失败
                    mActivity.onReset();
//                } else {//验证成功
//                    mActivity.goNext();
//                }
                break;
            case R.id.identy_cancel:
                mActivity.finish();
                break;
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
