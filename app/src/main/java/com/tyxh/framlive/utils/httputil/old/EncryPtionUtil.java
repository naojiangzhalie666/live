package com.tyxh.framlive.utils.httputil.old;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.tyxh.framlive.base.Constant;
import com.tyxh.framlive.base.LiveApplication;
import com.google.gson.Gson;
import com.superc.yyfflibrary.utils.MD5Utils;
import com.superc.yyfflibrary.utils.ShareUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/********************************************************************
 @version: 1.0.0
 @description: 进行加密解密的工具类
 @author: EDZ
 @time: 2019/10/10 15:49
 @变更历史:
 ********************************************************************/
public class EncryPtionUtil {
    private static final String TAG = "EncryPtionUtil";
    private String mPrivate_code;
    private String public_code;
    private Context mContext;
    private static EncryPtionUtil mInstance;

    private EncryPtionUtil(Context context) {
        mContext = context;
        public_code = (String) ShareUtil.getInstance(LiveApplication.getmInstance()).get("pub_code", "");
        mPrivate_code = (String) ShareUtil.getInstance(LiveApplication.getmInstance()).get("private_code", "");
    }

    public static EncryPtionUtil getInstance(Context context) {
        if (mInstance == null) {
            synchronized (EncryPtionUtil.class) {
                mInstance = new EncryPtionUtil(context);
            }
        }
        return mInstance;
    }

    /**
     * 获取加密后的请求体
     *
     * @param map_data 请求参数
     * @return
     */
    public RequestBody toEncryption(Map<String, Object> map_data) {

        String myKey = getNumLargeSmallLetter(16);
        Map<String, Object> map_all = new HashMap<>();
        RequestBody requestBody =null;
        long l_currentT = System.currentTimeMillis();
        if(Constant.IS_JIAMI) {
            try {
                if (Constant.PRINT_LOG) {
                    Log.e(TAG, "OkHttp:  " + "接口上传参数为：" + new Gson().toJson(map_data));
                }
                map_all.put("channel", "1");
                map_all.put("sendTime", l_currentT);
                /*-----------------------数据进行加密-----------*/
                map_all.put("data", AES.getInstance().baseencrypt(myKey, new Gson().toJson(map_data)));
                /*------------------myKey使用Rsa加密过程-----------*/
                byte[] cipherData = CRTUtils.encrypt(CRTUtils.loadPublicKeyByStr(public_code), myKey.getBytes());
                String new_myKey = Base64.encode(cipherData);
                String map_allResult = "key=" + new_myKey + "&&{\"channel\":\"1\",\"sendTime\":\"" + l_currentT + "\",\"data\":\"" + AES.getInstance().baseencrypt(myKey, new Gson().toJson(map_data)) + "\"}";
                /*------------------整体数据进行MD5加密----------------------*/
                String s_result = MD5Utils.md5Password(map_allResult);
                /*------------------重新对数据进行封装----------------------*/
                map_all.put("key", new_myKey);
                map_all.put("sign", s_result);
//                if (Constant.IS_JIAMI) {
                    requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), JSONObject.toJSONString(map_all));
//                } else {
//                    requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), JSONObject.toJSONString(map_data));
//                }
                return requestBody;
            } catch (Exception e) {
                Log.e(TAG, "上传报文发生错误toJiami: " + e.toString());
            }
        }else{
            return  RequestBody.create(MediaType.parse("application/json;charset=utf-8"), JSONObject.toJSONString(map_data));
        }
        return null;
    }

    /*进行解密*/
    public JSONObject toJieMi(JSONObject jsonObject) {
        if(TextUtils.isEmpty(mPrivate_code)){
            mPrivate_code= (String) ShareUtil.getInstance(mContext).get("private_code","");
        }
        try {
            String sign = jsonObject.getString("sign");
            String key = jsonObject.getString("key");
            String yanq_result="{\"code\":\""+jsonObject.getString("code")+"\",\"returnTime\":\""+jsonObject.getString("returnTime")+"\",\"data\":\""+jsonObject.getString("data")+"\"}";
            String s_remove = "key=" + key + "&&" + yanq_result;
            String s_mdu = MD5Utils.md5Password(s_remove);
            if (!TextUtils.isEmpty(sign)) {
                if (sign.equals(s_mdu)) {
                    byte[] res = CRTUtils.decryptPrivate(CRTUtils.loadPrivateKeyByStr(mPrivate_code), Base64.decode(key));
                    String re = new String(res);
                    String data_one = jsonObject.getString("data");
                    String data = AES.getInstance().decrypt(data_one, re);
                    try {
                        jsonObject.put("data",JSONObject.parse(data));
                    } catch (Exception e) {
                        Log.e(TAG, "toJieMi: 不是类对象:"+e.toString() );
                        jsonObject.put("data",data);
                    }
                    if(Constant.PRINT_LOG) {
                        Log.d(TAG, "验签成功");
                        Log.d(TAG, "获取并解密后的公钥: " + public_code);
                        Log.d(TAG, "获取到的私钥：  " + CRTUtils.loadPrivateKeyByStr(mPrivate_code));
                        Log.d(TAG, "toJieMi: "+s_remove);
                        Log.d(TAG, "解密后的字符串： " + re);
                        Log.d(TAG, "data_one:  " + data_one);
                        Log.d(TAG, "OkHttp解密后的返回数据： " + jsonObject.toString());
                    }
                } else {
                    Log.e(TAG, "验签失败");
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "解密报文出现错误: " + e.toString());
        }
        return jsonObject;
    }

    /**
     * 解密公钥
     *
     * @param de_code
     * @param returnTime
     */
    public void toJiemiPublic(String de_code, String returnTime) {
        try {
            String ccc = AES.getInstance().decrypt(de_code, AES.getInstance().getNewKey(returnTime));
            JSONObject jsonObject = new Gson().fromJson(ccc, JSONObject.class);
            if (jsonObject != null) {
                public_code = jsonObject.getString("publickey");
                ShareUtil.getInstance(LiveApplication.getmInstance()).put("pub_code", public_code);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 数字与大小写字母混编字符串
     *
     * @param size 随机生成的字符串长度
     * @return
     */
    public static String getNumLargeSmallLetter(int size) {
        StringBuffer buffer = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            if (random.nextInt(2) % 2 == 0) {//字母
                if (random.nextInt(2) % 2 == 0) {
                    buffer.append((char) (random.nextInt(27) + 'A'));
                } else {
                    buffer.append((char) (random.nextInt(27) + 'a'));
                }
            } else {//数字
                buffer.append(random.nextInt(10));
            }
        }
        return buffer.toString();
    }

}
