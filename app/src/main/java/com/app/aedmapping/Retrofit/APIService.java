package com.app.aedmapping.Retrofit;


import com.app.aedmapping.Retrofit.Defibrillator.CreateDefibrillatorRequest;
import com.app.aedmapping.Retrofit.Defibrillator.Defibrillator;
import com.app.aedmapping.Retrofit.Geocoder.CreateGeocoderResponse;
import com.app.aedmapping.Retrofit.Report.CreateReportRequest;
import com.app.aedmapping.Retrofit.Report.CreateReportResponse;
import com.app.aedmapping.Retrofit.User.CreateLoginRequest;
import com.app.aedmapping.Retrofit.User.CreateLoginResponse;
import com.app.aedmapping.Retrofit.User.CreateUserRequest;
import com.app.aedmapping.Retrofit.User.CreateUserResponse;
import com.app.aedmapping.Retrofit.User.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    //USER
    @GET("/api/user/findAll.php")
    Call<List<User>> findAllUsers();

    @POST("/api/user/add.php")
    Call<CreateUserResponse> addUser(@Body CreateUserRequest request);

    @POST("/api/user/login.php")
    Call<CreateLoginResponse> loginUser(@Body CreateLoginRequest request);

    //DEFIBRILLATOR
    @GET("/api/defib/findAll.php")
    Call<List<Defibrillator>> findAllDefibrillators();

    @POST("/api/defib/add.php")
    Call<CreateUserResponse> addDefib(@Body CreateDefibrillatorRequest request);

    @GET("/api/defib/find.php?id={id}")
    Call<Defibrillator> findDefib(@Path("id") Integer id);

    //REPORT
    @POST("/api/report/add.php")
    Call<CreateReportResponse> addReport(@Body CreateReportRequest request);

    @GET("json?")
    Call<CreateGeocoderResponse> getAddress(@Query("latlng") String latCommaLong, @Query("key") String apiKey);

}
