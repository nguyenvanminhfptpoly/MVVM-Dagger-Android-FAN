package com.minhnv.dagger2androidpro.utils;


import com.minhnv.dagger2androidpro.data.remote.DataClient;
import com.minhnv.dagger2androidpro.data.remote.RetrofitClient;

public class ApiUtils {
    public static final String baseUrl = "https://luxuryhomestay.000webhostapp.com/";

    public static DataClient getData(){
        return RetrofitClient.getRetrofit(baseUrl).create(DataClient.class);
    }
}
