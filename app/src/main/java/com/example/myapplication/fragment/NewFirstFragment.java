package com.example.myapplication.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.myapplication.R;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewFirstFragment extends Fragment {
    public int sex;//1 男 2 女
    @BindView(R.id.newfirst_woman)
    ImageView mNewfirstWoman;
    @BindView(R.id.newfirst_man)
    ImageView mNewfirstMan;
    private Unbinder unbinder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_first, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.newfirst_woman, R.id.newfirst_man})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.newfirst_woman:
                mNewfirstWoman.setImageResource(R.drawable.woman_se);
                mNewfirstMan.setImageResource(R.drawable.man_unse);
                sex = 2;
                break;
            case R.id.newfirst_man:
                mNewfirstWoman.setImageResource(R.drawable.woman_unse);
                mNewfirstMan.setImageResource(R.drawable.man_se);
                sex = 1;
                break;
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
