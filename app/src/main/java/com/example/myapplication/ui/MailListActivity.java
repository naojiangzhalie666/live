package com.example.myapplication.ui;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.base.LiveBaseActivity;
import com.example.myapplication.utils.TitleUtils;
import com.example.myapplication.utils.indelistview.LetterListView;
import com.example.myapplication.utils.indelistview.SortAdapter;
import com.example.myapplication.utils.indelistview.User;

import java.util.ArrayList;
import java.util.Collections;

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
    @BindView(R.id.mail_list_line)
    View mMailLine;
    private ArrayList<User> list_gzmine;

    private boolean is_user = false;
    private SortAdapter mAdapter;
    private int line_startDis = 0;
    private boolean is_first = true;
    private String mType="0";


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
        if(intent!=null){
            String index = intent.getStringExtra("index");
            if(!TextUtils.isEmpty(index)&&index.equals("end")){
                mMailTxlthreee.performClick();
                mType = "2";
            }else {
                getGzmine();
            }
        }else{
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
                    if (word.equalsIgnoreCase(list_gzmine.get(i).getFirstLetter())) {
                        mMailList.setSelection(i); // 选择到首字母出现的位置
                        return;
                    }
                }
            }
        });
    }

    private void getGzmine() {
        mMailList.setSelection(0);
        list_gzmine.clear();
        list_gzmine.add(new User("大娃"));
        list_gzmine.add(new User("二娃"));
        list_gzmine.add(new User("三娃"));
        list_gzmine.add(new User("四娃"));
        list_gzmine.add(new User("五娃"));
        list_gzmine.add(new User("六娃"));
        list_gzmine.add(new User("七娃"));
        list_gzmine.add(new User("喜羊羊"));
        list_gzmine.add(new User("美羊羊"));
        list_gzmine.add(new User("懒羊羊"));
        list_gzmine.add(new User("沸羊羊"));
        list_gzmine.add(new User("暖羊羊"));
        Collections.sort(list_gzmine); // 对list进行排序，需要让User实现Comparable接口重写compareTo方法
        mAdapter.notifyDataSetChanged();
    }

    private void getWodGz() {
        toGoAnima(mMailTxltwoo);
        mMailList.setSelection(0);
        list_gzmine.clear();
        list_gzmine.add(new User("懒羊羊"));
        list_gzmine.add(new User("沸羊羊"));
        list_gzmine.add(new User("暖羊羊"));
        list_gzmine.add(new User("慢羊羊"));
        list_gzmine.add(new User("灰太狼"));
        list_gzmine.add(new User("红太狼"));
        list_gzmine.add(new User("孙悟空"));
        list_gzmine.add(new User("黑猫警长"));
        list_gzmine.add(new User("舒克"));
        list_gzmine.add(new User("贝塔"));
        list_gzmine.add(new User("海尔"));
        list_gzmine.add(new User("阿凡提"));
        list_gzmine.add(new User("邋遢大王"));
        list_gzmine.add(new User("哪吒"));
        list_gzmine.add(new User("没头脑"));
        list_gzmine.add(new User("不高兴"));
        list_gzmine.add(new User("蓝皮鼠"));
        list_gzmine.add(new User("大脸猫"));
        list_gzmine.add(new User("大头儿子"));
        list_gzmine.add(new User("小头爸爸"));
        Collections.sort(list_gzmine); // 对list进行排序，需要让User实现Comparable接口重写compareTo方法
        mAdapter.notifyDataSetChanged();
    }

    private void getZxsList() {
        toGoAnima(mMailTxlthreee);
        mMailList.setSelection(0);
        list_gzmine.clear();
        list_gzmine.add(new User("小头爸爸"));
        list_gzmine.add(new User("蓝猫"));
        list_gzmine.add(new User("淘气"));
        list_gzmine.add(new User("叶峰"));
        list_gzmine.add(new User("楚天歌"));
        list_gzmine.add(new User("江流儿"));
        list_gzmine.add(new User("Tom"));
        list_gzmine.add(new User("Jerry"));
        list_gzmine.add(new User("12345"));
        list_gzmine.add(new User("54321"));
        list_gzmine.add(new User("_(:з」∠)_"));
        list_gzmine.add(new User("……%￥#￥%#"));
        Collections.sort(list_gzmine); // 对list进行排序，需要让User实现Comparable接口重写compareTo方法
        mAdapter.notifyDataSetChanged();

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

