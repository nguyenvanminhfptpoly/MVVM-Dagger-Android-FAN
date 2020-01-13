package com.minhnv.dagger2androidpro.data.remote;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface DataClient {
    @Multipart
    @POST("uploadimage.php")
    Call<String> UploadPhot(@Part MultipartBody.Part photo);

    @FormUrlEncoded
    @POST("listratingpost.php")
    Call<String> insertData(@Field("title") String title,
                            @Field("image") String image,
                            @Field("address") String address,
                            @Field("rating") String rating,
                            @Field("price") String price,
                            @Field("idhomestays") String idhomestay,
                            @Field("hastag") String hastag,
                            @Field("priceago") String priceago);

    @FormUrlEncoded
    @POST("updateHomestayRating.php")
    Call<String> editData(@Field("id") String id,
                            @Field("title") String title,
                            @Field("image") String image,
                            @Field("address") String address,
                            @Field("rating") String rating,
                            @Field("price") String price,
                            @Field("idhomestays") String idhomestay,
                            @Field("hastag") String hastag,
                            @Field("priceago") String priceago);

    @FormUrlEncoded
    @POST("storypost.php")
    Call<String> postStory(@Field("title") String title,
                           @Field("image") String image);
}
