package com.lazy.component.di.module;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.lazy.component.base.BaseApp;
import com.lazy.component.config.LazyConfig;
import com.lazy.component.di.annotation.CheckTokenEffective;
import com.lazy.component.di.annotation.HeaderParams;
import com.lazy.component.di.annotation.NetWork;
import com.lazy.component.di.annotation.NoNetWork;
import com.lazy.component.di.annotation.ScopeApp;
import com.lazy.component.retrofit.LazyHttpsFactory;
import com.lazy.component.retrofit.RetrofitService;
import com.lazy.component.util.DataManager;
import com.vondear.rxtool.RxNetTool;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.lazy.component.constant.StringConstants.REFRESH_TOKEN;
import static com.lazy.component.constant.StringConstants.TOKEN;

/**
 * <pre>
 *  author : liming
 *  time   : Created by on 2018/1/22.
 *  desc   : 相关文章 https://www.jianshu.com/p/66b59ad1fdc1
 *  modify :
 * </pre>
 */

@Module
public class ApiBaseServerModule {

    /**
     * 提供缓存名字
     *
     * @return 缓存名
     */
    @Provides
    @Singleton
    String providerCacheFileName() {
        return LazyConfig.get().getCacheFileName();
    }

    /**
     * 提供缓存大小
     *
     * @return 缓存大小
     */
    @Provides
    @Singleton
    long providerCacheSize() {
        return LazyConfig.get().getCacheFileSize();
    }

    /**
     * 为下面的缓存对象提供缓存文件
     *
     * @param context       AppModule提供过来的全局的Context
     * @param cacheFileName 缓存文件名
     * @return 缓存文件
     */
    @Provides
    @Singleton
    File providerCacheFile(Context context, String cacheFileName) {
        return new File(context.getCacheDir(), cacheFileName);
    }

    /**
     * 为下面的OkHttpClient提供缓存对象
     *
     * @param cacheFile AppModule提供过来的全局的Context
     * @param cacheSize 缓存大小
     * @return 缓存对象
     */
    @Provides
    @Singleton
    Cache providerCache(File cacheFile, long cacheSize) {
        return new Cache(cacheFile, cacheSize);
    }

    /**
     * 公共的请求头参数map
     *
     * @return
     */
    @Provides
    @Singleton
    HashMap<String, String> providerHeaderParamsMap() {
        HashMap<String, String> headerMap = new HashMap<>(2);
        headerMap.put(TOKEN, DataManager.getInstance().getToken());
        return headerMap;
    }

