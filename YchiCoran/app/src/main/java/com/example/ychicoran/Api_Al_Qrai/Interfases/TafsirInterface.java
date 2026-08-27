package com.example.ychicoran.Api_Al_Qrai.Interfases;

import com.example.ychicoran.Api_Al_Qrai.Class.Tafsir;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TafsirInterface {
    @GET("metadata/tafsir")
    Call<List<String>> getTafsir();
}
