package com.tyxh.framlive.ui;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import com.google.gson.Gson;
import com.ljy.devring.DevRing;
import com.ljy.devring.http.support.observer.CommonObserver;
import com.ljy.devring.http.support.throwable.HttpThrowable;
import com.tyxh.framlive.R;
import com.tyxh.framlive.base.ApiService;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.bean.AssetBean;
import com.tyxh.framlive.bean.BaseBean;
import com.tyxh.framlive.bean.EventMessage;
import com.tyxh.framlive.pop_dig.GuideDialog;
import com.tyxh.framlive.utils.SoftKeyboardFixerForFullscreen;
import com.tyxh.framlive.utils.TitleUtils;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;
import com.tyxh.framlive.utils.keyboard.BaseKeyboard;
import com.tyxh.framlive.utils.keyboard.KeyboardManager;
import com.tyxh.framlive.utils.keyboard.NumberKeyboard;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

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
    @BindView(R.id.witdraw_zhangh)
    EditText mEdt_zhanghao;
    @BindView(R.id.witdraw_name)
    EditText mEdt_name;
    @BindView(R.id.tx_imgv_wechat)
    ImageView mImgv_wechat;
    @BindView(R.id.tx_imgv_zfb)
    ImageView mImage_zfb;
    @BindView(R.id.withdrawal_ptmoney)
    TextView mImage_ptMoney;
    @BindView(R.id.withdrawal_gsmoney)
    TextView mImage_gsMoney;
    @BindView(R.id.withdrawal_sjmoney)
    TextView mImage_sjMoney;


    private KeyboardManager keyboardManagerNumber;
    private NumberKeyboard numberKeyboard;
    private double now_money = 0;
    private String now_mmey = "0";
    private String text_twofive = "2. ????????????????????????3??????????????????????????????????????????3?????????????????????????????????????????????3????????????????????????????????????????????????  ??????>";
    private String text_twoeight = "5. ??????????????????????????????????????????????????????";
    private int select_what = 1;//  1  ??????  2?????????
    private GuideDialog mGuideDig_kf, mGuideDig_wechat;
    private double tx_lowermoney =30;


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
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence)){
                    DecimalFormat d_for = new DecimalFormat("0.00");
                    BigDecimal tx_exp = new BigDecimal(charSequence.toString());
                    BigDecimal bg_pt =new BigDecimal("0.001");//???????????????
                    BigDecimal bg_sf =new BigDecimal("0.1");//????????????
                    BigDecimal pt_money = tx_exp.multiply(bg_pt);
                    BigDecimal sf_money = tx_exp.subtract(pt_money).multiply(bg_sf);
                    mImage_ptMoney.setText(d_for.format(pt_money)+"???");
                    mImage_gsMoney.setText(d_for.format(sf_money)+"???");
                    mImage_sjMoney.setText(d_for.format(tx_exp.subtract(pt_money).subtract(sf_money))+"???");
                }else{
                    mImage_ptMoney.setText("0.00???");
                    mImage_gsMoney.setText("0.00???");
                    mImage_sjMoney.setText("0.00???");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @OnClick({R.id.imgv_back, R.id.withdrawal_alltix, R.id.tx_imgv_wechat, R.id.tx_tv_wechat, R.id.tx_imgv_zfb, R.id.tx_tv_zfb, R.id.button})
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
                mEdt_name.setHint("??????????????????????????????????????????");
                mEdt_zhanghao.setHint("????????????????????????");
                break;
            case R.id.tx_imgv_zfb:
            case R.id.tx_tv_zfb:
                select_what = 2;
                mImage_zfb.setImageResource(R.drawable.tx_se);
                mImgv_wechat.setImageResource(R.drawable.tx_nse);
                mEdt_name.setHint("?????????????????????????????????????????????");
                mEdt_zhanghao.setHint("??????????????????????????????");
                break;
            case R.id.button:
                toWithDraw();
                break;
        }
    }

    /*????????????
     * select_what 1:??????  2????????????
     * */
    private void toWithDraw() {
        String money = mEditText.getText().toString();
        String zhangh = mEdt_zhanghao.getText().toString();
        String name = mEdt_name.getText().toString();
        if (TextUtils.isEmpty(money) || Double.parseDouble(money) == 0) {
            ToastShow("?????????????????????");
            return;
        }
        if(Double.parseDouble(money)<tx_lowermoney){
            ToastShow("????????????????????????30???");
            return;
        }
        if (TextUtils.isEmpty(zhangh)) {
            ToastShow( select_what == 1 ? "????????????????????????" : "??????????????????????????????");//select_what 1:??????  2????????????
            return;
        }
        if (TextUtils.isEmpty(name)) {
            ToastShow( select_what == 1 ? "??????????????????????????????????????????" : "?????????????????????????????????????????????");//select_what 1:??????  2????????????
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("channel", select_what == 1 ? "2" : "1");//????????????(1:?????????2:??????)  select_what 1:??????  2????????????
        map.put("realName", name);
        map.put("recevieAccount", zhangh);
        map.put("withdrawalMoney", money);
        map.put("phone", "");
        showLoad();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(map));
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getAddwith(token, requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                hideLoad();
                BaseBean baseBean = new Gson().fromJson(result.toString(), BaseBean.class);
                if (baseBean.getRetCode() == 0) {
                    getMineAsset();
                    Toast.makeText(WithdrawalActivity.this, "?????????????????????,??????3???????????????????????????", Toast.LENGTH_LONG).show();
                } else {
                    ToastShow(baseBean.getRetMsg());
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
                hideLoad();
            }
        });


    }

    private void getMineAsset() {
        DevRing.httpManager().commonRequest(DevRing.httpManager().getService(ApiService.class).getAsset(token, user_Info.getRetData().getId()), new CommonObserver<AssetBean>() {
            @Override
            public void onResult(AssetBean assetBean) {
                if (assetBean.getRetCode() == 0) {
                    AssetBean.RetDataBean data = assetBean.getRetData();
                    now_money = Double.parseDouble(data.getBalance());
                    now_mmey = data.getBalance();
                    mEditText.setText("");
                    mWithdrawalNowmoney.setText("??????????????????" + now_mmey + "???,");
                }
            }

            @Override
            public void onError(HttpThrowable throwable) {
                if (throwable.errorType == HTTP_ERROR) {//????????????
                    EventBus.getDefault().post(new EventMessage(1005));
                }
                Log.e(TAG, "onError: " + throwable.toString());
            }
        }, TAG);
    }

    /*??????????????????????????????????????????*/
    private void setTextContent() {
        mGuideDig_kf = new GuideDialog(this, 1);
        mGuideDig_wechat = new GuideDialog(this, 2);
        /*?????????????????????*/
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
                drawState.setColor(Color.parseColor("#0A57AF"));
                drawState.setUnderlineText(false);
                drawState.clearShadowLayer();
            }
        };
        builder.setSpan(clickableSpan, text_twofive.length() - 3, text_twofive.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTv_twofive.setMovementMethod(LinkMovementMethod.getInstance());
        mTv_twofive.setHighlightColor(getResources().getColor(android.R.color.transparent));//????????????????????????
        mTv_twofive.setText(builder);
        /*?????????????????????*/
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
                drawState.setColor(Color.parseColor("#0A57AF"));
                drawState.setUnderlineText(false);
                drawState.clearShadowLayer();
            }
        };
        builder_eig.setSpan(clickableSpan_eig, text_twoeight.length() - 4, text_twoeight.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTv_twoEnght.setMovementMethod(LinkMovementMethod.getInstance());
        mTv_twoEnght.setHighlightColor(getResources().getColor(android.R.color.transparent));//????????????????????????
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
                    Toast.makeText(WithdrawalActivity.this, "?????????????????????", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(WithdrawalActivity.this, "??????" + charSequence, Toast.LENGTH_SHORT).show();
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
