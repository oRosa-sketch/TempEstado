package com.faculdade.tempestado.network;

import com.faculdade.tempestado.model.PrevisaoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface HgBrasilApiService {


    @GET("weather")
    Call<PrevisaoResponse> getPrevisao(
            @Query("key") String apiKey,
            @Query("city_name") String cidade
    );
}