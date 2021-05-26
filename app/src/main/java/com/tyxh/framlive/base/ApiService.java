package com.tyxh.framlive.base;

import com.alibaba.fastjson.JSONObject;
import com.tyxh.framlive.bean.AssetBean;
import com.tyxh.framlive.bean.UserDetailBean;

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

    /*
     * 获取用户信息
     * */
    @GET("sysLoginController/info")
    Observable<JSONObject> getUserInfo(@Header("Authorization") String token);

    /**
     * 绑定手机号
     * @param token
     * @return
     */
    @POST("userController/bindPhone")
    Observable<JSONObject> bindPhone(@Header("Authorization") String token,@Query("phone")String phone,@Query("code")String code);

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
     * @param index  当前页数
     * @param cnt    请求数量
     * @param userID
     * @param token  MLVB的token
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
     * @param token
     * @return
     */
    @PUT("couCounselorController/patchCou")
    Observable<JSONObject> patchCou(@Header("Authorization") String token,@Query("id")String id,@Query("userType")String userType,@Query("name")String name,@Query("perIntroduce")String perIntroduce,@Query("meAddress")String meAddress);

    /**
     * 修改个人形象--咨询师--机构修改时图片
     * @param token
     * @param body
     * @return
     */
    @PUT("couCounselorController/patchCouImg")
    Observable<JSONObject> patchCouimg(@Header("Authorization") String token, @Body RequestBody body);

    /**
     * 收益兑钻时候的钻石列表
     * @param token
     * @param page      页码
     * @param size      数量  穿大点就得--不分页
     * @param platform  1 ios  2 android
     * @return
     */
    @GET("diamondBagController/getDiamondFe/{page}/{size}/{platform}")
    Observable<JSONObject> getDiamaond(@Header("Authorization") String token,@Path("page")String page,@Path("size")String size,@Path("platform")String platform);

    /**
     * 收益兑钻
     * @param token
     * @param body
     * @return
     */
    @POST("sysOrderController/incomeToDia")
    Observable<JSONObject> incomeDiamond(@Header("Authorization") String token,@Body RequestBody body);

    /**
     * 咨询师列表--首字母版
     * @param token
     * @return
     */
    @GET("couCounselorController/queryCou")
    Observable<JSONObject> queryCou(@Header("Authorization") String token);

    /**
     * 身份认证
     * @param token
     * @param idFace
     * @param nationalEmblem
     * @return
     */
    @POST("IdentityVRecordController/addIdentityVRecord")
    Observable<JSONObject> addSfrz(@Header("Authorization") String token,@Query("idFace")String idFace,@Query("nationalEmblem")String nationalEmblem);

    /**
     * 获取不重复昵称
     * @param token
     * @return
     */
    @GET("nicknameController/repeatedlyGenNickname")
    Observable<JSONObject> getNoRepeatName(@Header("Authorization") String token);

    /**
     * 获取支付订单
     * @param token
     * @param body  diamondId-钻石包id  remark--备注。。。
     * @return
     */
    @POST("sysOrderController/add")
    Observable<JSONObject> getAddOrderdetail(@Header("Authorization") String token,@Body RequestBody body);

    /**
     *
     * @param token
     * @param orderSn   支付订单--获取的订单号
     * @param payType   支付类型--1微信  2支付宝
     * @return
     */
    @POST("payController/pay")
    Observable<JSONObject> goPay(@Header("Authorization") String token,@Query("orderSn")String orderSn,@Query("payType")String payType);

    /**
     * 购买专属服务包
     * @param specsId
     * @param roomId
     * @return
     */
    @POST("servicePackageController/purchaseServiceBag")
    Observable<JSONObject> buyServ(@Header("Authorization") String token,@Query("specsId")String specsId,@Query("roomId")String roomId);

    /**
     * 查询是否进行了关注  --[true:已关注,false:未关注]
     * @param token
     * @param attId
     * @return
     */
    @POST("attentionController/isAttention")
    Observable<JSONObject> isAttention(@Header("Authorization") String token,@Query("attId")String attId);

    /**
     * 修改机构咨询师
     * @param token
     * @param body      couName:咨询师名称  imgUrl:咨询师形象  meId:咨询师里面的meId
     * @return
     */
    @POST("couMechanismController/updateMeCou")
    Observable<JSONObject> updateMecou(@Header("Authorization") String token,@Body RequestBody body);


    /*测试接口--获取微信支付数据*/
    @GET("testController/appWxPay")
    Observable<JSONObject> getWxPayinfo();

    /*测试接口--获取支付宝支付数据*/
    @GET("testController/appALiPay")
    Observable<JSONObject> getZFBPayinfo();

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
