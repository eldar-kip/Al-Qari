package com.example.ychicoran;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ychicoran.ApiQuranJson.Classes.SuraList;
import com.example.ychicoran.ApiQuranJson.Intrface.SuraListInterface;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private boolean log_person = true;
    public static List<SuraList> systemList;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {;
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        parsingSurash();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
            if (log_person) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            // Закрываем MainActivity, чтобы пользователь не вернулся на него кнопкой "Назад"
            finish();

    }
}