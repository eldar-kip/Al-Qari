package com.example.ychicoran.dopclasses;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.ychicoran.ApiQuranJson.Classes.SuraList;
import com.example.ychicoran.ApiQuranJson.Intrface.SuraListInterface;
import com.example.ychicoran.Api_Al_Qrai.Class.Reciters.Reciter;
import com.example.ychicoran.Api_Al_Qrai.Class.Text.ArabText;
import com.example.ychicoran.Api_Al_Qrai.Class.Text.TranscriptionSura;
import com.example.ychicoran.Api_Al_Qrai.Interfases.ArabTextInterface;
import com.example.ychicoran.Api_Al_Qrai.Interfases.ReciterInterface;
import com.example.ychicoran.Api_Al_Qrai.Interfases.RiwayahListInterfase;
import com.example.ychicoran.Api_Al_Qrai.Interfases.TafsirInterface;
import com.example.ychicoran.Api_Al_Qrai.Interfases.TranscriptionInterface;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ParsingFails {

    public static List<SuraList> systemList;
    public static List<String> riwayahList;
    public static List<String> tafsirList;
    public static List<TranscriptionSura> transcriptionSuraList;
    public static List<ArabText> arabText;

    public static Reciter recitersData;
    private String url = "https://bba7k5bpe2kl91r7r8qk.containers.yandexcloud.net/";
    private Context context;

    public ParsingFails(Context context) {
        this.context = context;
    }

    public void parsingSystem(){
        parsingSurash();
        parsigRiwayahList();
        parsingReciterList();
        parsingTafsirList();
        parsingTranscription();
        parsingArabicText();
    }

    private void parsingSurash() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://cdn.jsdelivr.net/npm/quran-json@3.1.2/dist/chapters/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SuraListInterface service = retrofit.create(SuraListInterface.class);
        service.getListSurashes(Locale.getDefault().getLanguage()).enqueue(new Callback<List<SuraList>>(){
            @Override
            public void onResponse(Call<List<SuraList>> call, Response<List<SuraList>> response) {
                if (response.isSuccessful()&&response.body()!=null) {
                    systemList = response.body();

                }
            }
            @Override
            public void onFailure(Call<List<SuraList>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    private void parsigRiwayahList(){

        RiwayahListInterfase service = RetrofitClient.getClient(url).create(RiwayahListInterfase.class);
        service.getRiwayahList().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null){
                    System.out.println("Тут все ок 1+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    riwayahList = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                System.out.println("Блять проеб 1 _-----------------------------------------------------------------------------------------------");
                t.printStackTrace();
            }
        });
    }

    private void parsingReciterList(){
        ReciterInterface service = RetrofitClient.getClient(url).create(ReciterInterface.class);
        service.getRecitrList().enqueue(new Callback<Reciter>() {
            @Override
            public void onResponse(Call<Reciter> call, Response<Reciter> response) {
                if (response.isSuccessful() && response.body() != null){
                    System.out.println("Чтецы загружены+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    recitersData = response.body();
                }
            }

            @Override
            public void onFailure(Call<Reciter> call, Throwable t) {
                System.out.println("Ошибка загрузки чтецов-----------------------------------------------------------------------------------------------");
                t.printStackTrace();
            }
        });
    }
    private void parsingTafsirList(){
        TafsirInterface service = RetrofitClient.getClient(url).create(TafsirInterface.class);
        service.getTafsir().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null){
                    System.out.println("Tafsir загружен+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    tafsirList = response.body();

                }
            }
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                System.out.println("Ошибка загрузки Tafsir-----------------------------------------------------------------------------------------------");
                t.printStackTrace();
            }
        });
    }

    private void parsingTranscription(){

        SharedPreferences prefs = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String riwayahType = prefs.getString("riwayah_name", "hafs").toLowerCase();
        String lang = Locale.getDefault().getLanguage();

        TranscriptionInterface service = RetrofitClient.getClient(url).create(TranscriptionInterface.class);
        service.getTranscription(riwayahType, lang).enqueue(new Callback<List<TranscriptionSura>>() {
            @Override
            public void onResponse(Call<List<TranscriptionSura>> call, Response<List<TranscriptionSura>> response) {
                if (response.isSuccessful() && response.body() != null){
                    System.out.println("Transcription загружен+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    transcriptionSuraList = response.body();
                }
            }
            @Override
            public void onFailure(Call<List<TranscriptionSura>> call, Throwable t) {
                System.out.println("Ошибка загрузки Transcription-----------------------------------------------------------------------------------------------");
                t.printStackTrace();
            }
        });

    }
    private void  parsingArabicText(){
        SharedPreferences prefs = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String riwayahType = prefs.getString("riwayah_name", "hafs").toLowerCase();

        ArabTextInterface service = RetrofitClient.getClient(url).create(ArabTextInterface.class);
        service.getArabText(riwayahType).enqueue(new Callback<List<ArabText>>() {
            @Override
            public void onResponse(Call<List<ArabText>> call, Response<List<ArabText>> response) {
                if (response.isSuccessful() && response.body() != null){
                    System.out.println("Arabic загружен+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    arabText = response.body();
                }
            }
            @Override
            public void onFailure(Call<List<ArabText>> call, Throwable t) {
                System.out.println("Ошибка загрузки Arabic-----------------------------------------------------------------------------------------------");
                t.printStackTrace();
            }
        });
    }

}
