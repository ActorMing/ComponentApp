package com.lazy.component.moduleother;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lazy.component.arouter.RouterConstants;

/**
 * @author :lazyMing
 * email   :407555147@qq.com
 * date    :2018/8/30
 * desc    :otherModule MainActivity (测试ModuleTest跳转的module Activity)
 * address :
 * update  :
 */
@Route(path = RouterConstants.SHOP_MALL_HOME)
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
