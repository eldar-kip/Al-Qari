package com.example.ychicoran.Api_Al_Qrai.Interfases;

import com.example.ychicoran.Api_Al_Qrai.Class.SuraAydio;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface SuraAudioInterface {
    @GET ("quran/audio/{riwayah_type}/{reciter_name}_{bitrate}/{surah_id}")
    Call<SuraAydio> getSuraAudio(
            @Path("riwayah_type") String riwayah_type,
            @Path("reciter_name") String reciter_name,
            @Path("bitrate") String bitrate,
            @Path("surah_id") String surah_id
    );
}
