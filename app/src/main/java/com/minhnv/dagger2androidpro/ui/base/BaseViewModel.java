package com.minhnv.dagger2androidpro.ui.base;

import android.app.ProgressDialog;

import androidx.lifecycle.ViewModel;

import com.minhnv.dagger2androidpro.data.DataManager;
import com.minhnv.dagger2androidpro.utils.rx.ScheduleProvider;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseViewModel<N> extends ViewModel {
    private final DataManager dataManager;

    private final ScheduleProvider schedulerProvider;

    private CompositeDisposable compositeDisposable;

    private ProgressDialog progressDialog;

    //interface with weakReference
    private WeakReference<N> mNavigator;
    protected BaseViewModel(DataManager dataManager, ScheduleProvider schedulerProvider) {
        this.dataManager = dataManager;
        this.schedulerProvider = schedulerProvider;
        this.compositeDisposable = new CompositeDisposable();
    }

    protected N getNavigator(){
        return mNavigator.get();
    }

    public void setNavigator(N navigator){
        this.mNavigator = new WeakReference<>(navigator);
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }

    public CompositeDisposable getCompositeDisposable(){
        return compositeDisposable;
    }

    public DataManager getDataManager(){
        return dataManager;
    }

    public ScheduleProvider getSchedulerProvider() {
        return schedulerProvider;
    }
}
