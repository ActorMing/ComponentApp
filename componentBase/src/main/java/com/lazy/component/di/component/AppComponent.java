package com.lazy.component.di.component;

import android.app.Application;

import com.lazy.component.di.module.ApiBaseServerModule;
import com.lazy.component.di.module.AppModule;
import com.lazy.component.retrofit.RetrofitService;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * @author :lazyMing
 * email   :407555147@qq.com
 * date    :2018/11/21
 * desc    : application component
 * address :
 * update  :
 */
@Singleton
@Component(modules = {AppModule.class, ApiBaseServerModule.class})
public interface AppComponent {

    /**
     * 注解 application 对象,提供 application 的 context 对象
     *
     * @param app
     */
    void inject(Application app);

    /**
     * 向外部提供全局为唯一的 retrofit 对象,方便之后的网络请求
     *
     * @return retrofit 对象
     */
    Retrofit getRetrofit();

    /**
     * 向外部提供全局唯一的 retrofitService 对象,方便请求接口的直接访问
     *
     * @return retrofitService 数据请求接口对象
     */
    RetrofitService getRetrofitService();
}
