package com.example.ychicoran;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ychicoran.ApiQuranJson.Classes.SuraText;
import com.example.ychicoran.ApiQuranJson.Intrface.TextSuraInterface;
import com.example.ychicoran.dopclasses.BaseActivity;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SuraDetals extends BaseActivity {

    private RecyclerView recyclerView;
    private AyahAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sura_detals);

        recyclerView = findViewById(R.id.recycler_suras);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        findViewById(R.id.btn_back_sura).setOnClickListener(v -> finish());
        Intent intent = getIntent();
        String suraId = intent.getStringExtra("SURA_ID");
        
        if (suraId != null) {
            fetchSuraText(suraId);
        }
    }

    private void fetchSuraText(String suraId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://cdn.jsdelivr.net/npm/quran-json@3.1.2/dist/chapters/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TextSuraInterface service = retrofit.create(TextSuraInterface.class);

        String lang = Locale.getDefault().getLanguage();
        // В API для русского используется "ru", для английского "en". 
        // По умолчанию ставим "ru"
        if (!lang.equals("en")) {
            lang = "ru"; 
        }

        service.getTextSura(lang, suraId).enqueue(new Callback<SuraText>() {
            @Override
            public void onResponse(Call<SuraText> call, Response<SuraText> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SuraText suraData = response.body();
                    
                    // Обновляем заголовки в header
                    android.widget.TextView titleTranslate = findViewById(R.id.name_sura_translate);
                    android.widget.TextView titleTransliteration = findViewById(R.id.name_sura_transliteration);
                    android.widget.TextView titleArabic = findViewById(R.id.arabic_name_sura);

                    if (titleTranslate != null) titleTranslate.setText(suraData.getTranslation());
                    if (titleTransliteration != null) titleTransliteration.setText(suraData.getTransliteration());
                    if (titleArabic != null) titleArabic.setText(suraData.getName());

                    adapter = new AyahAdapter(suraData.getVerses(), suraId);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<SuraText> call, Throwable t) {
                Log.e("SuraDetals", "Error fetching sura text", t);
            }
        });
    }
}
