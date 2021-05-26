package com.tyxh.framlive.ui;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tyxh.framlive.R;
import com.tyxh.framlive.base.ApiService;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.bean.AssetBean;
import com.tyxh.framlive.bean.EventMessage;
import com.tyxh.framlive.pop_dig.GuideDialog;
import com.tyxh.framlive.utils.SoftKeyboardFixerForFullscreen;
import com.tyxh.framlive.utils.TitleUtils;
import com.tyxh.framlive.utils.keyboard.BaseKeyboard;
import com.tyxh.framlive.utils.keyboard.KeyboardManager;
import com.tyxh.framlive.utils.keyboard.NumberKeyboard;
import com.ljy.devring.DevRing;
import com.ljy.devring.http.support.observer.CommonObserver;
import com.ljy.devring.http.support.throwable.HttpThrowable;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ljy.devring.http.support.throwable.HttpThrowable.HTTP_ERROR;

public class WithdrawalActivity extends LiveBaseActivity {

    @BindView(R.id.withdra_edt)
    EditText mEditText;
    @BindView(R.id.withdrawal_nowmoney)
    TextView mWithdrawalNowmoney;
    @BindView(R.id.textView125)
    TextView mTv_twofive;
    @BindView(R.id.textView128)
    TextView mTv_twoEnght;
    @BindView(R.id.tx_imgv_wechat)
    ImageView mImgv_wechat;
    @BindView(R.id.tx_imgv_zfb)
    ImageView mImage_zfb;


    private KeyboardManager keyboardManagerNumber;
    private NumberKeyboard numberKeyboard;
    private double now_money = 0;
    private String now_mmey = "0";
    private String text_twofive = "2. 审核成功后资金将在3个工作日内尽快转账，审核通过后3个工作日未到账，可向平台工作人员   反馈>";
    private String text_twoeight = "5. 微信提现需确保打开相关功能，请看详情";
    private int select_what = 1;//  1  微信  2支付宝
    private GuideDialog mGuideDig_kf,mGuideDig_wechat;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_withdrawal;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true, this);
        ButterKnife.bind(this);
        mImgv_wechat.requestFocus();
        SoftKeyboardFixerForFullscreen.assistActivity(this);
        setTextContent();
        getMineAsset();
