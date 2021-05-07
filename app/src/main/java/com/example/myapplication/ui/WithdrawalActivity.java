package com.example.myapplication.ui;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.base.LiveBaseActivity;
import com.example.myapplication.utils.TitleUtils;
import com.example.myapplication.utils.keyboard.BaseKeyboard;
import com.example.myapplication.utils.keyboard.KeyboardManager;
import com.example.myapplication.utils.keyboard.NumberKeyboard;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WithdrawalActivity extends LiveBaseActivity {

    @BindView(R.id.withdra_edt)
    EditText mEditText;
    @BindView(R.id.withdrawal_bankname)
    TextView mWithdrawalBankname;
    @BindView(R.id.withdrawal_nowmoney)
    TextView mWithdrawalNowmoney;

    private KeyboardManager keyboardManagerNumber;
    private NumberKeyboard numberKeyboard;
    private double now_money = 11123.22;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_withdrawal;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true, this);
        ButterKnife.bind(this);
        initKeyBoard();
        mWithdrawalNowmoney.setText("当前零钱余额"+now_money+"元,");


    }

    @OnClick({R.id.imgv_back, R.id.withdrawal_alltix})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgv_back:
                finish();
                break;
            case R.id.withdrawal_alltix:
                mEditText.setText(now_money+"");
                mEditText.setSelection(mEditText.getText().toString().length());
                break;
        }
    }


    private void initKeyBoard() {
        keyboardManagerNumber = new KeyboardManager(this);
        initNumberKeyboard();
        keyboardManagerNumber.bindToEditor(mEditText, numberKeyboard);
        mEditText.requestFocus();
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
                    Toast.makeText(WithdrawalActivity.this, "提现"+charSequence, Toast.LENGTH_SHORT).show();
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
