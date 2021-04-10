package com.example.myapplication.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.ui.ConnectRecordActivity;
import com.example.myapplication.ui.ExchangeActivity;
import com.example.myapplication.ui.LiveDetailActivity;
import com.example.myapplication.ui.OffsupportActivity;

import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class FwFragment extends Fragment {


    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fw, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.fw_live, R.id.fw_speak, R.id.fw_fuchi, R.id.fw_shouyi})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.fw_live:
                intent = new Intent(getActivity(), LiveDetailActivity.class);
                break;
            case R.id.fw_speak:
                intent = new Intent(getActivity(), ConnectRecordActivity.class);
                break;
            case R.id.fw_fuchi:
                intent = new Intent(getActivity(), OffsupportActivity.class);
                break;
            case R.id.fw_shouyi:
                intent = new Intent(getActivity(), ExchangeActivity.class);
                break;
        }
        startActivity(intent);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
