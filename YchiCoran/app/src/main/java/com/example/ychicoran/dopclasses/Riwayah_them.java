package com.example.ychicoran.dopclasses;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.ychicoran.MainActivity;
import com.example.ychicoran.R;

import java.util.List;

public class Riwayah_them {
    private static List<String> riwayahList;
    public static void  SinnerRiwayah(Activity activity, Spinner spinner){
        if (spinner == null) return;
        riwayahList  = MainActivity.riwayahList;
        System.out.println("____________________________________________________________________________________________"+riwayahList+"+++++++++++++++++++++++++++++++++++++++++++++++++++");
        if (riwayahList == null || riwayahList.isEmpty()) {
            return;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                activity,
                android.R.layout.simple_spinner_item,
                riwayahList
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        SharedPreferences save = activity.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        int savedRiwayahIndex = save.getInt("riwayah", 0);
        spinner.setSelection(savedRiwayahIndex);

        if(savedRiwayahIndex < riwayahList.size()){
            spinner.setSelection(savedRiwayahIndex);
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor =  save.edit();
                editor.putInt("riwayah", position);
                editor.putString("riwayah_name", riwayahList.get(position));
                editor.apply();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}
