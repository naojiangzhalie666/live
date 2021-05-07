package com.example.myapplication.base;

import com.alibaba.fastjson.JSONObject;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
    Observable<JSONObject> getUserInfo(@Header("Authorization")String token);

    /**
     * 获取直播及im登录的sign
     * @param token
     * @return
     */
    @POST("liveController/genUserSig")
    Observable<JSONObject> getUserSig(@Header("Authorization")String token);

    /**
     * 更新--添加用户信息
     * @param token
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @PUT("userController/updateUserInfo")
    Observable<JSONObject> updateUserInfo(@Header("Authorization")String token,@Body RequestBody body);

    /**
     * 获取手机验证码
     * @param token
     * @param phone
     * @return
     */
    @GET("sendMsgController/sendMsg")
    Observable<JSONObject> sendMsg(@Query("phone")String phone);


    /*获取微信支付数据*/
    @GET("testController/appWxPay")
    Observable<JSONObject> getWxPayinfo();

    /*获取支付宝支付数据*/
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
