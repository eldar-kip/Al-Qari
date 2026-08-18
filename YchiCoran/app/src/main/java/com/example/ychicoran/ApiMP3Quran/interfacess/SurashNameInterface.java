package com.example.ychicoran.ApiMP3Quran.interfacess;

import com.example.ychicoran.ApiMP3Quran.model.Surash;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SurashNameInterface {
    /**
     * Получить список сур.
     * @param language Код языка (например, "ru", "ar", "en")
     */
    @GET("suwar")
    Call<Surash> getSuraw(@Query("language") String language);
}
