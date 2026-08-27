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

import com.example.ychicoran.Api_Al_Qrai.Class.RiwayahListClass;
import com.example.ychicoran.Api_Al_Qrai.Class.Reciters.Reciter;
import com.example.ychicoran.Api_Al_Qrai.Class.Tafsir;
import com.example.ychicoran.Api_Al_Qrai.Interfases.ReciterInterface;
import com.example.ychicoran.Api_Al_Qrai.Interfases.RiwayahListInterfase;
import com.example.ychicoran.Api_Al_Qrai.Interfases.TafsirInterface;
import com.example.ychicoran.dopclasses.ParsingFails;
import com.example.ychicoran.dopclasses.RetrofitClient;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private boolean log_person = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {;
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ParsingFails parsingFails = new ParsingFails(this);
        parsingFails.parsingSystem();
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
        finish();
    }
}