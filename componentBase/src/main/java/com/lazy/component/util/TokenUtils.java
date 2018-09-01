package com.lazy.component.util;

import android.content.Context;
import android.text.TextUtils;

import com.lazy.component.constant.Constants;


/**
  * @author :lazyMing
  * email   :407555147@qq.com
  * date    :2018/8/28
  * desc    :TokenUtils
  * address :
  * update  :
  */
public class TokenUtils {

    /**
     * 以Token是否存在来判断是否已经登录
     *
     * @return boolean
     */
    public static boolean isLogin() {
        String token = PreferencesUtils.getString(Utils.getApp().getApplicationContext(), Constants.PreKey.TOKEN, "");
        return !TextUtils.isEmpty(token);
    }

    /**
     * 以Token是否存在来判断是否已经登录
     *
     * @return boolean
     */
    public static String getToken() {
        return PreferencesUtils.getString(Utils.getApp().getApplicationContext(), Constants.PreKey.TOKEN, "");
    }

    /**
     * 登录成功保存token
     *
     * @param context context
     */
    public static void saveToken(Context context, String token) {
        PreferencesUtils.putString(context, Constants.PreKey.TOKEN, token);
    }

    /**
     * 退出登录清空token
     *
     * @param context context
     */
    public static void clearToken(Context context) {
        PreferencesUtils.putString(context, Constants.PreKey.TOKEN, "");
    }


    /**
     * 退出登录清空token （同步）
     *
     * @param context context
     */
    public static void clearTokenAsync(Context context) {
        PreferencesUtils.putString(context, Constants.PreKey.TOKEN, "", true);
    }
}
