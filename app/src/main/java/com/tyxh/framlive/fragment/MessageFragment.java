package com.tyxh.framlive.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ljy.devring.DevRing;
import com.superc.yyfflibrary.utils.DateUtil;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.qcloud.tim.uikit.base.BaseFragment;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.component.action.PopActionClickListener;
import com.tencent.qcloud.tim.uikit.component.action.PopDialogAdapter;
import com.tencent.qcloud.tim.uikit.component.action.PopMenuAction;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationListLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.base.ConversationInfo;
import com.tencent.qcloud.tim.uikit.utils.DateTimeUtil;
import com.tencent.qcloud.tim.uikit.utils.PopWindowUtil;
import com.tyxh.framlive.R;
import com.tyxh.framlive.base.Constant;
import com.tyxh.framlive.base.LiveApplication;
import com.tyxh.framlive.bean.NoticeBean;
import com.tyxh.framlive.chat.ChatActivity;
import com.tyxh.framlive.ui.MailListActivity;
import com.tyxh.framlive.ui.NoticeActivity;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.fragment.app.Fragment;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends BaseFragment {
    private static final String TAG = "MessageFragment";
    private ConversationLayout mConversationLayout;
    private View mBaseView;
    private TextView mtv_notic, mtv_content, mtv_time, mtv_nomsg;
    private ImageView mimg_mail,mimg_notice;
    private ListView mConversationPopList;
    private PopDialogAdapter mConversationPopAdapter;
    private PopupWindow mConversationPopWindow;
    private List<PopMenuAction> mConversationPopActions = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.fragment_message, container, false);
        initView();
        Log.e(TAG, "onCreateView: " );
        return mBaseView;
    }


    private void initView() {
        Log.e(TAG, "initView: ");
        // 从布局文件中获取会话列表面板
        mConversationLayout = mBaseView.findViewById(R.id.conversation_layout);
        mtv_nomsg = mBaseView.findViewById(R.id.message_nomsg);
        mtv_notic = mBaseView.findViewById(R.id.message_tvnotice);
        mtv_content = mBaseView.findViewById(R.id.message_content);
        mtv_time = mBaseView.findViewById(R.id.message_time);
        mimg_mail = mBaseView.findViewById(R.id.msg_tongxunlv);
        mimg_notice = mBaseView.findViewById(R.id.imageView11);
        TitleBarLayout titleBarLayout = mConversationLayout.findViewById(R.id.conversation_title);
        titleBarLayout.setVisibility(GONE);
        // 会话列表面板的默认UI和交互初始化
        mConversationLayout.initDefault();
        // 通过API设置ConversataonLayout各种属性的样例，开发者可以打开注释，体验效果
//        ConversationLayoutHelper.customizeConversation(mConversationLayout);
        mConversationLayout.getConversationList().setOnItemClickListener(new ConversationListLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ConversationInfo conversationInfo) {
                //此处为demo的实现逻辑，更根据会话类型跳转到相关界面，开发者可根据自己的应用场景灵活实现
                startChatActivity(conversationInfo);
            }
        });
        mConversationLayout.getConversationList().setOnItemLongClickListener(new ConversationListLayout.OnItemLongClickListener() {
            @Override
            public void OnItemLongClick(View view, int position, ConversationInfo conversationInfo) {
                startPopShow(view, position, conversationInfo);
            }
        });
        mConversationLayout.getConversationList().setOnDeleteClickListener(new ConversationListLayout.OnDeleteClickListener() {
            @Override
            public void onItemDeleteClickListener(View view, int position, ConversationInfo conversationInfo) {
                mConversationLayout.deleteConversation(position, conversationInfo);//删除对话
//                mConversationLayout.setConversationTop(position, conversationInfo);//置顶--取消置顶
            }
        });
        mimg_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MailListActivity.class));
            }
        });
        mBaseView.findViewById(R.id.textView54).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NoticeActivity.class));
            }
        });
        initTitleAction();
        initPopMenuAction();
    }

    private void initTitleAction() {
//        mConversationLayout.getTitleBar().setOnRightClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mMenu.isShowing()) {
//                    mMenu.hide();
//                } else {
//                    mMenu.show();
//                }
//            }
//        });
    }

    private void initPopMenuAction() {

        // 设置长按conversation显示PopAction
        List<PopMenuAction> conversationPopActions = new ArrayList<PopMenuAction>();
        PopMenuAction action = new PopMenuAction();
        action.setActionName(getResources().getString(R.string.chat_top));
        action.setActionClickListener(new PopActionClickListener() {
            @Override
            public void onActionClick(int position, Object data) {
                mConversationLayout.setConversationTop(position, (ConversationInfo) data);
            }
        });
        conversationPopActions.add(action);
        action = new PopMenuAction();
        action.setActionClickListener(new PopActionClickListener() {
            @Override
            public void onActionClick(int position, Object data) {
                mConversationLayout.deleteConversation(position, (ConversationInfo) data);
            }
        });
        action.setActionName(getResources().getString(R.string.chat_delete));
        conversationPopActions.add(action);
        mConversationPopActions.clear();
        mConversationPopActions.addAll(conversationPopActions);
    }

    /**
     * 长按会话item弹框
     *
     * @param index            会话序列号
     * @param conversationInfo 会话数据对象
     * @param locationX        长按时X坐标
     * @param locationY        长按时Y坐标
     */
    private void showItemPopMenu(final int index, final ConversationInfo conversationInfo, float locationX, float locationY) {
        if (mConversationPopActions == null || mConversationPopActions.size() == 0)
            return;
        View itemPop = LayoutInflater.from(getActivity()).inflate(R.layout.pop_menu_layout, null);
        mConversationPopList = itemPop.findViewById(R.id.pop_menu_list);
        mConversationPopList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PopMenuAction action = mConversationPopActions.get(position);
                if (action.getActionClickListener() != null) {
                    action.getActionClickListener().onActionClick(index, conversationInfo);
                }
                mConversationPopWindow.dismiss();
            }
        });

        for (int i = 0; i < mConversationPopActions.size(); i++) {
            PopMenuAction action = mConversationPopActions.get(i);
            if (conversationInfo.isTop()) {
                if (action.getActionName().equals(getResources().getString(R.string.chat_top))) {
                    action.setActionName(getResources().getString(R.string.quit_chat_top));
                }
            } else {
                if (action.getActionName().equals(getResources().getString(R.string.quit_chat_top))) {
                    action.setActionName(getResources().getString(R.string.chat_top));
                }

            }
        }
        mConversationPopAdapter = new PopDialogAdapter();
        mConversationPopList.setAdapter(mConversationPopAdapter);
        mConversationPopAdapter.setDataSource(mConversationPopActions);
        mConversationPopWindow = PopWindowUtil.popupWindow(itemPop, mBaseView, (int) locationX, (int) locationY);
        mBaseView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mConversationPopWindow.dismiss();
            }
        }, 10000); // 10s后无操作自动消失
    }

    private void startPopShow(View view, int position, ConversationInfo info) {
        showItemPopMenu(position, info, view.getX(), view.getY() + view.getHeight() / 2);
    }

    private void startChatActivity(ConversationInfo conversationInfo) {
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(V2TIMConversation.V2TIM_C2C);
        chatInfo.setId(conversationInfo.getId());
        chatInfo.setChatName(conversationInfo.getTitle());
//        Intent intent = new Intent(getActivity(), ChathelfActivity.class);
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra(Constant.CHAT_INFO, chatInfo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getSMList(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken(), "1", "10"), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                NoticeBean bean = new Gson().fromJson(result.toString(), NoticeBean.class);
                if (bean.getRetCode() == 0) {
                    if (bean.getRetData() != null && bean.getRetData().getList() != null && bean.getRetData().getList().size() > 0) {
                        String createDate = bean.getRetData().getList().get(0).getCreateDate();
                        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd");
                        String format = simpleDateFormat.format(new Date());
                        if(format.equals(createDate.split(" ")[0])){
                            mtv_notic.setVisibility(View.VISIBLE);
                            mtv_content.setVisibility(View.VISIBLE);
                            mtv_time.setVisibility(View.VISIBLE);
                            mtv_nomsg.setVisibility(GONE);
                            mtv_notic.setText("通知");
                            mtv_content.setText(bean.getRetData().getList().get(0).getNoticeContent());
                            mtv_time.setText(DateTimeUtil.getTimeFormatText(new Date(Long.parseLong(DateUtil.getTimeLong(createDate, "yyyy-MM-dd HH:mm:ss")) * 1000)));
                            Glide.with(getActivity()).load(bean.getRetData().getList().get(0).getIco()).placeholder(R.drawable.live_defaultimg).error(R.drawable.live_defaultimg).into(mimg_notice);

                        }
                        Log.e("MessageFragment", "onSuccessListener:createDate: "+createDate  +"  format: "+format );

                    } else {
                        mtv_notic.setVisibility(GONE);
                        mtv_content.setVisibility(GONE);
                        mtv_time.setVisibility(GONE);
                        mtv_nomsg.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DevRing.httpManager().stopRequestByTag(LiveHttp.TAG);
    }
}
