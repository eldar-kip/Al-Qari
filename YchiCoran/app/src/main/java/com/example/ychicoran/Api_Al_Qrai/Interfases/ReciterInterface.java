package com.example.ychicoran.Api_Al_Qrai.Interfases;

import com.example.ychicoran.Api_Al_Qrai.Class.Reciters.Reciter;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ReciterInterface {
    @GET ("metadata/reciters")
    Call<Reciter> getRecitrList();

}
