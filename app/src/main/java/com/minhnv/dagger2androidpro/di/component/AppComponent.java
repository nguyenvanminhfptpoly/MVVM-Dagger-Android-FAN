package com.minhnv.dagger2androidpro.di.component;

import android.app.Activity;
import android.app.Application;

import com.minhnv.dagger2androidpro.BaseApplication;
import com.minhnv.dagger2androidpro.di.builder.ActivityBuilder;
import com.minhnv.dagger2androidpro.di.module.AppModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component( modules = {AndroidSupportInjectionModule.class, ActivityBuilder.class, AppModule.class})
public interface AppComponent extends AndroidInjector<BaseApplication> {

    @Component.Builder
    interface Builder{
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
