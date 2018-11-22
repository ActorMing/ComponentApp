package com.lazy.component.moduletest.di.module;

import com.lazy.component.di.annotation.ScopeActivity;
import com.lazy.component.moduletest.LoginActivity;
import com.lazy.component.moduletest.bean.Student;
import com.lazy.component.moduletest.mvp.contract.LoginContract;
import com.lazy.component.moduletest.mvp.model.LoginModel;
import com.lazy.component.moduletest.mvp.presenter.LoginPresenter;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class LoginModule {

    private LoginContract.View mView;

    public LoginModule(LoginActivity activity) {
        this.mView = activity;
    }

    @Provides
    @ScopeActivity
    LoginContract.View providerView() {
        return mView;
    }

    @Provides
    @ScopeActivity
    LoginContract.Model providerModel() {
        return new LoginModel();
    }

    @Provides
    @ScopeActivity
    LoginContract.Presenter providerPresenter(LoginContract.View view, LoginContract.Model model, Retrofit retrofit) {
        return new LoginPresenter(view, model, retrofit);
    }

    @Provides
    @ScopeActivity
    Student providerStudent() {
        return new Student(1, "lazy", 11, "address");
    }

}
