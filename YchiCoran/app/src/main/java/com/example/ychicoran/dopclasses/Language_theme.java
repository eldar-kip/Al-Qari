package com.example.ychicoran.dopclasses;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Locale;

public class Language_theme {

    public static void setupLanguageSpinner(Activity activity, Spinner spinner) {
        if (spinner == null) return;

        // Список отображаемых имен
        String[] items = {"Русский", "English", "Къырымтатар"};
        // Соответствующие коды языков (ISO)
        String[] langCodes = {"ru", "en", "crh"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                activity,
                android.R.layout.simple_spinner_item,
                items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // 1. Загружаем сохраненный язык и выставляем позицию
        SharedPreferences prefs = activity.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String savedLang = prefs.getString("language", "ru");

        for (int i = 0; i < langCodes.length; i++) {
            if (langCodes[i].equals(savedLang)) {
                spinner.setSelection(i);
                break;
            }
        }

        // 2. Слушатель выбора
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String newLang = langCodes[position];
                String currentLang = prefs.getString("language", "ru");

                // Меняем язык только если он реально изменился
                if (!newLang.equals(currentLang)) {
                    // Сохраняем
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("language", newLang);
                    editor.apply();

                    // Применяем язык и перезагружаем активити
                    setLocale(activity, newLang);
                    activity.recreate();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    /**
     * Метод для физической смены Locale в приложении
     */
    public static void setLocale(Context context, String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}