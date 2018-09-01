package com.lazy.component.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.support.multidex.BuildConfig;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.lazy.component.arouter.RouterConfig;
import com.lazy.component.util.SharedPreferencesUtils;
import com.lazy.component.util.Utils;

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
    public static Context context;

    public static BaseApp getIns() {
        return mBaseApplication;
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

        RouterConfig.init(this, com.lazy.component.commpoentbase.BuildConfig.DEBUG);

        mBaseApplication = this;
        context = this.getApplicationContext();

        Utils.init(this);
        if (inMainProcess()) {
            SharedPreferencesUtils.init(this);
            // 数据储存
            // DataKeeper.init(this);
        }

        setRxErrorHandler();
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

    public boolean inMainProcess() {
        String packageName = getPackageName();
        String processName = getProcessName(this);
        return packageName.equals(processName);
    }

    public String getProcessName(Context context) {
        String processName = null;

        // ActivityManager
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));

        while (true) {
            for (ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()) {
                if (info.pid == android.os.Process.myPid()) {
                    processName = info.processName;
                    break;
                }
            }

            if (!TextUtils.isEmpty(processName)) {
                return processName;
            }

            try {
                Thread.sleep(100L);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }


}
