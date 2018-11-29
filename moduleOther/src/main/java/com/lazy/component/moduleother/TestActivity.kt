package com.lazy.component.moduleother

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.lazy.component.arouter.RouterConstants.SHOP_MALL_TEST
import com.lazy.component.base.BaseActivity
import com.lazy.component.base.BasePresenter

@Route(path = SHOP_MALL_TEST)
class TestActivity : BaseActivity<BasePresenter>() {
    override fun detach() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLayoutId() = R.layout.activity_test

    override fun init(savedInstanceState: Bundle?) {

    }

}