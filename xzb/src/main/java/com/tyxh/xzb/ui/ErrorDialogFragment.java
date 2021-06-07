package com.tyxh.xzb.ui;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.tyxh.xzb.ui.dialog.ErrorDialog;

import static com.tyxh.xzb.important.MLVBCommonDef.LiveRoomErrorCode.ERROR_LICENSE_INVALID;


/**
 * Created by Administrator on 2016/9/26.
 */
public class ErrorDialogFragment extends DialogFragment {

    private ErrorDialog mErrorDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int errorCode = getArguments().getInt("errorCode");
        String info_msg = "";
        if (errorCode == ERROR_LICENSE_INVALID) {
            String errInfo = "License 校验失败";
            info_msg =errInfo+"详情请到官网进行查看";
          /*  int start = (errInfo + " 详情请点击[").length();
            int end = (errInfo + " 详情请点击[License 使用指南").length();
            SpannableStringBuilder spannableStrBuidler = new SpannableStringBuilder(errInfo + " 详情请点击[License 使用指南]");
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse("https://cloud.tencent.com/document/product/454/34750");
                    intent.setData(content_url);
                    startActivity(intent);
                }
            };
            spannableStrBuidler.setSpan(new ForegroundColorSpan(Color.BLUE), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableStrBuidler.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);*/

         /*   TextView tv = new TextView(this.getActivity());
            tv.setMovementMethod(LinkMovementMethod.getInstance());
            tv.setText(spannableStrBuidler);
            tv.setPadding(20, 50, 20, 0);
            builder.setView(tv).setTitle("推流失败");*/
        } else {
            info_msg = getArguments().getString("errorMsg");
        }
        mErrorDialog = new ErrorDialog.ErrorBuilder().setTitle("推流失败").setContent(info_msg).build(getActivity());
        mErrorDialog.setOnBtViewClickListener(new ErrorDialog.OnBtViewClickListener() {
            @Override
            public void onCancelClickListener() {

            }

            @Override
            public void onSureClickLlistener() {
                mErrorDialog.dismiss();

                getActivity().finish();
            }
        });



        return mErrorDialog;
    }
}
