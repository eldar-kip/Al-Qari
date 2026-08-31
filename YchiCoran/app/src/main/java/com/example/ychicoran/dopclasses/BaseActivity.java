package com.example.ychicoran.dopclasses;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.ychicoran.R;
import com.example.ychicoran.utils.AppUsageTracker;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        SharedPreferences prefs = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String lang = prefs.getString("language", "ru");
        Language_theme.setLocale(this, lang);

        SharedPreferences seva = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        int theme = seva.getInt("theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(theme);
        super.onCreate(savedInstanceState);

    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getWindow().getDecorView().setBackgroundResource(R.color.background_layout);
        NavigationProject.setup(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppUsageTracker.startTracking();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppUsageTracker.stopTracking(this);
    }


}
