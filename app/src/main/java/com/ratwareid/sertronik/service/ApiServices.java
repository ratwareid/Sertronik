package com.ratwareid.sertronik.service;


import com.ratwareid.sertronik.model.response.ResponseRoute;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServices {

    @GET("json")
    Call<ResponseRoute> responseRouteCall(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("types") String type,
            @Query("sensor") boolean sensor,
            @Query("key") String api
    );
}
