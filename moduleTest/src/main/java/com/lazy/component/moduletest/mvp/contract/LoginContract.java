package com.lazy.component.moduletest.mvp.contract;

import com.lazy.component.base.contract.BaseContractPresenter;
import com.lazy.component.base.contract.BaseView;

import retrofit2.Retrofit;

/**
  * @author :lazyMing
  * email   :407555147@qq.com
  * date    :2018/11/21
  * desc    :
  * address :
  * update  :
  */
public interface LoginContract {
    interface Model {

        void testRequest(Retrofit retrofit);
    }

    interface View extends BaseView {
    }

    interface Presenter extends BaseContractPresenter {
    }
}
