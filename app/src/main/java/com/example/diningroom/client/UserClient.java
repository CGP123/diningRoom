package com.example.diningroom.client;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface UserClient {
    @GET("basic")
    Call<Void> getUser(@Header("Authorization") String authHeader);

    @GET("getdata/references")
    Call<Directory> getDirectory(@Header("Authorization") String authHeader, @Query("imei") String imei);

    @GET("getdata/distribution")
    Call<List<Distribution>> getDistribution(@Header("Authorization") String authHeader);

    @PUT("cfmdata")
    Call<Void> putDirectory(@Header("Authorization") String authHeader, @Query("imei") String imei, @Query("messagenumber") int messageNumber);

    @POST("senddata")
    Call<Void> postReport(@Header("Authorization") String authHeader, @Body List<BoardingDocument> boardingDocument);

    @GET("GetData")
    Call<PassData> getPassData(@Query("imei") String imei);

    @POST("ProcessedGUIDs")
    Call<Void> postProcessedGUIDs(@Body ProcessedGUIDs processedGUIDs);
}
