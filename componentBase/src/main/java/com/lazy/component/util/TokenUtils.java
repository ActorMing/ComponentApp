package com.lazy.component.util;

import android.content.Context;
import android.text.TextUtils;

import static com.lazy.component.constant.StringConstants.TOKEN;


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
        String token = PreferencesUtils.getString(Utils.getApp().getApplicationContext(), TOKEN, "");
        return !TextUtils.isEmpty(token);
    }

    /**
     * 以Token是否存在来判断是否已经登录
     *
     * @return boolean
     */
    public static String getToken() {
        return PreferencesUtils.getString(Utils.getApp().getApplicationContext(), TOKEN, "");
    }

    /**
     * 登录成功保存token
     *
     * @param context context
     */
    public static void saveToken(Context context, String token) {
        PreferencesUtils.putString(context, TOKEN, token);
    }

    /**
     * 退出登录清空token
     *
     * @param context context
     */
    public static void clearToken(Context context) {
        PreferencesUtils.putString(context, TOKEN, "");
    }


    /**
     * 退出登录清空token （同步）
     *
     * @param context context
     */
    public static void clearTokenAsync(Context context) {
        PreferencesUtils.putString(context, TOKEN, "", true);
    }
}
