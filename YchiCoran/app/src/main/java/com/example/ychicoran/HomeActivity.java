package com.example.ychicoran;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.ychicoran.dopclasses.BaseActivity;
import com.example.ychicoran.dopclasses.NavigationProject;
import com.example.ychicoran.dopclasses.TimeNamaz;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class HomeActivity extends BaseActivity {

    private TextView currentPrayerTv;
    private TextView nextPrayerTimeTv;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // Инициализация UI
        currentPrayerTv = findViewById(R.id.current_prayer);
        nextPrayerTimeTv = findViewById(R.id.next_prayer_time);

        NavigationProject.setup(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        updatePrayerTimes();
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

                // Устанавливаем текст в наши TextView
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
    }
}
