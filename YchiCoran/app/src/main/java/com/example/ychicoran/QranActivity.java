package com.example.ychicoran;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;

import com.example.ychicoran.ApiMP3Quran.interfacess.SurashNameInterface;
import com.example.ychicoran.ApiMP3Quran.model.Surash;
import com.example.ychicoran.ApiMP3Quran.model.SurashName;
import com.example.ychicoran.dopclasses.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QranActivity extends BaseActivity {
    private List<SurashName> arabList, systemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qran);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://mp3quran.net/api/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SurashNameInterface service = retrofit.create(SurashNameInterface.class);
        arabicSura(service);
        sistemSura(service);
    }
    private void arabicSura(SurashNameInterface service) {
        service.getSuraw("ar").enqueue(new Callback<Surash>() {
            @Override
            public void onResponse(Call<Surash> call, Response<Surash> response) {
                if (response.isSuccessful()&&response.body()!=null) {
                    arabList = response.body().getSuwar();
                }
                if (arabList != null && systemList != null) {
                    // ОБА списка готовы! Теперь можно строить цикл
                    updateUI();
                }
            }
            @Override
            public void onFailure(Call<Surash> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }
    private void sistemSura(SurashNameInterface service) {
        service.getSuraw(Locale.getDefault().getLanguage()).enqueue(new Callback<Surash>() {
            @Override
            public void onResponse(Call<Surash> call, Response<Surash> response) {
                if (response.isSuccessful()&&response.body()!=null) {
                    systemList = response.body().getSuwar();
                }
                if (arabList != null && systemList != null) {
                    // ОБА списка готовы! Теперь можно строить цикл
                    updateUI();
                }
            }
            @Override
            public void onFailure(Call<Surash> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void updateUI() {
        LinearLayout containerSura = findViewById(R.id.sura_list);
        if (containerSura == null) return;
        containerSura.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < 114; i++) {
            SurashName arabSura = arabList.get(i);
            SurashName systemSura = systemList.get(i);
            View suraView = inflater.inflate(R.layout.item_sura, containerSura, false);

            TextView numberSura = suraView.findViewById(R.id.number_sura);
            TextView arabName = suraView.findViewById(R.id.name_sura);
            TextView systemName = suraView.findViewById(R.id.translation_sura);
            ImageView playBtn = suraView.findViewById(R.id.play_pause_item);

            numberSura.setText("Сура " + arabSura.getId());
            arabName.setText(arabSura.getName());
            systemName.setText(systemSura.getName());

            int finalI = i;
            playBtn.setOnClickListener(v -> {
                // Сюда добавьте вашу логику запуска аудио
            });

            // Обработка клика по всей карточке (переход к деталям)
            suraView.setOnClickListener(v -> {
                Intent intent = new Intent(QranActivity.this, SuraDetals.class);

                // Передаем индекс и название (используем перевод для названия на след. экране)
                intent.putExtra("SURA_INDEX", finalI);
                intent.putExtra("SURA_NAME", systemSura.getName());
                intent.putExtra("SURA_ID", arabSura.getId());

                startActivity(intent);
            });

            containerSura.addView(suraView);

        }


    }


}
