package com.example.myapplication.base;

import android.os.Bundle;

import com.example.myapplication.pop_dig.LoadDialog;
import com.superc.yyfflibrary.utils.ToastUtil;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LiveBaseFragment extends Fragment {
    public LoadDialog mLoadDialog;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLoadDialog = new LoadDialog(getActivity());
    }

    public void showLoad(){
        if(mLoadDialog!=null)
            mLoadDialog.show();
    }
    public void hideLoad(){
        if(mLoadDialog!=null)
            mLoadDialog.dismiss();
    }
    /**
     * @param msg 弹出提示类型--只支持String和Int索引
     */
    public void ToastShow(Object msg) {
        if (msg instanceof String) {
            ToastUtil.showToast(getActivity(), (String) msg);
        } else {
            ToastUtil.showToast(getActivity(), (int) msg);
        }
    }


}
