package com.lazy.component.di.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * @author :lazyMing
 * email   :407555147@qq.com
 * date    :2018/11/20
 * desc    :application scope
 * address :
 * update  :
 */
@Scope
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ScopeApp {
}