//        initKeyBoard();

    }

    @OnClick({R.id.imgv_back, R.id.withdrawal_alltix, R.id.tx_imgv_wechat, R.id.tx_tv_wechat, R.id.tx_imgv_zfb, R.id.tx_tv_zfb,R.id.button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgv_back:
                finish();
                break;
            case R.id.withdrawal_alltix:
                mEditText.setText(now_mmey + "");
                mEditText.setSelection(mEditText.getText().toString().length());
                break;
            case R.id.tx_imgv_wechat:
            case R.id.tx_tv_wechat:
                select_what = 1;
                mImage_zfb.setImageResource(R.drawable.tx_nse);
                mImgv_wechat.setImageResource(R.drawable.tx_se);
                break;
            case R.id.tx_imgv_zfb:
            case R.id.tx_tv_zfb:
                select_what = 2;
                mImage_zfb.setImageResource(R.drawable.tx_se);
                mImgv_wechat.setImageResource(R.drawable.tx_nse);
                break;
            case R.id.button:
                if(select_what ==1){
                    ToastShow("微信提现");
                }else{
                    ToastShow("支付宝提现");
                }
                break;
        }
    }
    private void getMineAsset(){
        DevRing.httpManager().commonRequest(DevRing.httpManager().getService(ApiService.class).getAsset(token, user_Info.getRetData().getId()), new CommonObserver<AssetBean>() {
            @Override
            public void onResult(AssetBean assetBean) {
                if(assetBean.getRetCode() == 0){
                    AssetBean.RetDataBean data = assetBean.getRetData();
                    now_money =Double.parseDouble(data.getBalance());
                    now_mmey=data.getBalance();
                    mWithdrawalNowmoney.setText("当前零钱余额" + now_mmey + "元,");
                }
            }

            @Override
            public void onError(HttpThrowable throwable) {
                if(throwable.errorType==HTTP_ERROR){//重新登录
                    EventBus.getDefault().post(new EventMessage(1005));
                }
                Log.e(TAG, "onError: " + throwable.toString());
            }
        }, TAG);
    }

    /*设置提现规则中的两个特殊文字*/
    private void setTextContent() {
        mGuideDig_kf =new GuideDialog(this,1);
        mGuideDig_wechat =new GuideDialog(this,2);
        /*第二个文字设置*/
        SpannableStringBuilder builder = new SpannableStringBuilder(text_twofive);
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
        builder.setSpan(styleSpan, text_twofive.length() - 3, text_twofive.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                mGuideDig_kf.show();
            }
            @Override
            public void updateDrawState(@NonNull TextPaint drawState) {
                super.updateDrawState(drawState);
                drawState.setColor(Color.parseColor("#0DACF6"));
                drawState.setUnderlineText(false);  drawState.clearShadowLayer();
            }
        };
        builder.setSpan(clickableSpan, text_twofive.length() - 3, text_twofive.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTv_twofive.setMovementMethod(LinkMovementMethod.getInstance());
        mTv_twofive.setHighlightColor(getResources().getColor(android.R.color.transparent));//不设置会有背景色
        mTv_twofive.setText(builder);
        /*第五个文字设置*/
        SpannableStringBuilder builder_eig = new SpannableStringBuilder(text_twoeight);
        StyleSpan styleSpan_eig = new StyleSpan(Typeface.BOLD);
        builder_eig.setSpan(styleSpan_eig, text_twoeight.length() - 4, text_twoeight.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ClickableSpan clickableSpan_eig = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                mGuideDig_wechat.show();
            }
            @Override
            public void updateDrawState(@NonNull TextPaint drawState) {
                super.updateDrawState(drawState);
                drawState.setColor(Color.parseColor("#0DACF6"));
                drawState.setUnderlineText(false);  drawState.clearShadowLayer();
            }
        };
        builder_eig.setSpan(clickableSpan_eig, text_twoeight.length() - 4, text_twoeight.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTv_twoEnght.setMovementMethod(LinkMovementMethod.getInstance());
        mTv_twoEnght.setHighlightColor(getResources().getColor(android.R.color.transparent));//不设置会有背景色
        mTv_twoEnght.setText(builder_eig);


    }


    private void initKeyBoard() {
        keyboardManagerNumber = new KeyboardManager(this);
        initNumberKeyboard();
        keyboardManagerNumber.bindToEditor(mEditText, numberKeyboard);
//        mEditText.requestFocus();
    }

    private void initNumberKeyboard() {
        numberKeyboard = new NumberKeyboard(this, NumberKeyboard.DEFAULT_NUMBER_XML_LAYOUT);
        numberKeyboard.setEnableDotInput(true);
        numberKeyboard.setActionDoneClickListener(new NumberKeyboard.ActionDoneClickListener() {
            @Override
            public void onActionDone(CharSequence charSequence) {
                if (TextUtils.isEmpty(charSequence) || charSequence.toString().equals("0") || charSequence.toString().equals("0.0")) {
                    Toast.makeText(WithdrawalActivity.this, "请输入提现金额", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(WithdrawalActivity.this, "提现" + charSequence, Toast.LENGTH_SHORT).show();
//                    onNumberkeyActionDone();
                }
            }
        });

        numberKeyboard.setKeyStyle(new BaseKeyboard.KeyStyle() {
            @Override
            public Drawable getKeyBackound(Keyboard.Key key) {
                if (key.iconPreview != null) {
                    return key.iconPreview;
                } else {
                    return ContextCompat.getDrawable(WithdrawalActivity.this, R.drawable.key_number_bg);
                }
            }

            @Override
            public Float getKeyTextSize(Keyboard.Key key) {
                if (key.codes[0] == WithdrawalActivity.this.getResources().getInteger(R.integer.action_done)) {
                    return convertSpToPixels(20f);
                }
                return convertSpToPixels(24f);
            }

            @Override
            public Integer getKeyTextColor(Keyboard.Key key) {
                if (key.codes[0] == getResources().getInteger(R.integer.action_done)) {
                    return Color.WHITE;
                }
                return null;
            }

            @Override
            public CharSequence getKeyLabel(Keyboard.Key key) {
                return null;
            }
        });
    }

    public float convertSpToPixels(float sp) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
        return px;
    }


}
