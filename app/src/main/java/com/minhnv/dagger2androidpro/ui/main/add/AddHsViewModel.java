package com.minhnv.dagger2androidpro.ui.main.add;

import com.minhnv.dagger2androidpro.data.DataManager;
import com.minhnv.dagger2androidpro.ui.base.BaseViewModel;
import com.minhnv.dagger2androidpro.utils.rx.ScheduleProvider;

public class AddHsViewModel extends BaseViewModel<AddHsNavigator> {
    public AddHsViewModel(DataManager dataManager, ScheduleProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }
}
