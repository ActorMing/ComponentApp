package com.lazy.component.arouter;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * @author :lazyMing
 * email   :407555147@qq.com
 * date    :2018/8/30
 * desc    :路由配置
 * address :
 * update  :
 */
public class RouterConfig {

    public static void init(Application app, boolean isDebug) {
        if (isDebug) {
            // 打印日志
            ARouter.openLog();
            // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openDebug();
        }
        // 尽可能早，推荐在Application中初始化
        ARouter.init(app);
    }
}
