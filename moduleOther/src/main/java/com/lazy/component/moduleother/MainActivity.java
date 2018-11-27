package com.lazy.component.moduleother;

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lazy.component.arouter.RouterConstants;
import com.lazy.component.base.BaseActivity;

/**
 * @author :lazyMing
 * email   :407555147@qq.com
 * date    :2018/8/30
 * desc    :otherModule MainActivity (测试ModuleTest跳转的module Activity)
 * address :
 * update  :
 */
@Route(path = RouterConstants.SHOP_MALL_HOME)
public class MainActivity extends BaseActivity {
    @Override
    protected void detach() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {

    }
}
