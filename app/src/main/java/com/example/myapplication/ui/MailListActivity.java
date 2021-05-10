package com.example.myapplication.ui;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.base.Constant;
import com.example.myapplication.base.LiveApplication;
import com.example.myapplication.base.LiveBaseActivity;
import com.example.myapplication.bean.AttentionBean;
import com.example.myapplication.chat.ChatActivity;
import com.example.myapplication.utils.LiveShareUtil;
import com.example.myapplication.utils.TitleUtils;
import com.example.myapplication.utils.httputil.HttpBackListener;
import com.example.myapplication.utils.httputil.LiveHttp;
import com.example.myapplication.utils.indelistview.LetterListView;
import com.example.myapplication.utils.indelistview.SortAdapter;
import com.google.gson.Gson;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MailListActivity extends LiveBaseActivity {

    @BindView(R.id.mail_list)
    ListView mMailList;
    @BindView(R.id.let_bar)
    LetterListView mLettView;
    @BindView(R.id.mail_txlone)
    TextView mMailTxlone;
    @BindView(R.id.mail_txltwo)
    TextView mMailTxltwo;
    @BindView(R.id.mail_txlthree)
    TextView mMailTxlthree;
    @BindView(R.id.mail_txlonee)
    TextView mMailTxlonee;
    @BindView(R.id.mail_txltwoo)
    TextView mMailTxltwoo;
    @BindView(R.id.mail_txlthreee)
    TextView mMailTxlthreee;
    @BindView(R.id.mail_tv_nodata)
    TextView mMailTvNodata;
    @BindView(R.id.mail_list_line)
    View mMailLine;
    private ArrayList<AttentionBean.RetDataBean.DatasBean> list_gzmine;

    private boolean is_user = false;
    private SortAdapter mAdapter;
    private int line_startDis = 0;
    private boolean is_first = true;
    private String mType = "0";


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_mail_list;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(false, this);
        ButterKnife.bind(this);
        if (is_user) {
            mMailTxltwo.setVisibility(View.GONE);
            mMailTxltwoo.setVisibility(View.GONE);
            mMailTxlonee.setText("我的关注");
            mMailTxlone.setText("我的关注");
        }


        initV();
        Intent intent = getIntent();
        if (intent != null) {
            String index = intent.getStringExtra("index");
            if (!TextUtils.isEmpty(index) && index.equals("end")) {
                mMailTxlthreee.performClick();
                mType = "2";
            } else {
                getGzmine();
            }
        } else {
            getGzmine();
        }
    }

    @OnClick({R.id.imgv_back, R.id.mail_txlone, R.id.mail_txltwo, R.id.mail_txlthree, R.id.mail_txlonee, R.id.mail_txltwoo, R.id.mail_txlthreee})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_back:
                finish();
                break;
            case R.id.mail_txlone:
            case R.id.mail_txlonee:
                mMailTxlonee.setVisibility(View.VISIBLE);
                mMailTxltwoo.setVisibility(is_user ? View.GONE : View.INVISIBLE);
                mMailTxlthreee.setVisibility(View.INVISIBLE);
                if (is_user) {
                    getWodGz();
                } else {
                    getGzmine();
                }
                toGoAnima(mMailTxlonee);
                break;
            case R.id.mail_txltwo:
            case R.id.mail_txltwoo:
                mMailTxlonee.setVisibility(View.INVISIBLE);
                mMailTxltwoo.setVisibility(View.VISIBLE);
                mMailTxlthreee.setVisibility(View.INVISIBLE);
                getWodGz();

                break;
            case R.id.mail_txlthree:
            case R.id.mail_txlthreee:
                mMailTxlonee.setVisibility(View.INVISIBLE);
                mMailTxltwoo.setVisibility(is_user ? View.GONE : View.INVISIBLE);
                mMailTxlthreee.setVisibility(View.VISIBLE);
                getZxsList();
                break;

        }
    }

    private void initV() {
        list_gzmine = new ArrayList<>();
        mAdapter = new SortAdapter(this, list_gzmine);
        mMailList.setAdapter(mAdapter);
        mLettView.setWordSelectedListener(new LetterListView.WordSelectedListener() {
            @Override
            public void word(String word, int index) {
                for (int i = 0; i < list_gzmine.size(); i++) {
                    if (word.equalsIgnoreCase(list_gzmine.get(i).getInitials())) {
                        mMailList.setSelection(i); // 选择到首字母出现的位置
                        return;
                    }
                }
            }
        });
        mMailList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                AttentionBean.RetDataBean.DatasBean datasBean = list_gzmine.get(pos);
                startChatActivity(datasBean.getAttId(),datasBean.getNickname());
            }
        });
    }
    private void startChatActivity(String id,String title) {
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(V2TIMConversation.V2TIM_C2C);
        chatInfo.setId(id);
        chatInfo.setChatName(title);
        Intent intent = new Intent(this,ChatActivity.class);
        intent.putExtra(Constant.CHAT_INFO, chatInfo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(intent);
    }

    /*关注我的--我的粉丝*/
    private void getGzmine() {
        list_gzmine.clear();
        mMailTvNodata.setVisibility(View.GONE);
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getAttentionMe(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken()), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                AttentionBean bean = new Gson().fromJson(result.toString(), AttentionBean.class);
                if (bean.getRetCode() == 0) {
                    List<AttentionBean.RetDataBean> retData = bean.getRetData();
                    toSetAttenData(retData);
                } else {
                    mMailTvNodata.setVisibility(View.VISIBLE);
                    list_gzmine.clear();
                    mAdapter.notifyDataSetChanged();
                    ToastShow(bean.getRetMsg());
                }

            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
        mAdapter.notifyDataSetChanged();
    }

    /*我的关注*/
    private void getWodGz() {
        toGoAnima(mMailTxltwoo);
        mMailTvNodata.setVisibility(View.GONE);
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getMyAttention(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken()), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                AttentionBean bean = new Gson().fromJson(result.toString(), AttentionBean.class);
                if (bean.getRetCode() == 0) {
                    List<AttentionBean.RetDataBean> retData = bean.getRetData();
                    toSetAttenData(retData);
                } else {
                    mMailTvNodata.setVisibility(View.VISIBLE);
                    list_gzmine.clear();
                    mAdapter.notifyDataSetChanged();
                    ToastShow(bean.getRetMsg());
                }

            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

    /*咨询师列表*/
    private void getZxsList() {
        toGoAnima(mMailTxlthreee);
        mMailTvNodata.setVisibility(View.GONE);



        list_gzmine.clear();
        mAdapter.notifyDataSetChanged();
        mMailTvNodata.setVisibility(View.VISIBLE);

    }

    private void toSetAttenData(List<AttentionBean.RetDataBean> dataBeanList) {
        list_gzmine.clear();
        List<AttentionBean.RetDataBean.DatasBean> datasBeans = new ArrayList<>();
        for (int i = 0; i < dataBeanList.size(); i++) {
            AttentionBean.RetDataBean retDataBean = dataBeanList.get(i);
            List<AttentionBean.RetDataBean.DatasBean> datas = retDataBean.getDatas();
            datasBeans.addAll(datas);
        }
        list_gzmine.addAll(datasBeans);
        mAdapter.notifyDataSetChanged();
        if(datasBeans.size() ==0){
            mMailTvNodata.setVisibility(View.VISIBLE);
        }
    }

    private void toGoAnima(TextView textView) {
        textView.setTextColor(getResources().getColor(R.color.setin_se));
        int left_line = mMailLine.getLeft() + mMailLine.getWidth() / 2;
        int left_tvview = textView.getLeft() + textView.getWidth() / 2;
        int go_distance = left_tvview - left_line;
        ObjectAnimator anim = ObjectAnimator.ofFloat(mMailLine, "translationX", line_startDis, go_distance);
        anim.setDuration(265);
        anim.start();
        line_startDis = go_distance;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (is_first) {
            switch (mType) {
                case "0":
                    toGoAnima(mMailTxlonee);
                    break;
                case "1":
                    toGoAnima(mMailTxltwoo);
                    break;
                case "2":
                    toGoAnima(mMailTxlthreee);
                    break;
            }
        }
        is_first = false;
    }

}

