package com.example.ychicoran.dopclasses;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.ychicoran.MainActivity;
import com.example.ychicoran.Api_Al_Qrai.Class.Reciters.Haf;
import com.example.ychicoran.Api_Al_Qrai.Class.Reciters.Qaloun;
import com.example.ychicoran.Api_Al_Qrai.Class.Reciters.Reciter;
import com.example.ychicoran.Api_Al_Qrai.Class.Reciters.Warsh;

import java.util.ArrayList;
import java.util.List;

public class Reciters_setting {

    public static void setupReciterSpinner(Activity activity, Spinner spinner) {
        if (spinner == null) return;

        Reciter data = ParsingFails.recitersData;
        if (data == null) {
            return;
        }

        SharedPreferences prefs = activity.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String riwayahName = prefs.getString("riwayah_name", "hafs").toLowerCase();

        List<String> names = new ArrayList<>();
        List<String> bitrates = new ArrayList<>();

        if (riwayahName.contains("hafs")) {
            if (data.getHafs() != null) {
                for (Haf h : data.getHafs()) {
                    names.add(h.getName());
                    bitrates.add(h.getBitrate());
                }
            }
        } else if (riwayahName.contains("qaloun")) {
            if (data.getQaloun() != null) {
                for (Qaloun q : data.getQaloun()) {
                    names.add(q.getName());
                    bitrates.add(q.getBitrate());
                }
            }
        } else if (riwayahName.contains("warsh")) {
            if (data.getWarsh() != null) {
                for (Warsh w : data.getWarsh()) {
                    names.add(w.getName());
                    bitrates.add(w.getBitrate());
                }
            }
        }

        if (names.isEmpty()) return;

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                activity,
                android.R.layout.simple_spinner_item,
                names
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        int savedReciterIndex = prefs.getInt("reciter_index", 0);
        if (savedReciterIndex < names.size()) {
            spinner.setSelection(savedReciterIndex);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < names.size()) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("reciter_index", position);
                    editor.putString("reciter_name", names.get(position));
                    if (position < bitrates.size()) {
                        editor.putString("reciter_bitrate", bitrates.get(position));
                    }
                    editor.apply();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}
