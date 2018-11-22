package com.lazy.component.di.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author :lazyMing
 * email   :407555147@qq.com
 * date    :2018/11/20
 * desc    : application module
 * address :
 * update  :
 */
@Module
public class AppModule {

    private Context mContext;

    public AppModule(Context context) {
        mContext = context;
    }

    /**
     * 上下文对象
     *
     * @return context
     */
    @Provides
    @Singleton
    Context providerContent() {
        return mContext;
    }
}
