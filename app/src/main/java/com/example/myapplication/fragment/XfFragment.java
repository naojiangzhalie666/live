package com.example.myapplication.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.ui.OrderListActivity;

import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class XfFragment extends Fragment {


    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_xf, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.xf_cz, R.id.xf_sj, R.id.xf_pingjia, R.id.xf_level})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.xf_cz:
                OrderListActivity.GoMe(getActivity(), "0");
                break;
            case R.id.xf_sj:
                OrderListActivity.GoMe(getActivity(), "1");
                break;
            case R.id.xf_pingjia:
                OrderListActivity.GoMe(getActivity(), "2");
                break;
            case R.id.xf_level:
                OrderListActivity.GoMe(getActivity(), "3");
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}