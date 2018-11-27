package com.lazy.component.base;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.WindowManager;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.lazy.component.base.contract.BaseView;
import com.lazy.component.util.TokenUtils;
import com.lazy.component.widget.LoadingDialog;
import com.vondear.rxtool.RxActivityTool;
import com.vondear.rxtool.view.RxToast;

/**
 * <p>BaseActivity </p>
 *
 * @author zdxiang
 * @date 2018-07-27
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView {

    protected ImmersionBar mImmersionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }
        init(savedInstanceState);
        RxActivityTool.addActivity(this);
    }

    /**
     * detach view
     */
    protected abstract void detach();

    /**
     * 获取布局资源id
     *
     * @return 资源id
     */
    protected abstract int getLayoutId();

    /**
     * Initialization
     *
     * @param savedInstanceState savedInstanceState
     */
    protected abstract void init(@Nullable Bundle savedInstanceState);

    /**
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    protected void initImmersionBar() {
        /**
         * 解决软件盘无法关闭问题
         * https://github.com/gyf-dev/ImmersionBar/issues/154
         SOFT_INPUT_ADJUST_NOTHING:         不调整(输入法完全直接覆盖住,未开放此参数)
         SOFT_INPUT_ADJUST_PAN:                 把整个Layout顶上去露出获得焦点的EditText,不压缩多余空间，见图1
         SOFT_INPUT_ADJUST_RESIZE:            整个Layout重新编排,重新分配多余空间，见图2
         SOFT_INPUT_ADJUST_UNSPECIFIED:  系统自己根据内容自行选择上两种方式的一种执行(默认配置)
         https://blog.csdn.net/achellies/article/details/7034756
         */
        mImmersionBar = ImmersionBar.with(this)
                .keyboardEnable(false)
                .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mImmersionBar.statusBarDarkFont(true, 0.2f).init();
    }

    @Override
    public void showLoadingDialog(String msg) {
        LoadingDialog.with(this).show();
    }

    @Override
    public void dismissLoadingDialog() {
        LoadingDialog.with(this).dismiss();
    }

    @Override
    public void showTip(String msg) {
        RxToast.showToast(this, msg, Toast.LENGTH_SHORT);
    }

    @Override
    public void onSessionOut() {
        TokenUtils.clearToken(this);
        // TODO: 2018/8/29 设置自己的session退出
//        LoginActivity.start(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxActivityTool.finishActivity(this);
        detach();

        // destroy the  mImmersionBar
        if (mImmersionBar != null) {
            mImmersionBar.destroy();
        }

        LoadingDialog.with(this).dismiss();
    }
}