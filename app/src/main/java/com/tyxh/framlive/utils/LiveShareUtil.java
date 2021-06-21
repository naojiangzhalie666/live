package com.tyxh.framlive.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.tyxh.framlive.bean.UserInfoBean;
import com.google.gson.Gson;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LiveShareUtil {
    public static final String FILE_NAME = "APP_FILE";

    private static LiveShareUtil mShareUtil;
    private static Context mContext;
    public static final String APP_BUYONE = "app_oneb";//是否可购买一元活动
    public static final String APP_AGREE = "app_agree";//是否同意了用户协议
    public static final String APP_POWER = "app_power";//用户等级
    public static final String APP_TOKEN = "app_token";//用户token
    public static final String APP_USERID = "app_userid";//用户userid
    public static final String APP_USERNAME = "app_name";//用户名称
    public static final String APP_USERSIGN = "app_sign";//用户签名
    public static final String APP_USERHEAD = "app_head";//头像
    public static final String APP_USERCOVER = "app_cover";//封面图

    private LiveShareUtil() {
    }

    public static LiveShareUtil getInstance(Context context) {
        if (mShareUtil == null) {
            synchronized (LiveShareUtil.class) {
                if (mShareUtil == null) {
                    mShareUtil = new LiveShareUtil();
                }
            }
        }
        mContext = context;
        return mShareUtil;
    }

    /**
     * @param value 1 观众 2 咨询师 3 咨询机构  4 子咨询师
     */
    public void putPower(int value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(APP_POWER, value);
        SharedPreferencesCompat.apply(edit);
    }

    public int getPower() {
        SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getInt(APP_POWER, 0);
    }

    /**
     * 是否同意了协议
     *
     * @return true同意 false未同意
     */
    public boolean getAgree() {
        SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(APP_AGREE, false);
    }

    /**
     * 目前未使用到
     * 是否购买过一元活动
     *
     * @return true购买过 false未购买过
     */
    public boolean getOneBuy() {
        SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(APP_BUYONE, false);
    }

    /*存储及获取Token*/
    public void putToken(String value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(APP_TOKEN, value);
        SharedPreferencesCompat.apply(edit);
    }

    public String getToken() {
        SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString(APP_TOKEN, "");
    }

    /*获取存储的用户信息*/
    public UserInfoBean getUserInfo() {
        SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        String user = sp.getString("user", "");
        if (TextUtils.isEmpty(user)) {
            return null;
        } else {
            return new Gson().fromJson(user, UserInfoBean.class);
        }
    }

    public void put(String name, Object value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        if (value instanceof String) {
            edit.putString(name, (String) value);
        } else if (value instanceof Integer) {
            edit.putInt(name, (Integer) value);
        } else if (value instanceof Boolean) {
            edit.putBoolean(name, (Boolean) value);
        } else if (value instanceof Float) {
            edit.putFloat(name, (Float) value);
        } else if (value instanceof Long) {
            edit.putLong(name, (Long) value);
        } else {
            edit.putString(name, new Gson().toJson(value));
        }
        SharedPreferencesCompat.apply(edit);
    }

    public Object get(String name, Object defaultValue) {
        SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        if (defaultValue instanceof String) {
            return sp.getString(name, (String) defaultValue);
        } else if (defaultValue instanceof Integer) {
            return sp.getInt(name, (Integer) defaultValue);
        } else if (defaultValue instanceof Boolean) {
            return sp.getBoolean(name, (Boolean) defaultValue);
        } else if (defaultValue instanceof Float) {
            return sp.getFloat(name, (Float) defaultValue);
        } else if (defaultValue instanceof Long) {
            return sp.getLong(name, (Long) defaultValue);
        }
//        else{edit.putString(name,new Gson().toJson(value));}
        return null;

    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     */
    public void remove(String key) {
        SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     */
    public void clear() {
        SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }
            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }
}
