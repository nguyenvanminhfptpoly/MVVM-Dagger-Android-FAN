package com.minhnv.dagger2androidpro.data.remote;

import com.minhnv.dagger2androidpro.data.model.HomeStay;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class AppApiHelper implements ApiHelper {

    @Inject
    public AppApiHelper() {
    }

    //use fast android networking to request to Server
    @Override
    public Observable<List<HomeStay>> doLoadHomeStay() {
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_LIST_HOMESTAYS)
                .build()
                .getObjectListObservable(HomeStay.class);
    }
}
