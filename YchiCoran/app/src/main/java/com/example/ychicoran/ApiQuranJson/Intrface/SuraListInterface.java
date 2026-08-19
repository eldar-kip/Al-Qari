package com.example.ychicoran.ApiQuranJson.Intrface;

import com.example.ychicoran.ApiQuranJson.Classes.SuraList;
import com.example.ychicoran.ApiQuranJson.Classes.SuraText;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SuraListInterface {
        /**
         * Получить список сур.
         * @param langCode Код языка (например, "ru", "ar", "en")
         */
        @GET("{langCode}/index.json")
        Call<List<SuraList>> getListSurashes(@Path("langCode") String langCode);
}
