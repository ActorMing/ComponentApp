package com.lazy.component.moduletest.di.component;

import com.lazy.component.di.annotation.ScopeActivity;
import com.lazy.component.di.component.AppComponent;
import com.lazy.component.moduletest.LoginActivity;
import com.lazy.component.moduletest.di.module.LoginModule;

import dagger.Component;

@ScopeActivity
@Component(modules = LoginModule.class, dependencies = AppComponent.class)
public interface LoginComponent {

    void inject(LoginActivity activity);
}
