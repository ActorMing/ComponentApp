package com.lazy.component.util;

import io.reactivex.annotations.NonNull;

import static com.lazy.component.constant.StringConstants.TOKEN;

/**
 * @author :lazyMing
 * email   :407555147@qq.com
 * date    :2018/11/22
 * desc    :数据管理工具类(保存用户登陆等数据)
 * address :
 * update  :
 */
public class DataManager {

    private static DataManager instance;

    private DataManager() {
    }

    public static DataManager getInstance() {
        if (instance == null) {
            synchronized (DataManager.class) {
                if (instance == null) {
                    instance = new DataManager();
                }
            }
        }
        return instance;
    }

    /**
     * 保存用户token
     *
     * @param token
     */
    public void saveToken(@NonNull String token) {
        SharedPreferencesUtils.put(TOKEN, token);
    }

    /**
     * 获取用户登陆的token
     *
     * @return
     */
    public String getToken() {
        return SharedPreferencesUtils.get(TOKEN);
    }
}
