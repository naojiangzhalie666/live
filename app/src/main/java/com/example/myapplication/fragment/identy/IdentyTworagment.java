package com.example.myapplication.fragment.identy;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
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
public class IdentyTworagment extends Fragment {


    @BindView(R.id.identytwo_bankname)
    TextView mIdentytwoBankname;
    @BindView(R.id.identytwo_kanum)
    EditText mIdentytwoKanum;
    @BindView(R.id.identytwo_phone)
    EditText mIdentytwoPhone;
    @BindView(R.id.identytwo_bankimg)
    ImageView mIdentytwoBankimg;
    @BindView(R.id.identytwo_yzm)
    EditText mIdentytwoYzm;
    @BindView(R.id.identytwo_getyzm)
    TextView mIdentytwoGetyzm;
    @BindView(R.id.identytwo_cb)
    CheckBox mIdentytwoCb;
    private Unbinder unbinder;
    private IdentyActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_identy_tworagment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (IdentyActivity) getActivity();
    }

    @OnClick({R.id.identytwo_bankname, R.id.identytwo_getyzm, R.id.identytwo_xieyi,R.id.identy_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.identytwo_bankname:
                break;
            case R.id.identytwo_getyzm:
                break;
            case R.id.identytwo_xieyi:
                break;
            case R.id.identy_next:
                mActivity.goNext();
                break;
        }
    }

    public String getResult() {
        String result = "";
        result+=mIdentytwoKanum.getText().toString();
        result+=mIdentytwoBankname.getText().toString();
        result+=mIdentytwoPhone.getText().toString();
        result+=mIdentytwoYzm.getText().toString();

        return result;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
