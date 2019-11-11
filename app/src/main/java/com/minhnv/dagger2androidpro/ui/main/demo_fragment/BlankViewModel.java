package com.minhnv.dagger2androidpro.ui.main.demo_fragment;

import androidx.lifecycle.ViewModel;

import com.minhnv.dagger2androidpro.data.DataManager;
import com.minhnv.dagger2androidpro.ui.base.BaseViewModel;
import com.minhnv.dagger2androidpro.utils.rx.ScheduleProvider;

public class BlankViewModel extends BaseViewModel<BlankFragmentNavigator> {
    public BlankViewModel(DataManager dataManager, ScheduleProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }
    // TODO: Implement the ViewModel
}
