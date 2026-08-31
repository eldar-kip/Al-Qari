package com.example.ychicoran;

import static com.example.ychicoran.dopclasses.ParsingFails.suraTimestampsList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ychicoran.Api_Al_Qrai.Class.Text.ArabText;
import com.example.ychicoran.Api_Al_Qrai.Class.Text.TasfirText;
import com.example.ychicoran.Api_Al_Qrai.Class.Text.TranscriptionSura;
import com.example.ychicoran.Api_Al_Qrai.Class.Text.TranslateSura;
import com.example.ychicoran.Api_Al_Qrai.Class.Text.Verse;
import com.example.ychicoran.Api_Al_Qrai.Class.Timecode.SuraTimestamps;
import com.example.ychicoran.Api_Al_Qrai.Class.Timecode.VerseTimestamp;
import com.example.ychicoran.Api_Al_Qrai.Interfases.TasfirTextInterface;
import com.example.ychicoran.dopclasses.AudioPlayer;
import com.example.ychicoran.dopclasses.BaseActivity;
import com.example.ychicoran.dopclasses.ParsingFails;
import com.example.ychicoran.dopclasses.RetrofitClient;
import com.example.ychicoran.models.PlaylistItem;
import com.example.ychicoran.utils.PlaylistManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuraDetals extends BaseActivity {

    private RecyclerView recyclerView;
    private AyahAdapter adapter;
    private List<TranscriptionSura> transcriptionSuraList = ParsingFails.transcriptionSuraList;
    private List<ArabText> arabText = ParsingFails.arabText;
    private List<TranslateSura> translateSurasList = ParsingFails.translateSurasList;
    private List<SuraTimestamps> suraTimestampsList = ParsingFails.suraTimestampsList;
    private Button btnSavePlaylist;
    private PlaylistManager playlistManager;
    private int lastActiveAyahIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sura_detals);
        final List<VerseTimestamp>[] currentTimestamps = new List[]{null};

        recyclerView = findViewById(R.id.recycler_suras);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        findViewById(R.id.btn_back_sura).setOnClickListener(v -> finish());

        btnSavePlaylist = findViewById(R.id.btn_save_playlist);
        playlistManager = new PlaylistManager(this);

        Intent intent = getIntent();
        String suraIdStr = intent.getStringExtra("SURA_ID");

        if (suraIdStr == null) {
            finish();
            return;
        }

        int suraIndex = Integer.parseInt(suraIdStr) - 1;
        int suraId = Integer.parseInt(suraIdStr);

        TextView titleTranslate = findViewById(R.id.name_sura_translate);
        TextView titleTransliteration = findViewById(R.id.name_sura_transliteration);
        TextView titleArabic = findViewById(R.id.arabic_name_sura);

        SeekBar audioSeekBar = findViewById(R.id.audio_seekbar);
        ViewGroup playPauseContainer = findViewById(R.id.audio_play_pause);
        ImageView playPauseImg = (ImageView) playPauseContainer.getChildAt(0);

        if (suraId == AudioPlayer.getCurrentSuraId()) {
            AudioPlayer.attachSeekBar(audioSeekBar);
            if (AudioPlayer.isPlaying()) {
                playPauseImg.setImageResource(R.drawable.media_playr_pause);
            }
        }

        String nameArabic = "";
        String nameRussian = "";

        if (translateSurasList != null && suraIndex < translateSurasList.size()) {
            nameRussian = translateSurasList.get(suraIndex).getName();
            if (titleTranslate != null) titleTranslate.setText(nameRussian);
        }
        if (transcriptionSuraList != null && suraIndex < transcriptionSuraList.size()) {
            if (titleTransliteration != null)
                titleTransliteration.setText(transcriptionSuraList.get(suraIndex).getName());
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
            List<VerseTimestamp> verseTimestamps = suraTimestampsList.get(suraIndex).getVerses();
            currentTimestamps[0] = verseTimestamps;

            adapter = new AyahAdapter(transcrVerses, arabVerses, translVerses, verseTimestamps, suraIdStr);
            recyclerView.setAdapter(adapter);

            // Прокрутка к последнему прочитанному аяту, если передано из HomeActivity
            int startAyah = intent.getIntExtra("START_AYAH", -1);
            if (startAyah > 0) {
                int position = startAyah - 1;
                recyclerView.post(() -> {
                    if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                        ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(position, 100);
                    } else {
                        recyclerView.scrollToPosition(position);
                    }
                    adapter.updateActivePosition(position);
                });
            }

            final String finalNameRussian = nameRussian;
            adapter.setOnAyahClickListener((position, timestamp) -> {
                // Сохраняем последний прочитанный аят
                saveLastRead(suraIdStr, position + 1, finalNameRussian);

                AudioPlayer.seekTo((long) (timestamp.getStart() * 1000));
                if (!AudioPlayer.isPlaying()) {
                    SuraAdapter.fetchAudioUrlAndPlay(this, suraId, playPauseImg, audioSeekBar, null);
                }
            });

            findViewById(R.id.audio_play_pause).setOnClickListener(v -> {
                SuraAdapter.fetchAudioUrlAndPlay(this, suraId, playPauseImg, audioSeekBar, null);
            });

            findViewById(R.id.audio_prev).setOnClickListener(v -> AudioPlayer.seekTo(0));

            adapter.setOnTafsirClickListener(ayahId -> showTafsirBottomSheet(suraId, ayahId));

            if (btnSavePlaylist != null) {
                btnSavePlaylist.setOnClickListener(v -> {
                    int[] range = adapter.getSelectedRange();
                    if (range != null) {
                        PlaylistItem item = new PlaylistItem(suraIdStr, range[0], range[1], finalNameRussian, "");
                        playlistManager.savePlaylist(item);
                        Toast.makeText(this, "Сохранено в плейлист", Toast.LENGTH_SHORT).show();
                        adapter.clearSelection();
                    }
                });
            }
        }

        final String finalNameRussianProgress = nameRussian;
        AudioPlayer.setOnProgressUpdateListener(positionMs -> {
            if (currentTimestamps[0] == null) return;
            double currentTimeSec = positionMs / 1000.0;
            List<VerseTimestamp> timestamps = currentTimestamps[0];
            for (int i = 0; i < timestamps.size(); i++) {
                VerseTimestamp vt = timestamps.get(i);
                if (currentTimeSec >= vt.getStart() && currentTimeSec < vt.getEnd()) {
                    if (i != lastActiveAyahIndex) {
                        int finalI = i;
                        runOnUiThread(() -> {
                            adapter.updateActivePosition(finalI);
                            recyclerView.smoothScrollToPosition(finalI);
                            saveLastRead(suraIdStr, finalI + 1, finalNameRussianProgress);
                        });
                        lastActiveAyahIndex = i;
                    }
                    break;
                }
            }
        });
    }

    private void saveLastRead(String suraId, int ayahId, String suraName) {
        SharedPreferences prefs = getSharedPreferences("LastRead", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("last_sura_id", suraId);
        editor.putInt("last_ayah_id", ayahId);
        editor.putString("last_sura_name", suraName);
        editor.apply();
    }

    private void showTafsirBottomSheet(int suraId, int ayahId) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_tafsir, null);
        TextView title = view.findViewById(R.id.tafsir_title);
        TextView contentText = view.findViewById(R.id.tafsir_text);
        title.setText("Тафсир аята " + suraId + ":" + ayahId);
        contentText.setText("Загрузка...");
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

        SharedPreferences prefs = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String tafsirEdition = prefs.getString("tafsir_name", "saadi");

        TasfirTextInterface service = RetrofitClient.getClient("https://bba7k5bpe2kl91r7r8qk.containers.yandexcloud.net/")
                .create(TasfirTextInterface.class);

        service.getTasfirText(tafsirEdition, suraId).enqueue(new Callback<TasfirText>() {
            @Override
            public void onResponse(Call<TasfirText> call, Response<TasfirText> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Verse> verses = response.body().getVerses();
                    if (verses != null) {
                        for (Verse v : verses) {
                            if (v.getId() == ayahId) {
                                contentText.setText(v.getText());
                                return;
                            }
                        }
                    }
                    contentText.setText("Тафсир не найден.");
                } else {
                    contentText.setText("Ошибка загрузки.");
                }
            }

            @Override
            public void onFailure(Call<TasfirText> call, Throwable t) {
                contentText.setText("Ошибка сети.");
            }
        });
    }
}
