package com.tyxh.framlive.base;

import com.alibaba.fastjson.JSONObject;
import com.tyxh.framlive.bean.AssetBean;
import com.tyxh.framlive.bean.LevelBean;
import com.tyxh.framlive.bean.NextLevel;
import com.tyxh.framlive.bean.UserDetailBean;
import com.tyxh.framlive.bean.UserInfoBean;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    /* 登录
     * authType :  帐号密码登录：空  、手机短信验证码登录：sms(验证码)、微信登录：wx ?
     * */
    @POST("sysLoginController/login")
    Observable<JSONObject> login(@Query("username") String name, @Query("password") String pwd, @Query("authType") String authType);

    /* 登录
     * authType :  帐号密码登录：空  、手机短信验证码登录：sms(验证码)、微信登录：wx ?
     * */
    @POST("sysLoginController/login")
    Observable<JSONObject> login(@Query("username") String name, @Query("password") String pwd);

    /*
     * 获取用户信息
     * */
    @GET("sysLoginController/info")
    Observable<UserInfoBean> getUserInfo(@Header("Authorization") String token);

    /**
     * 绑定手机号
     *
     * @param token
     * @return
     */
    @POST("userController/bindPhone")
    Observable<JSONObject> bindPhone(@Header("Authorization") String token, @Query("phone") String phone, @Query("code") String code);

    /**
     * 获取简介信息
     *
     * @param token
     * @param userId 需要获取的用户id
     * @return
     */
    @GET("sysLoginController/getDetails")
    Observable<UserDetailBean> getDetails(@Header("Authorization") String token, @Query("userId") String userId);

    /**
     * 获取直播及im登录的sign
     *
     * @param token
     * @return
     */
    @POST("liveController/genUserSig")
    Observable<JSONObject> getUserSig(@Header("Authorization") String token);

    /**
     * 更新--添加用户信息
     *
     * @param token
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @PUT("userController/updateUserInfo")
    Observable<JSONObject> updateUserInfo(@Header("Authorization") String token, @Body RequestBody body);

    /**
     * 获取手机验证码
     *
     * @param phone
     * @return
     */
    @GET("sendMsgController/sendMsg")
    Observable<JSONObject> sendMsg(@Query("phone") String phone);

    /**
     * 设置应急联系人
     *
     * @param token
     * @param phone
     * @return
     */
    @POST("userController/bindEmergencyContact")
    Observable<JSONObject> bindEmergencyPhone(@Header("Authorization") String token, @Query("phone") String phone);

    /**
     * @param mtoken 自己的TOKEN
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("liveController/getRoomList")
    Observable<JSONObject> getRoomList(@Header("Authorization") String mtoken, @Body RequestBody body);

    /**
     * 查询我关注的
     *
     * @param mtoken
     * @return
     */
    @GET("attentionController/myAttention")
    Observable<JSONObject> getMyAttention(@Header("Authorization") String mtoken);

    /**
     * 查询关注我的--我的粉丝
     *
     * @param mtoken
     * @return
     */
    @GET("attentionController/attentionMe")
    Observable<JSONObject> getAttentionMe(@Header("Authorization") String mtoken);

    /**
     * @param mtoken
     * @param attId  要关注的人:id
     * @param flag   1  关注  2取消关注
     * @return
     */
    @POST("attentionController/addAttention")
    Observable<JSONObject> addAttention(@Header("Authorization") String mtoken, @Query("attId") String attId, @Query("flag") String flag);

    /**
     * 直播中添加、取消关注
     *
     * @param mtoken
     * @param attId  关注的用户id
     * @return
     */
    @POST("attentionController/liveAddAttention")
    Observable<JSONObject> addLiveAtten(@Header("Authorization") String mtoken, @Query("attId") String attId);

    /**
     * 单文件上传
     *
     * @param mtoken
     * @param map    文件
     * @return
     */
    @Multipart
    @POST("minioAction/upload")
    Observable<JSONObject> upLoadFile(@Header("Authorization") String mtoken, @Part MultipartBody.Part map);

    /**
     * 咨询师标签--兴趣爱好
     *
     * @param token
     * @return
     */
    @GET("couLabelController")
    Observable<JSONObject> getCouLabel(@Header("Authorization") String token);

    /**
     * 咨询师入驻申请
     *
     * @param token
     * @param body
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("couCounselorController")
    Observable<JSONObject> toSubmitZxs(@Header("Authorization") String token, @Body RequestBody body);

    /**
     * 咨询机构入驻申请
     *
     * @param token
     * @param body
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("couMechanismController")
    Observable<JSONObject> toSubmitZxjg(@Header("Authorization") String token, @Body RequestBody body);

    /**
     * 绑定微信
     *
     * @param token
     * @param openId
     * @return
     */
    @POST("userController/bindWx")
    Observable<JSONObject> bindWxchat(@Header("Authorization") String token, @Query("openId") String openId);

    /**
     * 举证理由查询
     *
     * @param token
     * @return
     */
    @GET("ReportController/selectReportReason")
    Observable<JSONObject> getReportReason(@Header("Authorization") String token);

    /**
     * 举报
     *
     * @return
     */
    @POST("ReportController/report")
    Observable<JSONObject> toReprot(@Header("Authorization") String token, @Body RequestBody body);

    /**
     * 获取年龄列表
     *
     * @return
     */
    @GET("sysAgeController")
    Observable<JSONObject> getSysAge(@Header("Authorization") String token);

    /**
     * 获取系统通知
     *
     * @param token
     * @return
     */
    @GET("noticeMessageController/getSMList/{page}/{size}")
    Observable<JSONObject> getSMList(@Header("Authorization") String token, @Path("page") String page, @Path("size") String size);

    /**
     * 我的资产
     *
     * @param token
     * @param userId
     * @return
     */
    @GET("userController/getAsset/{userId}")
    Observable<AssetBean> getAsset(@Header("Authorization") String token, @Path("userId") String userId);

    /**
     * 我的礼物
     *
     * @param token
     * @param userId
     * @return
     */
    @GET("userController/getGift/{userId}")
    Observable<JSONObject> getGift(@Header("Authorization") String token, @Path("userId") String userId);

    /**
     * 获取礼物列表
     *
     * @param token
     * @param page     1
     * @param size     100
     * @param platform 平台  ios  android
     * @return
     */
    @GET("giftController/findGiftFe/{page}/{size}/{platform}")
    Observable<JSONObject> findAllGifts(@Header("Authorization") String token, @Path("page") String page, @Path("size") String size, @Path("platform") String platform);

    /**
     * 我的专属服务包
     *
     * @param token
     * @param userId
     * @return
     */
    @GET("userController/getServicePackage/{userId}")
    Observable<JSONObject> getServicePackage(@Header("Authorization") String token, @Path("userId") String userId);

    /**
     * 我的道具
     *
     * @param token
     * @param userId
     * @return
     */
    @GET("userController/getProp/{userId}")
    Observable<JSONObject> getProp(@Header("Authorization") String token, @Path("userId") String userId);

    /**
     * 购买并送出礼物
     *
     * @param token
     * @param body
     * @return
     */
    @POST("servicePackageController/purchaseGift")
    Observable<JSONObject> buyGifts(@Header("Authorization") String token, @Body RequestBody body);

    /**
     * 送出背包内的礼物
     *
     * @param token
     * @param body
     * @return
     */
    @POST("servicePackageController/useGift")
    Observable<JSONObject> useGifts(@Header("Authorization") String token, @Body RequestBody body);

    /**
     * 通过标签获取标题
     *
     * @param token
     * @param body
     * @return
     */
    @POST("liveTitleController/getTitleByLabel")
    Observable<JSONObject> getLiveTitle(@Header("Authorization") String token, @Body RequestBody body);

    /**
     * 私聊接口--成功后发送信息
     *
     * @param token
     * @param roomId
     * @return
     */
    @POST("servicePackageController/privateChat")
    Observable<JSONObject> privateChat(@Header("Authorization") String token, @Query("roomId") String roomId);

    /**
     * 修改个人信息--咨询师、机构  修改个人信息
     *
     * @param token
     * @return
     */
    @PUT("couCounselorController/patchCou")
    Observable<JSONObject> patchCou(@Header("Authorization") String token, @Query("id") String id, @Query("userType") String userType, @Query("name") String name, @Query("perIntroduce") String perIntroduce, @Query("meAddress") String meAddress);

    /**
     * 修改个人形象--咨询师--机构修改时图片
     *
     * @param token
     * @param body
     * @return
     */
    @PUT("couCounselorController/patchCouImg")
    Observable<JSONObject> patchCouimg(@Header("Authorization") String token, @Body RequestBody body);

    /**
     * 收益兑钻时候的钻石列表
     *
     * @param token
     * @param page     页码
     * @param size     数量  穿大点就得--不分页
     * @param platform 1 ios  2 android
     * @return
     */
    @GET("diamondBagController/getDiamondFe/{page}/{size}/{platform}")
    Observable<JSONObject> getDiamaond(@Header("Authorization") String token, @Path("page") String page, @Path("size") String size, @Path("platform") String platform);

    /**
     * 收益兑钻
     *
     * @param token
     * @param body
     * @return
     */
    @POST("sysOrderController/incomeToDia")
    Observable<JSONObject> incomeDiamond(@Header("Authorization") String token, @Body RequestBody body);

    /**
     * 咨询师列表--首字母版
     *
     * @param token
     * @return
     */
    @GET("couCounselorController/queryCou")
    Observable<JSONObject> queryCou(@Header("Authorization") String token);

    /**
     * 身份认证
     *
     * @param token
     * @param idFace
     * @param nationalEmblem
     * @return
     */
    @POST("IdentityVRecordController/addIdentityVRecord")
    Observable<JSONObject> addSfrz(@Header("Authorization") String token, @Query("idFace") String idFace, @Query("nationalEmblem") String nationalEmblem);

    /**
     * 获取不重复昵称
     *
     * @param token
     * @return
     */
    @GET("nicknameController/repeatedlyGenNickname")
    Observable<JSONObject> getNoRepeatName(@Header("Authorization") String token);

    /**
     * 获取支付订单
     * @param token
     * @param body  diamondId-钻石包id  remark--备注。。。 购买一元活动礼包时传actId =1 其它时候不传
     * @return
     */
    @POST("sysOrderController/add")
    Observable<JSONObject> getAddOrderdetail(@Header("Authorization") String token, @Body RequestBody body);

    /**
     * @param token
     * @param orderSn 支付订单--获取的订单号
     * @param payType 支付类型--1微信  2支付宝
     * @return
     */
    @POST("payController/pay")
    Observable<JSONObject> goPay(@Header("Authorization") String token, @Query("orderSn") String orderSn, @Query("payType") String payType);

    /**
     * 购买专属服务包
     *
     * @param specsId
     * @param roomId
     * @return
     */
    @POST("servicePackageController/purchaseServiceBag")
    Observable<JSONObject> buyServ(@Header("Authorization") String token, @Query("specsId") String specsId, @Query("roomId") String roomId);

    /**
     * 查询是否进行了关注  --[true:已关注,false:未关注]
     *
     * @param token
     * @param attId
     * @return
     */
    @POST("attentionController/isAttention")
    Observable<JSONObject> isAttention(@Header("Authorization") String token, @Query("attId") String attId);

    /**
     * 修改机构咨询师
     *
     * @param token
     * @param body  couName:咨询师名称  imgUrl:咨询师形象  meId:咨询师里面的meId
     * @return
     */
    @POST("couMechanismController/updateMeCou")
    Observable<JSONObject> updateMecou(@Header("Authorization") String token, @Body RequestBody body);

    /**
     * 私聊页连线记录查询
     *
     * @param token
     * @param page   当前页
     * @param size   当前查询数量
     * @param userId 要查询的目标id
     * @return
     */
    @GET("servicePackageController/queryConnectHistory/{page}/{size}/{userId}")
    Observable<JSONObject> getContxtHis(@Header("Authorization") String token, @Path("page") int page, @Path("size") int size, @Path("userId") String userId);

    /**
     * 添加连线备注
     *
     * @param token
     * @param id     主键id
     * @param remark 备注
     * @param title  标题
     * @return
     */
    @PUT("servicePackageController/addConnectRemark")
    Observable<JSONObject> addContctRemark(@Header("Authorization") String token, @Query("id") int id, @Query("remark") String remark, @Query("title") String title);

    /**
     * 首页咨询师查询
     *
     * @param token
     * @param pageNum     查询当前页码
     * @param pageSize    查询数量
     * @param interest    擅长方向
     * @param anchorState 状态(1:离线;2:在线;3:正在连线中)--目前传“” 无法根据这个状态进行查询
     * @return
     */
    @GET("couCounselorController/queryCounselorMechanism/{pageNum}/{pageSize}")
    Observable<JSONObject> queryAllZxs(@Header("Authorization") String token, @Path("pageNum") int pageNum, @Path("pageSize") int pageSize, @Query("interest") String interest, @Query("anchorState") String anchorState);

    /**
     * 服务特色修改
     *
     * @param token
     * @param id              修改的服务id
     * @param serviceFeature  特色1
     * @param serviceFeature2 特色2
     * @return
     */
    @PUT("servicePackageController/updateServiceFeature")
    Observable<JSONObject> updateService(@Header("Authorization") String token, @Query("id") int id, @Query("serviceFeature") String serviceFeature, @Query("serviceFeature2") String serviceFeature2);

    /**
     * 腾讯云一键登录获取手机号
     *
     * @param sdkappid
     * @param random
     * @return
     */
    @POST("https://yun.tim.qq.com/v5/rapidauth/validate")
    Observable<JSONObject> getvalidata(@Query("sdkappid") String sdkappid, @Query("random") String random, @Body RequestBody body);

    /**
     * 获取可用来进行连线的数据列表
     *
     * @param token
     * @param roomId 房间id--主播id
     * @param userId 用户id
     * @return
     */
    @GET("servicePackageController/getConnectionTime")
    Observable<JSONObject> getContTime(@Header("Authorization") String token, @Query("roomId") String roomId, @Query("userId") String userId);

    /**
     * 我的任务列表
     *
     * @param token
     * @param taskTypeId 1 新手    2 每日   3 日收益   4 月收益
     * @return
     */
    @POST("taskTableController/selectUserTask")
    Observable<JSONObject> getUserTask(@Header("Authorization") String token, @Query("taskTypeId") int taskTypeId);

    /**
     * 领取任务奖励
     *
     * @param token
     * @param taskId
     * @return
     */
    @POST("taskTableController/receiveReward")
    Observable<JSONObject> receiveReward(@Header("Authorization") String token, @Query("taskId") int taskId);

    /**
     * 我的订单--充值订单
     *
     * @param token
     * @param page
     * @param size
     * @return
     */
    @GET("sysOrderController/myOrder/{page}/{size}")
    Observable<JSONObject> myOrder(@Header("Authorization") String token, @Path("page") int page, @Path("size") int size);

    /**
     * 咨询记录查询
     *
     * @param token
     * @param isEvaluation 空：全部  1：未评价
     * @param page
     * @param size
     * @return
     */
    @GET("sysOrderController/myConnectHistory/{page}/{size}")
    Observable<JSONObject> getMyContctHis(@Header("Authorization") String token, @Path("page") int page, @Path("size") int size, @Query("isEvaluation") String isEvaluation);

    /**
     * 评价
     *
     * @param token
     * @param id
     * @param star
     * @return
     */
    @POST("connectHistoryController/evaluationConnectHistory")
    Observable<JSONObject> toEvealContctHis(@Header("Authorization") String token, @Query("id") int id, @Query("star") int star);

    /**
     * 通知关注直播间的人开始直播了
     *
     * @param token
     * @return
     */
    @POST("attentionController/noticeUserShow")
    Observable<JSONObject> noticeUsershow(@Header("Authorization") String token);

    /**
     * 开播时传标题给后台
     *
     * @param token
     * @param title
     * @return
     */
    @POST("liveController/pushTitle")
    Observable<JSONObject> pushTitle(@Header("Authorization") String token, @Query("title") String title);

    /**
     * 等级与特权
     *
     * @param token
     * @return
     */
    @GET("sysOrderController/myLevel")
    Observable<LevelBean> getMyLevel(@Header("Authorization") String token);

    /**
     * 获取下一等级相关信息
     *
     * @param token
     * @return
     */
    @GET("userController/queryNextExp")
    Observable<NextLevel> getMyNextLevel(@Header("Authorization") String token);

    /**
     * 查询我的收益（金额）
     *
     * @return
     */
    @GET("incomeExpensesController/queryMyIncome")
    Observable<JSONObject> getMyIncome(@Header("Authorization") String token);

    /**
     * 提现申请
     *
     * @return
     */
    @POST("withdrawalController/addWithdrawal")
    Observable<JSONObject> getAddwith(@Header("Authorization") String token, @Body RequestBody body);

    /**
     * 提现记录
     *
     * @return
     */
    @POST("withdrawalController/myWithdrawal")
    Observable<JSONObject> getMyWithDraw(@Header("Authorization") String token, @Body RequestBody body);

    /**
     * 做任务
     *
     * @param triggerType  触发类型【2:观看直播;5:发弹幕;7:分享直播;8:直播时长】
     * @param conditionNum 触发条件数目
     * @return
     */
    @POST("taskTableController/whileTask")
    Observable<JSONObject> toWhildTask(@Header("Authorization") String token, @Query("triggerType") int triggerType, @Query("conditionNum") String conditionNum);

    /**
     * 我的钱包(钻石列表)
     *
     * @param startDate
     * @param endDate
     * @return
     */
    @GET("userController/myPurseDiamondsWater/{page}/{size}")
    Observable<JSONObject> getMyPurse(@Header("Authorization") String token, @Path("page") int page, @Path("size") int size, @Query("startDate") String startDate, @Query("endDate") String endDate);

    /**
     * 我的收益（记录）
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param words     搜索条件
     * @return
     */
    @GET("incomeExpensesController/queryIncomeExpenses/{pageNum}/{pageSize}")
    Observable<JSONObject> getIncomePenses(@Header("Authorization") String token, @Path("pageNum") int pageNum, @Path("pageSize") int pageSize, @Query("startDate") String startDate, @Query("endDate") String endDate, @Query("words") String words);

    /**
     * 子账号-发送验证码
     *
     * @param username
     * @return
     */
    @POST("userController/sendSMS")
    Observable<JSONObject> sendSMS(@Query("username") String username);

    /**
     * 子账号  --验证码验证
     *
     * @param username
     * @param code
     * @return
     */
    @POST("userController/smsVerify")
    Observable<JSONObject> smsVerify(@Query("username") String username, @Query("code") String code);

    /**
     * 子账号--修改账号密码
     *
     * @param uuid
     * @param password
     * @return
     */
    @POST("userController/updateUserPassword")
    Observable<JSONObject> updateZiPwd(@Query("uuid") String uuid, @Query("password") String password);

    /**
     * 直播日数据
     *
     * @param token
     * @param page
     * @param size
     * @param startDate
     * @param endDate
     * @return
     */
    @GET("userController/queryLiveDayReport/{page}/{size}")
    Observable<JSONObject> queryLiveDayRt(@Header("Authorization") String token, @Path("page") int page, @Path("size") int size, @Query("startDate") String startDate, @Query("endDate") String endDate);

    /**
     * 直播月数据
     *
     * @param token
     * @param page
     * @param size
     * @param startDate
     * @param endDate
     * @return
     */
    @GET("userController/queryLiveMonReport/{page}/{size}")
    Observable<JSONObject> queryLiveMonRt(@Header("Authorization") String token, @Path("page") int page, @Path("size") int size, @Query("startDate") String startDate, @Query("endDate") String endDate);

    /**
     * 连线记录
     *
     * @param token
     * @param startDate
     * @param endDate
     * @return
     */
    @GET("connectHistoryController/queryCounselorConnectHistory/{page}/{size}")
    Observable<JSONObject> queryCotctHis(@Header("Authorization") String token, @Path("page") int page, @Path("size") int size, @Query("startDate") String startDate, @Query("endDate") String endDate, @Query("words") String words);

    /**
     * 兑换码兑换
     *
     * @param code
     * @param userId
     * @return
     */
    @POST("exchangeWaterController/exchange")
    Observable<JSONObject> exchangeCode(@Header("Authorization") String token, @Query("code") String code, @Query("userId") String userId);

    /**
     * 查询参加活动情况
     *
     * @param platform 【1:IOS;2:Android】
     * @return
     */
    @GET("userController/queryJoinActivity")
    Observable<JSONObject> queryJoAct(@Header("Authorization") String token, @Query("platform") String platform);

    /*-------------------------------------------如下为微信登录时获取数据---------------------------------------------------*/

    /**
     * 微信获取accrss_token
     *
     * @param appid
     * @param secret
     * @param grant_type 固定值
     * @param code       微信返回
     * @return
     */
    @GET("https://api.weixin.qq.com/sns/oauth2/access_token")
    Observable<JSONObject> getWxtoken(@Query("appid") String appid, @Query("secret") String secret, @Query("grant_type") String grant_type, @Query("code") String code);

    /**
     * @param access_token
     * @param openid
     * @param lang         固定--zh_CN
     * @return
     */
    @GET("https://api.weixin.qq.com/sns/userinfo")
    Observable<JSONObject> getWxUserInfo(@Query("access_token") String access_token, @Query("openid") String openid, @Query("lang") String lang);

}
