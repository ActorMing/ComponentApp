package com.lazy.component.retrofit;


import android.text.TextUtils;

import com.lazy.component.base.BasePresenter;
import com.lazy.component.base.BaseResponse;
import com.lazy.component.base.BaseView;
import com.lazy.component.constant.ApiStatusCode;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author :zdxiang
 * email   :407555147@qq.com
 * date    :2018/8/29
 * desc    :baseObserver
 * address :
 * update  :
 */
public abstract class BaseObserver<T> implements Observer<BaseResponse<T>> {

    private BasePresenter presenter;

    private BaseView view;

    public BaseObserver(BasePresenter presenter, BaseView view) {
        this.presenter = presenter;
        this.view = view;
    }

    public BaseObserver() {
    }


    @Override
    public void onSubscribe(Disposable d) {
        if (presenter != null) {
            presenter.addDisposable(d);
        }
        if (view != null) {
            view.showLoadingDialog("");
        }
    }

    @Override
    public void onNext(BaseResponse<T> baseResponse) {
        if (view != null) {
            view.dismissLoadingDialog();
        }
        if (baseResponse.isSuccess()) {
            onSuccess(baseResponse.getData());
        } else {
            onFailed(baseResponse);
        }
    }

    @Override
    public void onError(Throwable e) {
        if (view != null) {
            view.dismissLoadingDialog();
            view.showTip(ExceptionHelper.handleException(e));
        }
    }

    @Override
    public void onComplete() {
    }

    protected abstract void onSuccess(T t);

    /**
     * Processing failure
     *
     * @param baseResponse BaseResponse
     */
    public void onFailed(BaseResponse baseResponse) {
        handleCode(baseResponse.getStatusCode(), baseResponse.getMessage());
    }

    /**
     * 处理API返回状态码情况。
     * 以下情况均为非返回成功的处理
     *
     * @param code {@link ApiStatusCode}
     */
    private void handleCode(int code, String msg) {
        if (view == null || TextUtils.isEmpty(msg)) return;
        switch (code) {
            case ApiStatusCode.FAILURE:
                view.showTip(msg);
                break;

            case ApiStatusCode.TOKEN_EXPIRED:
                view.showTip(msg);
                view.onSessionOut();
                break;

            case ApiStatusCode.UNION_ID_BIND:
                view.showTip(msg);
                break;

            case ApiStatusCode.LOGIN_OUT:
                view.showTip(msg);
                view.onSessionOut();
                break;

            case ApiStatusCode.PAY_SUCCESS:
                view.showTip(msg);
                break;

            default:
                view.showTip(msg);
                break;
        }
    }
}
