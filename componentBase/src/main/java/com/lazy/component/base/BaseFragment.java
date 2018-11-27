package com.lazy.component.base;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lazy.component.base.contract.BaseView;
import com.lazy.component.widget.LoadingDialog;
import com.vondear.rxtool.view.RxToast;


/**
 * <p> </p>
 *
 * @author zdxiang
 * @date 2018-07-27 15:15
 */
public abstract class BaseFragment<P extends BasePresenter> extends BaseLazyFragment implements BaseView {

    protected P Presenter;

    protected View rootView;

    /**
     * 是否需要加载loading对话框
     * default is true
     */
    protected boolean needShowLoadingDialog = true;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Presenter = createPresenter();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutId(), container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setIfNeedShowLoadingDialog(needShowLoadingDialog);
        init(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (Presenter != null) {
            Presenter.unDisposable();
        }
    }

    /**
     * Return the layout id
     *
     * @return integer
     */
    protected abstract int getLayoutId();

    /**
     * Create the presenter
     *
     * @return the mPresenter extents BasePresenter{@link BasePresenterImpl}
     */
    protected abstract P createPresenter();


    public void init(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }


    /**
     * 是否需要显示loading对话框
     *
     * @param needShowLoadingDialog boolean
     */
    public void setIfNeedShowLoadingDialog(boolean needShowLoadingDialog) {
        this.needShowLoadingDialog = needShowLoadingDialog;
    }

    @Override
    public void showLoadingDialog(String msg) {
        if (needShowLoadingDialog) {
            LoadingDialog.with(mContext).show();
        }
    }

    @Override
    public void dismissLoadingDialog() {
        LoadingDialog.with(mContext).dismiss();
    }

    @Override
    public void showTip(String msg) {
        RxToast.showToast(mContext, msg, Toast.LENGTH_SHORT);
    }

    @Override
    public void onSessionOut() {
        if (mContext instanceof BaseActivity) {
            ((BaseActivity) mContext).onSessionOut();
        }
    }


    public FragmentManager getSupportFragmentManager() {
        if (mContext instanceof BaseActivity) {
            return ((BaseActivity) mContext).getSupportFragmentManager();
        } else {
            return null;
        }
    }

    public void finish() {
        if (getActivity() != null && !getActivity().isFinishing()) {
            getActivity().finish();
        }
    }
}

