package com.example.myapplication.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.ui.ExchangeActivity;
import com.example.myapplication.ui.LiveDetailActivity;

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
        switch (view.getId()) {
            case R.id.fw_live:
                Intent live_intent =new Intent(getActivity(), LiveDetailActivity.class);
                startActivity(live_intent);
                break;
            case R.id.fw_speak:
                break;
            case R.id.fw_fuchi:
                break;
            case R.id.fw_shouyi:
                Intent intent = new Intent(getActivity(), ExchangeActivity.class);
                startActivity(intent);
                break;
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
