package com.lazy.component.retrofit.cookie.store;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * @author :zdxiang
 * email   :407555147@qq.com
 * date    :2018/8/29
 * desc    : cookieStore
 * address :
 * update  :
 */
public interface CookieStore {

    /**
     * add
     *
     * @param uri
     * @param cookie
     */
    void add(HttpUrl uri, List<Cookie> cookie);

    /**
     * get by uri
     *
     * @param uri
     * @return
     */
    List<Cookie> get(HttpUrl uri);

    /**
     * get
     *
     * @return
     */
    List<Cookie> getCookies();

    /**
     * remove by uri and cookie
     *
     * @param uri
     * @param cookie
     * @return
     */
    boolean remove(HttpUrl uri, Cookie cookie);

    /**
     * remove all
     *
     * @return
     */
    boolean removeAll();

}
