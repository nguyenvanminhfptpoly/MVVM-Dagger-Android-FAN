package com.minhnv.dagger2androidpro.di.builder.main;

import com.minhnv.dagger2androidpro.ui.main.demo_fragment.BlankFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBuilder {

    @ContributesAndroidInjector
    abstract BlankFragment blankFragment();
}
