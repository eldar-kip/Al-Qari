package com.example.ychicoran;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ychicoran.dopclasses.BaseActivity;
import com.example.ychicoran.dopclasses.Language_theme;
import com.example.ychicoran.dopclasses.System_theme;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        System_theme.setupThemeSpinner(this, findViewById(R.id.spinner_theme));
        Language_theme.setupLanguageSpinner(this, findViewById(R.id.spinner_target_language));
    }
}
