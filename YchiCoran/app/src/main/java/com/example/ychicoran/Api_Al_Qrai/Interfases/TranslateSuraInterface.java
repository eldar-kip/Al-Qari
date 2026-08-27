package com.example.ychicoran.Api_Al_Qrai.Interfases;

import com.example.ychicoran.Api_Al_Qrai.Class.Text.TranslateSura;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TranslateSuraInterface {
    @GET("quran/translation/{lang}")
    Call<List<TranslateSura>> getTranslate(@Path("lang") String lang);
}
