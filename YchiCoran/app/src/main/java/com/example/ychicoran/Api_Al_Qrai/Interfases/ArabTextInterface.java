package com.example.ychicoran.Api_Al_Qrai.Interfases;

import com.example.ychicoran.Api_Al_Qrai.Class.Text.ArabText;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ArabTextInterface {
    @GET("quran/riwayah/{riwayah_type}")
    Call<List<ArabText>> getArabText(@Path("riwayah_type") String riwayahType);
}