    /**
     * 请求头拦截器
     *
     * @param headerMap 公共的请求头参数map
     * @return
     */
    @HeaderParams
    @Provides
    @Singleton
    Interceptor providerHeaderInterceptor(final HashMap<String, String> headerMap) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();
                if (headerMap != null && headerMap.size() > 0) {
                    for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                        builder.addHeader(entry.getKey(), entry.getValue());
                    }
                }
                return chain.proceed(builder.build());
            }
        };
    }

    /**
     * 在有网络的情况下，先去读缓存，设置的缓存时间到了，在去网络获取
     *
     * @return 网络拦截器
     */
    @NetWork
    @Provides
    @Singleton
    Interceptor providerNetWorkInterceptor(final Context context) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                /**
                 *  1. 获取 Request 对象
                 *  2. 获取 Chain 对象  Chain继续发送请求
                 *  3. 判断网络是否可用
                 *      3.1 网络可用设置get方式的缓存
                 */
                Request request = chain.request();

                Response response = chain.proceed(request);

                // 网络状态大于Disable说明可以使用,需要进入缓存
                if (!RxNetTool.isNetworkAvailable(context)) {
                    // 缓存失效时间,单位为秒
                    int maxAge = 60;
                    // 清除头信息(Pragma)，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    return response.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .build();
                }
                return response;
            }
        };
    }

    /**
     * 在没有网络的情况下，取读缓存数据
     *
     * @return 无网络的拦截器
     */
    @NoNetWork
    @Provides
    @Singleton
    Interceptor providerNoNetWorkInterceptor(@ScopeApp final Context context) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                /**
                 *  1. 获取Request对象
                 *  2. 获取Response对象
                 *  3. 没有网络直接去缓存中获取数据
                 */
                Request request = chain.request();
                Response response = chain.proceed(request);
                // 没有网络取缓存数据
                if (!RxNetTool.isNetworkAvailable(context)) {
                    // 缓存失效时间,单位为秒
                    int maxStale = 60;
                    // 清除头信息(Pragma)，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    return response.newBuilder()
                            .removeHeader("Pragma")
                            .addHeader("Cache-Control", "public, only-if-cache, max-stale=" + maxStale)
                            .build();
                }
                return response;
            }
        };
    }

    /**
     * 日志拦截器
     *
     * @return
     */
    @Provides
    @Singleton
    HttpLoggingInterceptor providerInterceptor() {
        //日志显示级别
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e("RetrofitBody", message);
            }
        });
        return loggingInterceptor.setLevel(level);
    }

    private Handler mHandler = new Handler();
    private String resultStr;

    /**
     * CheckTokenEffectiveInterceptor
     * 校验token有效性拦截器{主要处理token失效进行界面跳转,token过期重新刷新等问题}
     *
     * @return
     */
    @CheckTokenEffective
    @Provides
    @Singleton
    Interceptor providerCheckTokenEffectiveInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .header(TOKEN, DataManager.getInstance().getToken())
                        .build();
                Response response = chain.proceed(request);
                MediaType mediaType = response.body().contentType();
                // 判断 token 是否过期
                if (isTokenExpired(response)) {
                    //同步请求方式，获取最新的Token
                    Log.e("OkHttp", "静默自动刷新Token,然后重新请求数据");
                    final String refreshToken = getNewToken();
                    if (!TextUtils.isEmpty(resultStr)) {
                        Request newRequest = chain.request()
                                .newBuilder()
                                .header(REFRESH_TOKEN, refreshToken)
                                .build();
                        return chain.proceed(newRequest.newBuilder().build());
                    }
                }
                return response.newBuilder().body(ResponseBody.create(mediaType, resultStr)).build();
            }
        };
    }

    /**
     * 根据 Response 判断 token 是否失效
     *
     * @param response
     * @return
     */
    private boolean isTokenExpired(Response response) {
        try {
            resultStr = response.body().toString();
//            RequestCode requestCode = new Gson().fromJson(resultStr, RequestCode.class);
//            Log.e("OkHttpManager", requestCode.getResultCode() + "----requestCode");
//            Log.e("OkHttpManager", resultStr + "----requestCode");
//            if (requestCode.getResultCode() == 30001) {
//                Log.e("OkHttpManager", "----requestCode,Token登录过期了");
//                return true;
//            }
//            if (requestCode.getResultCode() == 30002) {
//                Log.e("OkHttpManager", "----requestCode,Token过期了");
//                return true;
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据 refreshToken 获取最新的 token
     *
     * @return 最新的token值
     */
    private String getNewToken() {
        final String refreshTokenUrl = "";
        Log.e("OkHttp", "重新请求---" + refreshTokenUrl);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(refreshTokenUrl).build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
//            TokenEntity data = new Gson().fromJson(response.body().string(), TokenEntity.class);
//            Log.e("OkHttpManager", "重新请求---"+data.resultCode);
//            if (data.resultCode == 0) {
//                if (null != data.data.accessToken && null != data.data.refreshToken) {
//                    PsUtils.putString(IeouApplication.getInstance(), "accessToken", data.data.accessToken);
//                    PsUtils.putString(IeouApplication.getInstance(), "refreshToken", data.data.refreshToken);
//                    PsUtils.putString(IeouApplication.getInstance(), "upToken", data.data.upToken);
            DataManager.getInstance().saveToken("");
//                    Log.e("OkHttpManager", data.data.accessToken);
//                }
//            }else {
//                mHandler.post(() -> ToastUtils.showToast("登录已过期，请重新登录..."));
//                JPushInterface.deleteAlias(IeouApplication.getInstance(), 1);
//            launcherIntent(LoginActivity.class);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 根据className启动对应的意图
     *
     * @param aClass 目标类
     */
    private void launcherIntent(Class aClass) {
        Intent intent = new Intent(BaseApp.getIns().getApplicationContext(), aClass);
        BaseApp.getIns().startActivity(intent);
    }

    /**
     * 信任所有的 SSL 签名
     *
     * @return
     */
    @Provides
    @Singleton
    SSLSocketFactory providerSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }

    /**
     * 为下面的Retrofit提供OkHttpClient对象
     *
     * @param cache                上面提供的缓存对象
     * @param networkInterceptor   上面提供的网络拦截器
     * @param noNetWorkInterceptor 上面提供的无网络拦截器
     * @return OkHttpClient
     */
    @Provides
    @Singleton
    OkHttpClient providerOkHttpClient(Cache cache,
                                      @ScopeApp Context context,
                                      @NetWork Interceptor networkInterceptor,
                                      @NoNetWork Interceptor noNetWorkInterceptor,
                                      @HeaderParams Interceptor paramsInterceptor,
                                      @CheckTokenEffective Interceptor tokenInterceptor,
                                      SSLSocketFactory sslSocketFactory,
                                      HttpLoggingInterceptor httpLoggingInterceptor) {
        /**
         * 1. 设置连接超时时间
         * 2. 设置读取超时时间
         * 3. 设置写入超时时间
         * 4. 设置网络缓存拦截器
         * 5. 设置无网络的拦截器
         * 6. 设置缓存文件
         * 7. 设置失败后是否从新连接
         */

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(LazyConfig.get().getConnTimeout(), TimeUnit.SECONDS)
                .readTimeout(LazyConfig.get().getReadTimeout(), TimeUnit.SECONDS)
                .writeTimeout(LazyConfig.get().getWriteTimeout(), TimeUnit.SECONDS)
                .addNetworkInterceptor(networkInterceptor)
                .addNetworkInterceptor(noNetWorkInterceptor)
                .addInterceptor(paramsInterceptor)
                .addInterceptor(tokenInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .cache(cache)
                .retryOnConnectionFailure(LazyConfig.get().isHttpRetry());

        if (LazyConfig.get().isIgnoreSSL()) {
            builder.sslSocketFactory(sslSocketFactory);
        } else {
            if (LazyConfig.get().getCertificates() != null && LazyConfig.get().getCertificates().length > 0) {
                builder.sslSocketFactory(LazyHttpsFactory.getSSLSocketFactory(context, LazyConfig.get().getCertificates()));
            } else {
                builder.sslSocketFactory(sslSocketFactory);
            }
        }

        // receive all hostname address
        if (LazyConfig.get().isReceiveAllHostname()) {
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } else {
            // only receive support specified  hostname address
            if (LazyConfig.get().getSupportHostnameArray() != null
                    && LazyConfig.get().getSupportHostnameArray().length > 0) {
                builder.hostnameVerifier(LazyHttpsFactory.getHostnameVerifier(LazyConfig.get().getSupportHostnameArray()));
            } else {
                builder.hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
            }
        }

        return builder.build();
    }

    /**
     * 提供一个全局唯一的Retrofit对象,方便外部进行网络数据访问
     *
     * @param okHttpClient 上面提供的OkHttpClient对象
     * @return Retrofit
     */
    @Provides
    @Singleton
    Retrofit providerRetrofit(OkHttpClient okHttpClient) {

        /**
         *  . 设置OkHttpClient
         *  . 设置回调适配工厂对象RxJavaAdapterFactory
         *  . 设置数据转换工厂对象GsonConverterFactory 或者使用 FastJsonConverterFactory
         *  . 设置字符串转换工厂对象
         */
        okHttpClient.sslSocketFactory();
        Retrofit.Builder builder = new Retrofit.Builder();
        if (TextUtils.isEmpty(LazyConfig.get().getBaseHttpUrl())) {
            throw new NullPointerException("baseUrl can not null");
        }
        builder.baseUrl(LazyConfig.get().getBaseHttpUrl());
        builder.client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
//                .addConverterFactory(FastJsonConverterFactory.create());
                .addConverterFactory(GsonConverterFactory.create());
        return builder.build();
    }

    /**
     * 提供一个全局的 retrofitService 对象
     *
     * @param retrofit 网络请求对象
     * @return retrofitService 对象
     */
    @Provides
    @Singleton
    RetrofitService providerRetrofitService(Retrofit retrofit) {
        return retrofit.create(RetrofitService.class);
    }
}
