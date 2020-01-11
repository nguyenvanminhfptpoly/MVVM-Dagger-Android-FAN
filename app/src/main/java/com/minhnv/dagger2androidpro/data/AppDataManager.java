package com.minhnv.dagger2androidpro.data;

import android.content.Context;

import com.google.gson.Gson;
import com.minhnv.dagger2androidpro.data.model.HomeStay;
import com.minhnv.dagger2androidpro.data.model.HomestayRequest;
import com.minhnv.dagger2androidpro.data.remote.ApiHelper;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class AppDataManager implements DataManager {
    private ApiHelper apiHelper;
    private Context context;
    private Gson gson;

    @Inject
    public AppDataManager(ApiHelper apiHelper, Context context, Gson gson) {
        this.apiHelper = apiHelper;
        this.context = context;
        this.gson = gson;
    }

    @Override
    public Observable<List<HomeStay>> doLoadHomeStay() {
        return apiHelper.doLoadHomeStay();
    }

    @Override
    public Observable<String> doDeleteHomestay(HomestayRequest.ServerDeleteHomeStays id) {
        return apiHelper.doDeleteHomestay(id);
    }
}
