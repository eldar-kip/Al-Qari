package com.example.ychicoran.dopclasses;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Shader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.ychicoran.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Tafsi_them {
    public static void  SinnerTafsir(Activity activity, Spinner spinner) {
        if (spinner == null) return;
        List<String> allTafsir = ParsingFails.tafsirList;
        if (allTafsir == null || allTafsir.isEmpty()) {
            return;
        }

        SharedPreferences save = activity.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String currentLang = save.getString("language", Locale.getDefault().getLanguage());
        List<String> filteredTafsir = new ArrayList<>();

        for (String tafsir : allTafsir) {
            if (tafsir.length() >= 2 && tafsir.substring(0, 2).equalsIgnoreCase(currentLang)) {
                filteredTafsir.add(tafsir);
            }
        }

        if (filteredTafsir.isEmpty()) {
            // Если для текущего языка нет тафсиров, можно показать все или оставить пустым
            filteredTafsir.addAll(allTafsir);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                activity,
                android.R.layout.simple_spinner_item,
                filteredTafsir
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        String savedTafsirName = save.getString("tafsir_name", "");
        
        // Находим позицию сохраненного тафсира в отфильтрованном списке
        int selectionIndex = 0;
        if (!savedTafsirName.isEmpty()) {
            for (int i = 0; i < filteredTafsir.size(); i++) {
                if (filteredTafsir.get(i).equals(savedTafsirName)) {
                    selectionIndex = i;
                    break;
                }
            }
        }
        spinner.setSelection(selectionIndex);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedName = filteredTafsir.get(position);
                String currentSavedName = save.getString("tafsir_name", "");

                if (!selectedName.equals(currentSavedName)) {
                    SharedPreferences.Editor editor = save.edit();
                    editor.putInt("tafsir", position); // Сохраняем индекс в текущем (фильтрованном) списке
                    editor.putString("tafsir_name", selectedName);
                    editor.apply();
                    activity.recreate();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}

        });
    }
}
