package com.example.ychicoran.Api_Al_Qrai.Interfases;

import com.example.ychicoran.Api_Al_Qrai.Class.Text.TasfirText;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TasfirTextInterface {
    @GET("/quran/tafsir/{edition}/{surah_id}")
    Call<TasfirText> getTasfirText(
            @Path("edition") String edition,
            @Path("surah_id") int surahId
    );
}
