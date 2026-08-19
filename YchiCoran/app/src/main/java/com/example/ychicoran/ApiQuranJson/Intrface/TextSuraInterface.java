package com.example.ychicoran.ApiQuranJson.Intrface;

import com.example.ychicoran.ApiQuranJson.Classes.SuraText;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TextSuraInterface {
    /**
     * Получить список сур.
     * @param langCode Код языка (например, "ru", "ar", "en")
     */
    @GET("{langCode}/{chapterNumber}.json")
    Call<List<SuraText>> getTextSura(@Path("langCode") String langCode, @Path("chapterNumber") String chapterNumber);

}
