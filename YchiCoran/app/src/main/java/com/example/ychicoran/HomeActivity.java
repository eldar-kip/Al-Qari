package com.example.ychicoran;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.ychicoran.dopclasses.BaseActivity;
import com.example.ychicoran.dopclasses.NavigationProject;
import com.example.ychicoran.dopclasses.TimeNamaz;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.card.MaterialCardView;

public class HomeActivity extends BaseActivity {

    private TextView currentPrayerTv;
    private TextView nextPrayerTimeTv;
    private FusedLocationProviderClient fusedLocationClient;

    // Элементы карточки "Продолжить чтение"
    private MaterialCardView cardContinueReading;
    private TextView tvSuraName;
    private TextView tvAyatInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // Инициализация UI
        currentPrayerTv = findViewById(R.id.current_prayer);
        nextPrayerTimeTv = findViewById(R.id.next_prayer_time);

        // Инициализация UI карточки чтения
        cardContinueReading = findViewById(R.id.card_continue_reading);
        tvSuraName = findViewById(R.id.tv_sura_name);
        tvAyatInfo = findViewById(R.id.tv_ayat_info);

        NavigationProject.setup(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        updatePrayerTimes();
        updateLastReadCard();
    }

    private void updateLastReadCard() {
        SharedPreferences prefs = getSharedPreferences("LastRead", Context.MODE_PRIVATE);
        String suraId = prefs.getString("last_sura_id", null);
        int ayahId = prefs.getInt("last_ayah_id", -1);
        String suraName = prefs.getString("last_sura_name", "");

        if (suraId != null) {
            cardContinueReading.setVisibility(View.VISIBLE);
            tvSuraName.setText(suraName);
            tvAyatInfo.setText("Сура " + suraId + " • Аят " + ayahId);

            cardContinueReading.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, SuraDetals.class);
                intent.putExtra("SURA_ID", suraId);
                // Передаем также ID аята, чтобы в SuraDetals можно было к нему проскроллить
                intent.putExtra("START_AYAH", ayahId);
                startActivity(intent);
            });
        } else {
            cardContinueReading.setVisibility(View.GONE);
        }
    }

    private void updatePrayerTimes() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            currentPrayerTv.setText("Нет доступа к GPS");
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                TimeNamaz timeNamaz = new TimeNamaz();
                TimeNamaz.PrayerResult info = timeNamaz.getPrayerInfo(location.getLatitude(), location.getLongitude());

                currentPrayerTv.setText("Сейчас " + info.currentPrayerName + " намаз");
                String nextInfo = "Начало " + info.nextPrayerName + " через:\n" + info.timeRemaining;
                nextPrayerTimeTv.setText(nextInfo);
            } else {
                currentPrayerTv.setText("Местоположение не определено");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePrayerTimes();
        updateLastReadCard();
    }
}
