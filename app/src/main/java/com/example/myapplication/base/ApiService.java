package com.example.myapplication.base;

import com.alibaba.fastjson.JSONObject;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    /*登录
    *
    * authType :  帐号密码登录：空  、手机短信验证码登录：sms(验证码)、微信登录：wx ?
    * */
    @POST("sysLoginController/login")
    Observable<JSONObject> login(@Query("username") String name, @Query("password") String pwd, @Query("authType") String authType);

    /**
     * 微信获取accrss_token
     * @param appid
     * @param secret
     * @param grant_type 固定值
     * @param code  微信返回
     * @return
     */
    @GET("https://api.weixin.qq.com/sns/oauth2/access_token")
    Observable<JSONObject> getWxtoken(@Query("appid") String appid, @Query("secret") String secret, @Query("grant_type") String grant_type, @Query("code") String code);

    /**
     *
     * @param access_token
     * @param openid
     * @param lang  固定--zh_CN
     * @return
     */
    @GET("https://api.weixin.qq.com/sns/userinfo")
    Observable<JSONObject> getWxUserInfo(@Query("access_token") String access_token, @Query("openid") String openid, @Query("lang") String lang);


}
