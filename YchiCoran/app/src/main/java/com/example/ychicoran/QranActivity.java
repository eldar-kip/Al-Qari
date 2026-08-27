package com.example.ychicoran;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.ychicoran.ApiMP3Quran.interfacess.SurashNameInterface;
//import com.example.ychicoran.ApiMP3Quran.model.Surash;
//import com.example.ychicoran.ApiMP3Quran.model.SurashName;
import com.example.ychicoran.ApiQuranJson.Classes.SuraList;
import com.example.ychicoran.ApiQuranJson.Classes.SuraText;
import com.example.ychicoran.ApiQuranJson.Intrface.SuraListInterface;
import com.example.ychicoran.ApiQuranJson.Intrface.TextSuraInterface;
import com.example.ychicoran.dopclasses.BaseActivity;
import com.example.ychicoran.dopclasses.ParsingFails;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class QranActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qran);

        RecyclerView recyclerView = findViewById(R.id.recycler_suras);
        // Сетка/список (LinearLayoutManager)
        recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));

        // Берем готовый список из MainActivity и ставим его в адаптер
        if (ParsingFails.systemList != null) {
            SuraAdapter adapter = new SuraAdapter(ParsingFails.systemList, this);
            recyclerView.setAdapter(adapter);
        }
    }
}







