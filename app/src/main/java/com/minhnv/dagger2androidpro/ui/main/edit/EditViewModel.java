package com.minhnv.dagger2androidpro.ui.main.edit;

import com.minhnv.dagger2androidpro.data.DataManager;
import com.minhnv.dagger2androidpro.ui.base.BaseViewModel;
import com.minhnv.dagger2androidpro.utils.rx.ScheduleProvider;

public class EditViewModel extends BaseViewModel<EditNavigator> {
    public EditViewModel(DataManager dataManager, ScheduleProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }
}
