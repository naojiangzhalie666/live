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
public class FindWonderFragment extends Fragment {


    @BindView(R.id.wonder_img)
    ImageView mWonderImg;
    private Unbinder unbinder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_wonder, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.wonder_img)
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wonder_img:
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
