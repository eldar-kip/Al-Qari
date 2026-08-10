package com.example.ychicoran.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserApi {
    @GET("api/users/{id}")
    Call<User> getUserById(@Path("id") int id);
}
