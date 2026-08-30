package com.example.ychicoran.Api_Al_Qrai.Interfases;

import com.example.ychicoran.Api_Al_Qrai.Class.Timecode.SuraTimestamps;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TimestampsInterface {
    @GET("quran/audio/{riwayah_type}/{reciter_name}_{bitrate}/timestamps")
    Call<List<SuraTimestamps>> getSuraTimestamps(@Path("riwayah_type") String riwayahType, @Path("reciter_name") String reciterName, @Path("bitrate") String bitrate);
}
