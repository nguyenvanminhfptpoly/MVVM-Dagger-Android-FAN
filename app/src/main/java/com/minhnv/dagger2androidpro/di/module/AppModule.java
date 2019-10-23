package com.minhnv.dagger2androidpro.di.module;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.minhnv.dagger2androidpro.BaseApplication;
import com.minhnv.dagger2androidpro.data.AppDataManager;
import com.minhnv.dagger2androidpro.data.DataManager;
import com.minhnv.dagger2androidpro.data.remote.ApiHelper;
import com.minhnv.dagger2androidpro.data.remote.AppApiHelper;
import com.minhnv.dagger2androidpro.utils.rx.AppScheduleProvider;
import com.minhnv.dagger2androidpro.utils.rx.ScheduleProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application;
    }

    @Provides
    @Singleton
    ApiHelper provideApiHelper(AppApiHelper appApiHelper) {
        return appApiHelper;
    }

    @Provides
    @Singleton
    DataManager provideDataManager(AppDataManager appDataManager) {
        return appDataManager;
    }



    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder().setLenient().create();
    }

    @Provides
    ScheduleProvider provideSchedulerProvider() {
        return new AppScheduleProvider();
    }
}
