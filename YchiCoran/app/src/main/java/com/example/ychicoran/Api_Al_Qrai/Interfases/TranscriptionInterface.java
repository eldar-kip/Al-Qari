package com.example.ychicoran.Api_Al_Qrai.Interfases;

import com.example.ychicoran.Api_Al_Qrai.Class.Text.TranscriptionSura;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TranscriptionInterface {
    @GET("quran/transcription/{riwayah_type}/{lang}")
    Call<List<TranscriptionSura>> getTranscription(@Path("riwayah_type") String riwayahType, @Path("lang") String lang);
}
