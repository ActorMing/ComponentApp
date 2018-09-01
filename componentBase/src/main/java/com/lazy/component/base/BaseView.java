package com.lazy.component.base;

/**
 * Created on 2018-07-27
 *
 * @author zdxiang
 */
public interface BaseView {

    /**
     * show the loading dialog
     *
     * @param msg message
     */
    void showLoadingDialog(String msg);

    /**
     * dismiss the loading dialog
     */
    void dismissLoadingDialog();

    /**
     * show the toast
     *
     * @param msg message
     */
    void showTip(String msg);

    /**
     * 离线相关
     */
    void onSessionOut();
}
