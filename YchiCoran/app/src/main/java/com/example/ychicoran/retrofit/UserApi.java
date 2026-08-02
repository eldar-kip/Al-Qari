package com.example.ychicoran.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserApi {
    @GET("/user/get-all")
    Call<List<User>> getAllUsers();

}
