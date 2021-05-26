package com.tyxh.framlive.ui;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.tyxh.framlive.R;
import com.tyxh.framlive.base.LiveApplication;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.bean.BaseBean;
import com.tyxh.framlive.bean.EventMessage;
import com.tyxh.framlive.bean.UserInfoBean;
import com.tyxh.framlive.fragment.NewFirstFragment;
import com.tyxh.framlive.fragment.NewLastFragment;
import com.tyxh.framlive.fragment.NewSecondFragment;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;
import com.google.gson.Gson;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.tyxh.framlive.base.Constant.LIVE_UPDATE_CODE;

public class MsgInputActivity extends LiveBaseActivity {

    @BindView(R.id.msginput_next)
    Button mMsginputNext;
    @BindView(R.id.msginput_num)
    TextView mMsginputNum;
    private NewFirstFragment mNewFirstFragment;
    private NewSecondFragment mNewSecondFragment;
    private NewLastFragment mNewLastFragment;
    private int now_num = 1;
    private int sex ;//1 男 2女
    private String old_nian = "";
    private String old_pos = "";
    private String msg_last = "";
    private String msg_lastId =  "";



    @Override
    public int getContentLayoutId() {
        return R.layout.activity_msg_input;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true, this);
        ButterKnife.bind(this);
        mNewFirstFragment = new NewFirstFragment();
        mNewSecondFragment = new NewSecondFragment();
        mNewLastFragment = new NewLastFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.msginput_fram,mNewFirstFragment).commit();
        getSupportFragmentManager().beginTransaction().show(mNewFirstFragment);


    }

    @OnClick(R.id.msginput_next)
    public void onClick() {
        if(now_num ==1){
            if(mNewFirstFragment.sex ==0){
                ToastShow("请选择性别");
                return;
            }
            mMsginputNum.setText("2/3");
            getSupportFragmentManager().beginTransaction().replace(R.id.msginput_fram,mNewSecondFragment).commit();
            getSupportFragmentManager().beginTransaction().show(mNewSecondFragment);
            sex = mNewFirstFragment.sex;
            now_num+=1;
        }else if(now_num ==2){
            if(TextUtils.isEmpty(mNewSecondFragment.old_nian)){
                ToastShow("请选择年龄");
                return;
            }
            mMsginputNum.setText("3/3");
            getSupportFragmentManager().beginTransaction().replace(R.id.msginput_fram,mNewLastFragment).commit();
            getSupportFragmentManager().beginTransaction().show(mNewLastFragment);
            old_nian =mNewSecondFragment.old_nian;
            old_pos = mNewSecondFragment.click_pos;
            mMsginputNext.setText("开启心灵之旅");
            now_num+=1;
        }else{
            if(TextUtils.isEmpty(mNewLastFragment.msg_last)){
                ToastShow("请至少选择一项");
                return;
            }
            msg_last = mNewLastFragment.msg_last;
            msg_lastId = mNewLastFragment.msg_lastId;
            Log.i(TAG, "onClick:sex "+ sex+" old_nian: "+old_nian+" msg_Last: "+msg_lastId);
            toUpdateMsg();
        }

    }

    /*put请求上传基本信息*/
    private void toUpdateMsg(){
        Map<String,Object> map =new HashMap<>();
        map.put("age",old_pos);
        map.put("gender",sex+"");
        map.put("ico","");
        map.put("id",LiveShareUtil.getInstance(this).get(LiveShareUtil.APP_USERID,""));
        map.put("interest",msg_lastId);
        String result =new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), result);
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().updateUserInfo(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken(),requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean baseBean =new Gson().fromJson(result.toString(),BaseBean.class);
                if(baseBean.getRetCode() ==0){
                    getUserInfo();
                }else{
                    ToastShow(baseBean.getRetMsg());
                }

            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

    /*获取用户信息*/
    private void getUserInfo() {
        EventBus.getDefault().post(new EventMessage(LIVE_UPDATE_CODE));
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getUserInfo(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken()), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                UserInfoBean userInfoBean = new Gson().fromJson(result.toString(), UserInfoBean.class);
                if (userInfoBean.getRetCode() == 0) {
                    LiveShareUtil.getInstance(MsgInputActivity.this).put("user", new Gson().toJson(userInfoBean));//保存用户信息
                }
                statActivity(MainActivity.class);
                finish();
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

}
