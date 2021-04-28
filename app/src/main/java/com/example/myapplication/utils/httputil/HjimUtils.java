package com.example.myapplication.utils.httputil;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.example.myapplication.base.Constant;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

public class HjimUtils {

    public static <T> T toGet(String result, T t){
        T t_get = null;
        try {
            t_get = new Gson().fromJson(result, (Type) t.getClass());
        } catch (JsonSyntaxException e) {
            t_get  = new Gson().fromJson(AESUtils.decrypt(AESUtils.DEFAULT_SECRET_KEY,result), (Type) t.getClass());
            Log.d("OkHttp", Constant.PRINT_LOG?JSONObject.toJSONString(t_get)+"DECRYPT DATA":"ENCRYPT DATA");
            return t_get;
        }
        return t_get;
    }

}
