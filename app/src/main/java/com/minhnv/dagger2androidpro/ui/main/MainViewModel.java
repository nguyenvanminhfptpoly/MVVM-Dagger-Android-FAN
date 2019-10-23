package com.minhnv.dagger2androidpro.ui.main;

import android.util.Log;

import com.minhnv.dagger2androidpro.data.DataManager;
import com.minhnv.dagger2androidpro.data.model.HomeStay;
import com.minhnv.dagger2androidpro.ui.base.BaseViewModel;
import com.minhnv.dagger2androidpro.utils.rx.ScheduleProvider;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class MainViewModel extends BaseViewModel<MainNavigator> {
    private static final String TAG = "MainViewModel";
    //It emits all the items of the source Observable, regardless of when the subscriber subscribes to activity
    public PublishSubject<List<HomeStay>>listPublishSubject = PublishSubject.create();
    public MainViewModel(DataManager dataManager, ScheduleProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        Observable<List<HomeStay>> listObservable = listPublishSubject.share();
    }

    public void loadList(){
        getCompositeDisposable().add(
                getDataManager().doLoadHomeStay()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    listPublishSubject.onNext(response);
                    getNavigator().onSuccess();
                },throwable -> {
                    getNavigator().onError(throwable);
                    Log.d(TAG, "loadList: "+throwable);
                })
        );
    }

    //callback on Activity
    public void ServerLoad(){
        getNavigator().ServerLoadList();
    }
}
