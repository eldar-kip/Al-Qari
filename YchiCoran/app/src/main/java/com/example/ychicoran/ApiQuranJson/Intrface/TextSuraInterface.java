package com.example.ychicoran.ApiQuranJson.Intrface;

import com.example.ychicoran.ApiQuranJson.Classes.SuraText;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TextSuraInterface {
    /**
     * Получить текст конкретной суры.
     * @param langCode Код языка (например, "ru", "en")
     * @param chapterNumber Номер суры
     */
    @GET("{langCode}/{chapterNumber}.json")
    Call<SuraText> getTextSura(@Path("langCode") String langCode, @Path("chapterNumber") String chapterNumber);
}
