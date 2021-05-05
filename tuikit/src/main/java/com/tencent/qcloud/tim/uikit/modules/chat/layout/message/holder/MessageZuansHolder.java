package com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder;

import android.view.View;
import android.widget.TextView;

import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;

public class MessageZuansHolder extends MessageBaseHolder {

    public TextView chatTimeText;

    public MessageZuansHolder(View itemView) {
        super(itemView);
        rootView = itemView;

        chatTimeText = itemView.findViewById(R.id.chat_time_tv);
    }


    @Override
    public void layoutViews(final MessageInfo msg, final int position) {

        //// 时间线设置
        if (properties.getChatTimeBubble() != null) {
            chatTimeText.setBackground(properties.getChatTimeBubble());
        }
        if (properties.getChatTimeFontColor() != 0) {
            chatTimeText.setTextColor(properties.getChatTimeFontColor());
        }
        if (properties.getChatTimeFontSize() != 0) {
            chatTimeText.setTextSize(properties.getChatTimeFontSize());
        }
        chatTimeText.setText("私聊1钻石/每条");
        chatTimeText.setVisibility(View.VISIBLE);
    }

}