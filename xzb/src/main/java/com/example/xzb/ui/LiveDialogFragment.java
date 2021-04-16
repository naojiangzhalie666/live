package com.example.xzb.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.example.xzb.ui.dialog.RemindDialog;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Administrator on 2016/9/26.
 */
public class LiveDialogFragment extends DialogFragment {

    private RemindDialog mRemindDialog;
    private Activity mActivity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mActivity = getActivity();
        mRemindDialog = new RemindDialog(mActivity);
        mRemindDialog.show();
        mRemindDialog.setOnllClickListenenr(new RemindDialog.OnllClickListenenr() {
            @Override
            public void onllClickListener() {
                mRemindDialog.dismiss();
                if(mActivity!=null)
                    mActivity.finish();
            }
        });
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                if(mRemindDialog!=null)
                mRemindDialog.dismiss();
                if(mActivity!=null)
                    mActivity.finish();
            }
        }, 3000 , 1000);
        return mRemindDialog;
    }
}
