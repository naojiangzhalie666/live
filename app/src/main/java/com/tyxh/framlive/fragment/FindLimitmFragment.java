package com.tyxh.framlive.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tyxh.framlive.R;
import com.tyxh.framlive.pop_dig.BuyzActivity;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindLimitmFragment extends Fragment {


    @BindView(R.id.finlimit_imgv)
    ImageView mFinlimitImgv;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_limitm, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.finlimit_imgv)
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.finlimit_imgv:
                startActivity(new Intent(getActivity(), BuyzActivity.class));
                break;


        }

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
