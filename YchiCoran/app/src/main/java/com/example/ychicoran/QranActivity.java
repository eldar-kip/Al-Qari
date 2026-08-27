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

import com.example.ychicoran.dopclasses.BaseActivity;
import com.example.ychicoran.dopclasses.DataUpdatedEvent;
import com.example.ychicoran.dopclasses.ParsingFails;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class QranActivity extends BaseActivity {
    private SuraAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qran);

        recyclerView = findViewById(R.id.recycler_suras);
        recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));

        updateAdapter();
    }

    private void updateAdapter() {
        if (ParsingFails.arabText != null && ParsingFails.transcriptionSuraList != null) {
            adapter = new SuraAdapter(ParsingFails.arabText, ParsingFails.transcriptionSuraList, this);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataUpdated(DataUpdatedEvent event) {
        // Когда любые данные обновились, просто обновляем список
        updateAdapter();
    }
}







