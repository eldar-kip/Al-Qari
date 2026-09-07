package com.example.ychicoran.dopclasses;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.ychicoran.Api_Al_Qrai.Class.Reciters.Reciter;
import com.example.ychicoran.Api_Al_Qrai.Class.Text.ArabText;
import com.example.ychicoran.Api_Al_Qrai.Class.Text.TranscriptionSura;
import com.example.ychicoran.Api_Al_Qrai.Class.Text.TranslateSura;
import com.example.ychicoran.Api_Al_Qrai.Class.Timecode.SuraTimestamps;
import com.example.ychicoran.Api_Al_Qrai.Class.Timecode.VerseTimestamp;
import com.example.ychicoran.Api_Al_Qrai.Interfases.ArabTextInterface;
import com.example.ychicoran.Api_Al_Qrai.Interfases.ReciterInterface;
import com.example.ychicoran.Api_Al_Qrai.Interfases.RiwayahListInterfase;
import com.example.ychicoran.Api_Al_Qrai.Interfases.TafsirInterface;
import com.example.ychicoran.Api_Al_Qrai.Interfases.TimestampsInterface;
import com.example.ychicoran.Api_Al_Qrai.Interfases.TranscriptionInterface;
import com.example.ychicoran.Api_Al_Qrai.Interfases.TranslateSuraInterface;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ParsingFails {


    public static List<String> riwayahList;
    public static List<String> tafsirList;
    public static List<TranscriptionSura> transcriptionSuraList;
    public static List<TranslateSura> translateSurasList;
    public static List<ArabText> arabText;
    public static List<SuraTimestamps> suraTimestampsList ;

    public static Reciter recitersData;
    private String url = "https://bba7k5bpe2kl91r7r8qk.containers.yandexcloud.net/";
    private Context context;
    private SharedPreferences prefs;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;


    public ParsingFails(Context context) {

        this.context = context;
        this.prefs = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);

        // Инициализируем слушатель
        this.listener = (sharedPreferences, key) -> {
            if (key != null) {
                switch (key) {
                    case "language":
                        System.out.println("Настройки: Язык изменен, перепарсиваем...........................................................");
                        parsingTranscription(); // Зависит от языка
                        parsingTranslate();     // Зависит от языка
                        break;
                    case "riwayah_name":
                        System.out.println("Настройки: Риваят изменен, перепарсиваем...................................................................");
                        parsingTranscription(); // Зависит от риваята
                        parsingArabicText();    // Зависит от риваята
                        break;
                }
            }
        };

        // Регистрируем слушатель
        this.prefs.registerOnSharedPreferenceChangeListener(listener);

    }

    public void parsingSystem(){
       // parsingSurash();
        parsigRiwayahList();
        parsingReciterList();
        parsingTafsirList();
        parsingTranscription();
        parsingArabicText();
        parsingTranslate();
        parsingTimestamps();
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
        String lang = prefs.getString("language", "en").toLowerCase();


        TranscriptionInterface service = RetrofitClient.getClient(url).create(TranscriptionInterface.class);
        service.getTranscription(riwayahType, lang).enqueue(new Callback<List<TranscriptionSura>>() {
            @Override
            public void onResponse(Call<List<TranscriptionSura>> call, Response<List<TranscriptionSura>> response) {
                if (response.isSuccessful() && response.body() != null){
                    System.out.println("Transcription загружен+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    transcriptionSuraList = response.body();
                    EventBus.getDefault().post(new DataUpdatedEvent("Transcription"));
                    EventBus.getDefault().post(new DataUpdatedEvent("Arabic"));

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
                    EventBus.getDefault().post(new DataUpdatedEvent("Arabic"));
                }
            }
            @Override
            public void onFailure(Call<List<ArabText>> call, Throwable t) {
                System.out.println("Ошибка загрузки Arabic-----------------------------------------------------------------------------------------------");
                t.printStackTrace();
            }
        });
    }
    private void parsingTranslate(){
        SharedPreferences prefs = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String lang = prefs.getString("language", "en").toLowerCase();

        TranslateSuraInterface service = RetrofitClient.getClient(url).create(TranslateSuraInterface.class);
        service.getTranslate(lang).enqueue(new Callback<List<TranslateSura>>() {
            @Override
            public void onResponse(Call<List<TranslateSura>> call, Response<List<TranslateSura>> response) {
                if (response.isSuccessful() && response.body() != null){
                    System.out.println("Translate загружен+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    translateSurasList = response.body();
                    EventBus.getDefault().post(new DataUpdatedEvent("Translate"));
                }
            }
            @Override
            public void onFailure(Call<List<TranslateSura>> call, Throwable t) {
                System.out.println("Ошибка загрузки Translate-----------------------------------------------------------------------------------------------");
                t.printStackTrace();
            }
        });
    }
    private void parsingTimestamps(){

        SharedPreferences prefs = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String riwayahType = prefs.getString("riwayah_name", "hafs").toLowerCase();
        String reciterName = prefs.getString("reciter_name", "muhammad");
        String bitrate = prefs.getString("reciter_bitrate", "64k");
        TimestampsInterface service = RetrofitClient.getClient(url).create(TimestampsInterface.class);
        service.getSuraTimestamps(riwayahType, reciterName, bitrate).enqueue(new Callback<List<SuraTimestamps>>() {
            @Override
            public void onResponse(Call<List<SuraTimestamps>> call, Response<List<SuraTimestamps>> response) {
                if (response.isSuccessful() && response.body() != null){
                    System.out.println("Timestamps загружен+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    suraTimestampsList = response.body();

                }
            }
            @Override
            public void onFailure(Call<List<SuraTimestamps>> call, Throwable t) {
                System.out.println("Ошибка загрузки Timestamps-----------------------------------------------------------------------------------------------");
                t.printStackTrace();

            }
        });

    }


}
