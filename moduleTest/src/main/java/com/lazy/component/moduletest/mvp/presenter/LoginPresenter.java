package com.lazy.component.moduletest.mvp.presenter;

import com.lazy.component.base.BasePresenterImpl;
import com.lazy.component.moduletest.mvp.contract.LoginContract;

import retrofit2.Retrofit;

/**
 * @author :lazyMing
 * email   :407555147@qq.com
 * date    :2018/11/21
 * desc    :
 * address :
 * update  :
 */
public class LoginPresenter extends BasePresenterImpl<LoginContract.View> implements LoginContract.Presenter {

    private LoginContract.View mView;
    private LoginContract.Model mModel;
    private Retrofit mRetrofit;


    public LoginPresenter(LoginContract.View view, LoginContract.Model model, Retrofit retrofit) {
        super(view);
        mView = view;
        mModel = model;
        mRetrofit = retrofit;
    }


    @Override
    public void recyclerRes() {
        super.detach();
    }
}
