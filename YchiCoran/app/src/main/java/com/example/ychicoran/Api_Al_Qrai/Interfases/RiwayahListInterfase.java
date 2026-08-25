package com.example.ychicoran.Api_Al_Qrai.Interfases;

import com.example.ychicoran.Api_Al_Qrai.Class.RiwayahListClass;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RiwayahListInterfase {
    @GET ("metadata/riwayah")
    Call<List<String>> getRiwayahList();

}
