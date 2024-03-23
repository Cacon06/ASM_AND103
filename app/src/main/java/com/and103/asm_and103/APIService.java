package com.and103.asm_and103;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIService {
    String DOMAIN = "http://192.168.253.180:3000/";

    @GET("/api/list")
    Call<List<CarModel>> getCars();

    @POST("/api/add_xe")
    Call<List<CarModel>> addXe(@Body CarModel car);
    @PUT("/api/update/{id}")
    Call<CarModel> update(@Path("id") String id, @Body CarModel car);
    @DELETE("/api/xoa/{id}")
    Call<CarModel> delete(@Path("id") String id);
}
