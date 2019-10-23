package com.minhnv.dagger2androidpro.data.remote;

import com.minhnv.dagger2androidpro.data.model.HomeStay;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.Response;

public interface ApiHelper {
    //subscribe observable reactive programing
    Observable<List<HomeStay>> doLoadHomeStay();
}
