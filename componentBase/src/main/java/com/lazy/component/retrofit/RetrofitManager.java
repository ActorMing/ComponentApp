

package com.lazy.component.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lazy.component.retrofit.cookie.CookieJarImpl;
import com.lazy.component.retrofit.cookie.store.PersistentCookieStore;
import com.lazy.component.util.Utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * @author :zdxiang
 * email   :407555147@qq.com
 * date    :2018/8/29
 * desc    :retorfitManager
 * address :
 * update  :
 */
public class RetrofitManager {

    /**
     * 访问超时时间
     */
    private static final long TIMEOUT = 15000L;

    private static RetrofitManager retrofitManager;

    private RetrofitService retrofitService;


    private RetrofitManager() {
        CookieJarImpl cookieJarImpl = new CookieJarImpl(new PersistentCookieStore(Utils.getApp()));
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(Interceptor.buildHttpLoggingInterceptor())
                .addInterceptor(Interceptor.buildHeaderInterceptor())
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .cookieJar(cookieJarImpl)
                .build();

        Gson gson = new GsonBuilder().setLenient().create();

        retrofitService = new Retrofit.Builder()
                .baseUrl("")
                // 添加Gson转换器
                .addConverterFactory(GsonConverterFactory.create(gson))
                // 添加Retrofit到RxJava的转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
                .create(RetrofitService.class);
    }


    public static RetrofitManager getInstance() {
        if (retrofitManager == null) {
            synchronized (RetrofitManager.class) {
                if (retrofitManager == null) {
                    retrofitManager = new RetrofitManager();
                }
            }
        }
        return retrofitManager;
    }

    public RetrofitService getService() {
        return retrofitService;
    }
}
