package com.tyxh.framlive.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.superc.yyfflibrary.utils.ToastUtil;
import com.tyxh.framlive.R;
import com.tyxh.framlive.base.LiveApplication;
import com.tyxh.framlive.bean.ActBackBean;
import com.tyxh.framlive.pop_dig.BuyzActivity;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;

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
    private String mToken;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_limitm, container, false);
        unbinder = ButterKnife.bind(this, view);
        mToken = LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken();
        return view;
    }

    @OnClick(R.id.finlimit_imgv)
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.finlimit_imgv:
                getJoinAct();
                break;
        }

    }

    private void getJoinAct(){
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().queryJoAct(mToken, "2"), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                ActBackBean backBean =new Gson().fromJson(result.toString(),ActBackBean.class);
                if(backBean.getRetCode() ==0 ){
                    //首充【0:不可；1:可购】
                    if(backBean.getRetData().getFirstCharge().equals("1")) {
                        startActivity(new Intent(getActivity(), BuyzActivity.class));
                    }else{
                        ToastUtil.showToast(getActivity(),"您已购买过，无法重复购买");
                    }
                }else{
                    ToastUtil.showToast(getActivity(),backBean.getRetMsg());
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
