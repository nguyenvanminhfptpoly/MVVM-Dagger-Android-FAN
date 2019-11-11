package com.minhnv.dagger2androidpro;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.minhnv.dagger2androidpro.data.DataManager;
import com.minhnv.dagger2androidpro.ui.main.MainViewModel;
import com.minhnv.dagger2androidpro.ui.main.demo_fragment.BlankFragment;
import com.minhnv.dagger2androidpro.ui.main.demo_fragment.BlankViewModel;
import com.minhnv.dagger2androidpro.utils.rx.ScheduleProvider;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ViewModelProviderFactory extends ViewModelProvider.NewInstanceFactory {
    private final DataManager dataManager;
    private final ScheduleProvider scheduleProvider;

    @Inject
    public ViewModelProviderFactory(DataManager dataManager, ScheduleProvider scheduleProvider) {
        this.dataManager = dataManager;
        this.scheduleProvider = scheduleProvider;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(MainViewModel.class)){
            return (T) new MainViewModel(dataManager,scheduleProvider);
        }else if(modelClass.isAssignableFrom(BlankViewModel.class)){
            return (T) new BlankViewModel(dataManager,scheduleProvider);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
