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
import com.example.ychicoran.models.PlaylistItem;
import com.example.ychicoran.utils.PlaylistManager;

import java.util.List;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;

public class SuraDetals extends BaseActivity {

    private RecyclerView recyclerView;
    private AyahAdapter adapter;
    private List<TranscriptionSura> transcriptionSuraList = ParsingFails.transcriptionSuraList;
    private List<ArabText> arabText = ParsingFails.arabText;
    private List<TranslateSura> translateSurasList = ParsingFails.translateSurasList;
    private Button btnSavePlaylist;
    private PlaylistManager playlistManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sura_detals);

        recyclerView = findViewById(R.id.recycler_suras);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        findViewById(R.id.btn_back_sura).setOnClickListener(v -> finish());
        
        btnSavePlaylist = findViewById(R.id.btn_save_playlist); // Мы добавим этот ID в layout чуть позже или используем существующий если есть
        playlistManager = new PlaylistManager(this);

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

        String nameArabic = "";
        String nameRussian = "";

        if (translateSurasList != null && suraIndex < translateSurasList.size()) {
            nameRussian = translateSurasList.get(suraIndex).getName();
            if (titleTranslate != null) titleTranslate.setText(nameRussian);
        }

        if (transcriptionSuraList != null && suraIndex < transcriptionSuraList.size()) {
            if (titleTransliteration != null) titleTransliteration.setText(transcriptionSuraList.get(suraIndex).getName());
        }

        if (arabText != null && suraIndex < arabText.size()) {
            nameArabic = arabText.get(suraIndex).getName();
            if (titleArabic != null) titleArabic.setText(nameArabic);
        }

        if (arabText != null && transcriptionSuraList != null && translateSurasList != null &&
                suraIndex >= 0 && suraIndex < arabText.size()) {

            List<Verse> arabVerses = arabText.get(suraIndex).getVerses();
            List<Verse> transcrVerses = transcriptionSuraList.get(suraIndex).getVerses();
            List<Verse> translVerses = translateSurasList.get(suraIndex).getVerses();

            // Логика фильтрации по диапазону
            if (intent.hasExtra("START_INDEX") && intent.hasExtra("END_INDEX")) {
                int start = intent.getIntExtra("START_INDEX", 0);
                int end = intent.getIntExtra("END_INDEX", arabVerses.size() - 1);
                
                if (start >= 0 && end < arabVerses.size() && start <= end) {
                    arabVerses = arabVerses.subList(start, end + 1);
                    transcrVerses = transcrVerses.subList(start, end + 1);
                    translVerses = translVerses.subList(start, end + 1);
                }
            }

            adapter = new AyahAdapter(transcrVerses, arabVerses, translVerses, suraIdStr);
            recyclerView.setAdapter(adapter);

            String finalNameRussian = nameRussian;
            String finalNameArabic = nameArabic;

            adapter.setOnSelectionListener(count -> {
                if (btnSavePlaylist != null) {
                    btnSavePlaylist.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
                }
            });

            if (btnSavePlaylist != null) {
                btnSavePlaylist.setOnClickListener(v -> {
                    int[] range = adapter.getSelectedRange();
                    if (range != null) {
                        PlaylistItem item = new PlaylistItem(
                                suraIdStr,
                                range[0],
                                range[1],
                                finalNameRussian,
                                finalNameArabic
                        );
                        playlistManager.savePlaylist(item);
                        Toast.makeText(this, "Сохранено в плейлист", Toast.LENGTH_SHORT).show();
                        adapter.clearSelection();
                    }
                });
            }
        }
    }
}
