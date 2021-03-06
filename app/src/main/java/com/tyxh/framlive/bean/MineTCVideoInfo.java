package com.tyxh.framlive.bean;

import com.tyxh.xzb.utils.onlinelive.TCVideoInfo;

import org.json.JSONObject;

/**
 *  Module:   TCVideoInfo
 *
 *  Function: 视频信息
 *
 */
public class MineTCVideoInfo extends TCVideoInfo {
    public String   userId;         // 用户 userId
    public String   groupId;        // 直播的房间号Id
    public boolean  livePlay;       // 是否为直播
    public int      viewerCount;    // 已看人数
    public int      likeCount;      // 点赞人数
    public String   title;          // 标题
    public String   playUrl;        // 播放链接
    public String   fileId;         // 点播的FileId
    public String   nickname;       // 昵称
    public String   frontCover;     // 直播封面
    public String   location;       // 定位信息
    public String   avatar;         // 头像
    public String   lxavatar;       //连线中用户头像
    public String   lxUserid;       //连线中用户id
    public String   createTime;     // 开播时间
    public String   hlsPlayUrl;     // HLS播放链接
    public String   lable;          //标签
    public String   meName;         //机构名称
    public String   roomInfo;
    public int push_size;//推流个数
    public int index ;
    public boolean fault =false;
    public int type;//用户类型：1-普通用户；2-咨询师；3-主机构；4-子机构
    public RoomBean.RetDataBean.UserInfoBean mUserInfoBean;


    public MineTCVideoInfo() {}

    public MineTCVideoInfo(JSONObject data) {
        try {
            this.userId = data.optString("userid");
            this.nickname   = data.optString("nickname");
            this.avatar     = data.optString("avatar");
            this.fileId = data.optString("file_id");
            this.title      = data.optString("title");
            this.frontCover = data.optString("frontcover");
            this.location   = data.optString("location");
            this.playUrl = data.optString("play_url");
            this.hlsPlayUrl = data.optString("hls_play_url");
            this.createTime = data.optString("create_time");
            this.likeCount  = data.optInt("likecount");
            this.viewerCount  = data.optInt("viewer_count");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
