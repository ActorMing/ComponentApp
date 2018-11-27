package com.lazy.component.retrofit;

import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.lazy.component.commpoentbase.BuildConfig;
import com.lazy.component.util.Md5Utils;
import com.lazy.component.util.TokenUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;

/**
 * @author :zdxiang
 * email   :407555147@qq.com
 * date    :2018/8/30
 * desc    :OkHttp 拦截器
 * address :
 * update  :
 */
public class Interceptor {

    private static final String TAG = "RetrofitManager";

    /**
     * 公共参数拦截器
     */
    static okhttp3.Interceptor buildQueryParameterInterceptor() {
        return new okhttp3.Interceptor() {
            @Override
            public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request request;
                HttpUrl modifiedUrl = originalRequest.url().newBuilder()
                        .addQueryParameter("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                        .build();
                request = originalRequest.newBuilder().url(modifiedUrl).build();
                return chain.proceed(request);
            }
        };
    }


    /**
     * Init HeaderInterceptor
     */
    public static okhttp3.Interceptor buildHeaderInterceptor() {
        return new okhttp3.Interceptor() {
            @Override
            public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                Request originalRequest = chain.request();
                String currentTimeMillis = String.valueOf(System.currentTimeMillis() / 1000);
                String randomNumber = getRandomNumber();

                Map<String, Object> params = new HashMap<>();
                //添加动态请求参数
                Set<String> parameterNames = originalRequest.url().queryParameterNames();

                for (String key : parameterNames) {  //循环参数列表
                    params.put(key, originalRequest.url().queryParameterValues(key).get(0));
                }

                if (originalRequest.body() != null && originalRequest.body().contentType() != null) {
                    MediaType mediaType = originalRequest.body().contentType();
                    if (mediaType.toString().equals("application/x-www-form-urlencoded")) {
                    } else if (mediaType.toString().equals("application/json; charset=UTF-8")) {
                        String jsonBody = bodyToString(originalRequest.body());
                        if (!TextUtils.isEmpty(jsonBody)) {
                            params = Md5Utils.json2HashMap(jsonBody);
                        }
                    }
                }
                if (BuildConfig.DEBUG) {
                    Log.e("token", TokenUtils.getToken());
                }
                Request.Builder requestBuilder = originalRequest.newBuilder()
                        .addHeader("timestamp", currentTimeMillis)
                        .addHeader("nonce", randomNumber)
                        .addHeader("token", TokenUtils.getToken())
                        // TODO: 2018/8/29 replace your service secretKey
                        .addHeader("signature", Md5Utils.getsignature(currentTimeMillis, randomNumber, "", params))
                        .method(originalRequest.method(), originalRequest.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }

            private String bodyToString(final RequestBody request) {
                try {
                    final RequestBody copy = request;
                    final Buffer buffer = new Buffer();
                    if (copy != null)
                        copy.writeTo(buffer);
                    else
                        return "";
                    return buffer.readUtf8();
                } catch (final IOException e) {
                    return "did not work";
                }
            }
        };
    }

    /**
     * Init HttpLoggingInterceptor
     * 网络请求日志拦截器
     *
     * @return HttpLoggingInterceptor
     */
    static HttpLoggingInterceptor buildHttpLoggingInterceptor() {
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NonNull String message) {
                Log.w(TAG, message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    /**
     * 获取随机数
     */
    public static String getRandomNumber() {
        return String.valueOf(((int) ((Math.random() * 9 + 1) * 100000)));
    }
}
