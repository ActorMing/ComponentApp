package com.lazy.component.moduletest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.lazy.component.arouter.RouterConstants;
import com.lazy.component.base.BaseActivity;
import com.lazy.component.base.BaseApp;
import com.lazy.component.moduletest.bean.Student;
import com.lazy.component.moduletest.di.component.DaggerLoginComponent;
import com.lazy.component.moduletest.di.module.LoginModule;
import com.lazy.component.moduletest.mvp.contract.LoginContract;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author :lazyMing
 * email   :407555147@qq.com
 * date    :2018/8/30
 * desc    :loginActivity
 * address :
 * update  :
 */
public class LoginActivity extends BaseActivity implements LoginContract.View {

    private EditText etName, etPassword;
    private Button btnLogin, btnVerify, btnRequest;

    private CompositeDisposable mDisposable = new CompositeDisposable();

    @Inject
    Student mStudent;

    @Inject
    LoginContract.Presenter mPresenter;

    @Override
    protected void detach() {
        mPresenter.recyclerRes();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_loginctivity;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        initViews();
        initEvents();
        rxEditText();
        countdown();
        initDagger();
        Log.e("login", mStudent.toString());
        Log.e("login", mPresenter == null ? "null" : "not null");
    }

    private void initDagger() {
        DaggerLoginComponent.builder()
                .appComponent(BaseApp.getAppComponent())
                .loginModule(new LoginModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    private void initViews() {
        etName = findViewById(R.id.et_name);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnVerify = findViewById(R.id.btn_verify);
        btnRequest = findViewById(R.id.btn_request);
    }

    private void initEvents() {
    }

    private void countdown() {
        mDisposable.add(RxView.clicks(btnVerify)
                // 防止20秒内连续点击,或者只使用doOnNext部分
                .throttleFirst(20, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnNext(o -> {
                    btnVerify.setEnabled(false);
                })
                .subscribe(o -> {
                    Observable.interval(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                            .take(20)
                            .subscribe(new Observer<Long>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(Long value) {
                                    btnVerify.setText("剩余" + (20 - value) + "秒");
                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onComplete() {
                                    btnVerify.setText("获取验证码");
                                    btnVerify.setEnabled(true);
                                }
                            });
                }));
    }

    @SuppressLint("CheckResult")
    private void rxEditText() {
        Observable.combineLatest(
                RxTextView.textChanges(etName)
                        .map(charSequence -> String.valueOf(charSequence)),
                RxTextView.textChanges(etPassword)
                        .map(charSequence -> String.valueOf(charSequence)),
                (s, s2) -> isNameValid(s) && isPasswordValid(s2))
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        btnLogin.setEnabled(true);
                        btnLogin.setBackgroundResource(R.drawable.red_bg);
                        RxView.clicks(btnLogin)
                                .throttleFirst(2, TimeUnit.SECONDS)
                                .subscribe(o -> {
                                    ARouter.getInstance()
                                            .build(RouterConstants.SHOP_MALL_HOME)
                                            .navigation(this, new NavigationCallback() {
                                                @Override
                                                public void onFound(Postcard postcard) {
                                                    Toast.makeText(LoginActivity.this, "onFound", Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onLost(Postcard postcard) {
                                                    Toast.makeText(LoginActivity.this, "onLost", Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onArrival(Postcard postcard) {
                                                    Toast.makeText(LoginActivity.this, "onArrival", Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onInterrupt(Postcard postcard) {
                                                    Toast.makeText(LoginActivity.this, "onInterrupt", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                });
                    } else {
                        btnLogin.setEnabled(false);
                        btnLogin.setBackgroundResource(R.drawable.gray_bg);
                    }
                });
    }

    private boolean isNameValid(String name) {
        return name.equalsIgnoreCase("lazy");
    }

    private boolean isPasswordValid(String pwd) {
        return pwd.equalsIgnoreCase("123");
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
