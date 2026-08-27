package com.example.ychicoran;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ychicoran.Api_Al_Qrai.Class.Text.ArabText;
import com.example.ychicoran.Api_Al_Qrai.Class.Text.TranscriptionSura;
import com.example.ychicoran.Api_Al_Qrai.Class.Text.TranslateSura;
import com.example.ychicoran.Api_Al_Qrai.Class.Text.Verse;
import com.example.ychicoran.dopclasses.BaseActivity;
import com.example.ychicoran.dopclasses.ParsingFails;

import java.util.List;

public class SuraDetals extends BaseActivity {

    private RecyclerView recyclerView;
    private AyahAdapter adapter;
    private List<TranscriptionSura> transcriptionSuraList = ParsingFails.transcriptionSuraList;
    private List<ArabText> arabText = ParsingFails.arabText;
    private List<TranslateSura> translateSurasList = ParsingFails.translateSurasList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sura_detals);

        recyclerView = findViewById(R.id.recycler_suras);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        findViewById(R.id.btn_back_sura).setOnClickListener(v -> finish());

        Intent intent = getIntent();
        String suraIdStr = intent.getStringExtra("SURA_ID");

        if (suraIdStr == null) {
            finish();
            return;
        }

        int suraIndex = Integer.parseInt(suraIdStr) - 1;

        TextView titleTranslate = findViewById(R.id.name_sura_translate);
        TextView titleTransliteration = findViewById(R.id.name_sura_transliteration);
        TextView titleArabic = findViewById(R.id.arabic_name_sura);

        if (translateSurasList != null && suraIndex < translateSurasList.size()) {
            if (titleTranslate != null) titleTranslate.setText(translateSurasList.get(suraIndex).getName());
        }

        if (transcriptionSuraList != null && suraIndex < transcriptionSuraList.size()) {
            if (titleTransliteration != null) titleTransliteration.setText(transcriptionSuraList.get(suraIndex).getName());
        }

        if (arabText != null && suraIndex < arabText.size()) {
            if (titleArabic != null) titleArabic.setText(arabText.get(suraIndex).getName());
        }

        if (arabText != null && transcriptionSuraList != null && translateSurasList != null &&
                suraIndex >= 0 && suraIndex < arabText.size()) {

            List<Verse> arabVerses = arabText.get(suraIndex).getVerses();
            List<Verse> transcrVerses = transcriptionSuraList.get(suraIndex).getVerses();
            List<Verse> translVerses = translateSurasList.get(suraIndex).getVerses();

            adapter = new AyahAdapter(transcrVerses, arabVerses, translVerses, suraIdStr);
            recyclerView.setAdapter(adapter);
        }
    }
}
