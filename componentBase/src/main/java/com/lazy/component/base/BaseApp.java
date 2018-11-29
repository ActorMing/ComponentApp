package com.lazy.component.base;

import android.app.Application;
import android.content.Context;

import com.lazy.component.arouter.RouterConfig;
import com.lazy.component.config.LazyConfig;
import com.lazy.component.di.component.AppComponent;
import com.lazy.component.di.component.DaggerAppComponent;
import com.lazy.component.di.module.ApiBaseServerModule;
import com.lazy.component.di.module.AppModule;
import com.lazy.component.util.DensityUtils;
import com.lazy.component.util.SharedPreferencesUtils;
import com.lazy.component.util.Utils;

import androidx.multidex.MultiDex;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * <p> </p>
 *
 * @author zdxiang
 * @date 2018-07-27 15:09
 */
public class BaseApp extends Application {

    private static BaseApp mBaseApplication;
    private static AppComponent appComponent;
    public static Context context;

    public static BaseApp getIns() {
        return mBaseApplication;
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mBaseApplication = this;
        // MultiDex分包方法 必须最先初始化
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBaseApplication = this;
        RouterConfig.init(mBaseApplication, com.lazy.component.commpoentbase.BuildConfig.DEBUG);
        DensityUtils.setDensity(this);
        context = this.getApplicationContext();
        Utils.init(mBaseApplication);
        initDagger();
        initLazyConfig();
        SharedPreferencesUtils.getInstance().init(mBaseApplication);
        // 数据储存
        // DataKeeper.init(this);
    }

    private static void initDagger() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(mBaseApplication.getApplicationContext()))
                .apiBaseServerModule(new ApiBaseServerModule())
                .build();
        appComponent.inject(mBaseApplication);
    }

    private void initLazyConfig() {
        LazyConfig.init()
                .setBaseHttpUrl("http://www.baidu.com");
    }

    /**
     * Handling Network Error in RxJava 2
     * 处理RxJava2 networkException 不走onError的问题
     */
    private void setRxErrorHandler() {
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
            }
        });
    }
}
