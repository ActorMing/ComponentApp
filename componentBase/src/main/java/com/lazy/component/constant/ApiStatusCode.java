package com.lazy.component.constant;

/**
  * @author :lazyMing
  * email   :407555147@qq.com
  * date    :2018/8/28
  * desc    :API 请求状态码常量
  * address :
  * update  :
  */
public interface ApiStatusCode {
    /**
     * 成功
     */
    int SUCCESS = 1;

    /**
     * FAILURE
     */
    int FAILURE = 2;

    /**
     * Token失效，session out
     */
    int TOKEN_EXPIRED = 3;

    /**
     * 微信需要绑定手机号码
     * TODO: 2018/8/1/001 使用情况未知
     */
    int UNION_ID_BIND = 4;

    /**
     * 退出登录
     * TODO: 2018/8/1/001 使用情况未知
     */
    int LOGIN_OUT = 5;

    /**
     * 支付成功
     * TODO: 2018/8/1/001 使用情况未知
     */
    int PAY_SUCCESS = 6;
    /**
     * 基本许可请求代码
     * TODO: 2018/8/1/001 使用情况未知
     */
    int BASIC_PERMISSION_REQUEST_CODE = 110;
}
