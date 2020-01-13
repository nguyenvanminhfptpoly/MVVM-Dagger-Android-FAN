package com.minhnv.dagger2androidpro.di.builder;

import com.minhnv.dagger2androidpro.di.builder.main.MainFragmentBuilder;
import com.minhnv.dagger2androidpro.ui.main.MainActivity;
import com.minhnv.dagger2androidpro.ui.main.add.AddHsActivity;
import com.minhnv.dagger2androidpro.ui.main.edit.EditActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {
    @ContributesAndroidInjector(
            modules = MainFragmentBuilder.class
    )
    abstract MainActivity mainActivity();

    @ContributesAndroidInjector
    abstract AddHsActivity addHsActivity();

    @ContributesAndroidInjector
    abstract EditActivity editActivity();
}
