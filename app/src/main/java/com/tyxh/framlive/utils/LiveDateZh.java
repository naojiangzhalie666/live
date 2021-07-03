package com.tyxh.framlive.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.tyxh.framlive.base.Constant;
import com.tyxh.framlive.bean.MineTCVideoInfo;
import com.tyxh.framlive.bean.RoomBean;
import com.tyxh.framlive.live.TCAudienceActivity;
import com.tyxh.xzb.utils.TCConstants;

import org.json.JSONObject;

import java.util.List;

/**
 * 整合直播时的数据为   MineTCVideoInfo
 */
public class LiveDateZh {
    
    public static MineTCVideoInfo getMineVideo(RoomBean.RetDataBean  dataBean){
        MineTCVideoInfo info = new MineTCVideoInfo();
        List<RoomBean.RetDataBean.PushersBean> pushers = dataBean.pushers;
        info.playUrl = dataBean.mixedPlayURL;
        info.title = dataBean.roomName;
        info.userId = dataBean.roomCreator;
        info.groupId = dataBean.roomID;
        info.roomInfo = dataBean.roomInfo;
        info.viewerCount = dataBean.cumulativeNum;
        info.livePlay = true;
        info.type = dataBean.getUserInfo().getType();
        if (dataBean.getUserInfo().getType() > 2) {//机构--子机构
            try {
                info.meName = dataBean.getCouData().getMeName();
            } catch (Exception e) {
                info.meName = "天宇新航心理咨询机构";
                Log.e("HomeFragment", "toZhData: " + e.toString());
            }
        }
        info.mUserInfoBean = dataBean.getUserInfo();
        if (pushers != null && !pushers.isEmpty()) {
            RoomBean.RetDataBean.PushersBean pusher = pushers.get(0);
            info.nickname = pusher.userName;
            info.avatar = pusher.userAvatar;
            info.push_size = pushers.size();
            if (pushers.size() > 1) {//正在连线中的用户信息
                info.lxavatar = pushers.get(1).userAvatar;
                info.lxUserid = pushers.get(1).userID;
            }
        }
        try {
            JSONObject jsonRoomInfo = new JSONObject(dataBean.roomInfo);
            info.title = jsonRoomInfo.optString("title");
            info.frontCover = jsonRoomInfo.optString("frontcover");
            info.location = jsonRoomInfo.optString("location");
            info.lable = jsonRoomInfo.optString("label");
        } catch (Exception e) {
            e.printStackTrace();
            if (!TextUtils.isEmpty(dataBean.roomInfo)) {
                info.title = dataBean.roomInfo;
            }
        }
        try {
            JSONObject jsonCunstomInfo = new JSONObject(dataBean.custom);
            info.likeCount = jsonCunstomInfo.optInt("praise");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static Intent startPlay(final MineTCVideoInfo item, Context context){
        Intent intent = null;
        if (item.livePlay) {
            intent = new Intent(context, TCAudienceActivity.class);
            intent.putExtra(TCConstants.PLAY_URL, item.playUrl);
        } else {
//            intent = new Intent(getActivity(), TCPlaybackActivity.class);回放
//            intent.putExtra(TCConstants.PLAY_URL, TextUtils.isEmpty(item.hlsPlayUrl) ? item.playUrl : item.hlsPlayUrl);
        }

        intent.putExtra(TCConstants.PUSHER_ID, item.userId != null ? item.userId : "");
        intent.putExtra(TCConstants.PUSHER_NAME, TextUtils.isEmpty(item.nickname) ? item.userId : item.nickname);
        intent.putExtra(TCConstants.PUSHER_AVATAR, item.avatar);
        intent.putExtra(TCConstants.HEART_COUNT, "" + item.likeCount);
        intent.putExtra(TCConstants.MEMBER_COUNT, "" + item.viewerCount);
        intent.putExtra(TCConstants.GROUP_ID, item.groupId);
        intent.putExtra(TCConstants.PLAY_TYPE, item.livePlay);
        intent.putExtra(TCConstants.FILE_ID, item.fileId != null ? item.fileId : "");
        intent.putExtra(TCConstants.COVER_PIC, item.frontCover);
        intent.putExtra(TCConstants.TIMESTAMP, item.createTime);
        intent.putExtra(TCConstants.ROOM_TITLE, item.title);
        intent.putExtra(Constant.LIVE_JGNAME, item.meName);
        intent.putExtra(Constant.LIVE_ISJG, item.type);
        intent.putExtra(Constant.LIVE_ISGZ, true);//是否关注了该主播---字段还没有
        intent.putExtra(Constant.LVIE_ISLIANX, item.push_size > 1 ? true : false);//是否正在连线
        intent.putExtra(Constant.LIVE_LIANXHEAD, item.lxavatar);//连线中的用户头像
        intent.putExtra(Constant.LIVE_LXUSERID, item.lxUserid);//连线中的用户id

        return intent;
    }
    
    
}
