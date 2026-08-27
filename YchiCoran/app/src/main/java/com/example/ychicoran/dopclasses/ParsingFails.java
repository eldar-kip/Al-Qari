package com.example.ychicoran.dopclasses;

import com.example.ychicoran.ApiQuranJson.Classes.SuraList;
import com.example.ychicoran.ApiQuranJson.Intrface.SuraListInterface;
import com.example.ychicoran.Api_Al_Qrai.Class.Reciters.Reciter;
import com.example.ychicoran.Api_Al_Qrai.Interfases.ReciterInterface;
import com.example.ychicoran.Api_Al_Qrai.Interfases.RiwayahListInterfase;
import com.example.ychicoran.Api_Al_Qrai.Interfases.TafsirInterface;

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
    public static Reciter recitersData;

    public void parsingSystem(){
        parsingSurash();
        parsigRiwayahList();
        parsingReciterList();
        parsingTafsirList();
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

        RiwayahListInterfase service = RetrofitClient.getClient("https://bba7k5bpe2kl91r7r8qk.containers.yandexcloud.net/").create(RiwayahListInterfase.class);
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
        ReciterInterface service = RetrofitClient.getClient("https://bba7k5bpe2kl91r7r8qk.containers.yandexcloud.net/").create(ReciterInterface.class);
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
        TafsirInterface service = RetrofitClient.getClient("https://bba7k5bpe2kl91r7r8qk.containers.yandexcloud.net/").create(TafsirInterface.class);
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

}
