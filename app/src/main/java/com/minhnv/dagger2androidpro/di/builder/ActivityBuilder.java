package com.minhnv.dagger2androidpro.di.builder;

import com.minhnv.dagger2androidpro.ui.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {
    @ContributesAndroidInjector
    abstract MainActivity mainActivity();
}
