package com.example.ychicoran.dopclasses;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.ychicoran.R;

public class System_theme {
    String[] items;
    public static void setupThemeSpinner(Activity activity, Spinner spinner) {
        if (spinner == null) return;

         String[] items = new String[]{
                activity.getString(R.string.theme_system),
                activity.getString(R.string.theme_light),
                activity.getString(R.string.theme_dark)
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                activity,
                android.R.layout.simple_spinner_item,
                items
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        SharedPreferences prefs = activity.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        int savedTheme = prefs.getInt("theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        if (savedTheme == AppCompatDelegate.MODE_NIGHT_YES) spinner.setSelection(2);
        else if (savedTheme == AppCompatDelegate.MODE_NIGHT_NO) spinner.setSelection(1);
        else spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                int[] modes = {
                        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, // 0
                        AppCompatDelegate.MODE_NIGHT_NO,            // 1
                        AppCompatDelegate.MODE_NIGHT_YES            // 2
                };

                int newMode = modes[position]; // Просто берем режим по индексу

                if (AppCompatDelegate.getDefaultNightMode() != newMode) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("theme", newMode);
                    editor.apply();
                    AppCompatDelegate.setDefaultNightMode(newMode);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}