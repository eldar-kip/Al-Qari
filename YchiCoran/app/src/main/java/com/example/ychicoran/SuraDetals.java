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
    private Button btnSavePlaylist;
    private PlaylistManager playlistManager;
    private int lastActiveAyahIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sura_detals);
        
        // Используем актуальные данные из ParsingFails напрямую
        List<TranscriptionSura> transcriptionSuraList = ParsingFails.transcriptionSuraList;
        List<ArabText> arabText = ParsingFails.arabText;
        List<TranslateSura> translateSurasList = ParsingFails.translateSurasList;
        List<SuraTimestamps> suraTimestampsList = ParsingFails.suraTimestampsList;

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
        
        final int intentStartAyah = intent.getIntExtra("START_AYAH", -1);
        final int intentEndAyah = intent.getIntExtra("END_AYAH", -1);

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
            
            // Если пришли из плейлиста, перематываем на начало диапазона
            if (intentStartAyah > 0 && intentEndAyah > 0 && suraTimestampsList != null) {
                List<VerseTimestamp> timestamps = suraTimestampsList.get(suraIndex).getVerses();
                if (intentStartAyah <= timestamps.size()) {
                    AudioPlayer.seekTo((long)(timestamps.get(intentStartAyah - 1).getStart() * 1000));
                }
            }
        }

        String nameArabicDisplay = "";
        String nameRussianDisplay = "";

        if (translateSurasList != null && suraIndex < translateSurasList.size()) {
            nameRussianDisplay = translateSurasList.get(suraIndex).getName();
            if (titleTranslate != null) titleTranslate.setText(nameRussianDisplay);
        }
        if (transcriptionSuraList != null && suraIndex < transcriptionSuraList.size()) {
            if (titleTransliteration != null)
                titleTransliteration.setText(transcriptionSuraList.get(suraIndex).getName());
        }
        if (arabText != null && suraIndex < arabText.size()) {
            nameArabicDisplay = arabText.get(suraIndex).getName();
            if (titleArabic != null) titleArabic.setText(nameArabicDisplay);
        }

        if (arabText != null && transcriptionSuraList != null && translateSurasList != null &&
                suraIndex >= 0 && suraIndex < arabText.size()) {

            List<Verse> arabVerses = new java.util.ArrayList<>(arabText.get(suraIndex).getVerses());
            List<Verse> transcrVerses = new java.util.ArrayList<>(transcriptionSuraList.get(suraIndex).getVerses());
            List<Verse> translVerses = new java.util.ArrayList<>(translateSurasList.get(suraIndex).getVerses());
            List<VerseTimestamp> verseTimestamps = new java.util.ArrayList<>(suraTimestampsList.get(suraIndex).getVerses());

            if (intentStartAyah > 0 && intentEndAyah > 0) {
                // Фильтруем списки под нужный диапазон
                int startIndex = Math.max(0, intentStartAyah - 1);
                int endIndex = Math.min(arabVerses.size(), intentEndAyah);

                if (startIndex < endIndex) {
                    arabVerses = new java.util.ArrayList<>(arabVerses.subList(startIndex, endIndex));
                    transcrVerses = new java.util.ArrayList<>(transcrVerses.subList(startIndex, endIndex));
                    translVerses = new java.util.ArrayList<>(translVerses.subList(startIndex, endIndex));
                    verseTimestamps = new java.util.ArrayList<>(verseTimestamps.subList(startIndex, endIndex));
                }
                
                // Устанавливаем диапазон в плеер
                if (suraTimestampsList != null && suraIndex < suraTimestampsList.size()) {
                    List<VerseTimestamp> originalTimestamps = suraTimestampsList.get(suraIndex).getVerses();
                    if (intentStartAyah <= originalTimestamps.size() && intentEndAyah <= originalTimestamps.size()) {
                        long startMs = (long) (originalTimestamps.get(intentStartAyah - 1).getStart() * 1000);
                        long endMs = (long) (originalTimestamps.get(intentEndAyah - 1).getEnd() * 1000);
                        AudioPlayer.setPlaybackRange(startMs, endMs);
                    }
                }
            } else {
                AudioPlayer.clearPlaybackRange();
            }

            currentTimestamps[0] = verseTimestamps;

            adapter = new AyahAdapter(transcrVerses, arabVerses, translVerses, verseTimestamps, suraIdStr);
            recyclerView.setAdapter(adapter);

            adapter.setOnSelectionListener(count -> {
                if (btnSavePlaylist != null) {
                    btnSavePlaylist.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
                }
            });

            // Прокрутка к аяту (из HomeActivity или если это начало плейлиста)
            if (intentStartAyah > 0 && intentEndAyah == -1) { // Только скролл (последний прочитанный)
                int position = intentStartAyah - 1;
                recyclerView.post(() -> {
                    if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                        ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(position, 100);
                    } else {
                        recyclerView.scrollToPosition(position);
                    }
                    adapter.updateActivePosition(position);
                });
            }

            final String finalNameRussian = nameRussianDisplay;
            final List<VerseTimestamp> finalVersesTimestamps = verseTimestamps;

            adapter.setOnAyahClickListener((position, timestamp) -> {
                // Сохраняем последний прочитанный аят (учитываем смещение, если это плейлист)
                int actualAyahId = (intentStartAyah > 0 && intentEndAyah > 0) ? (intentStartAyah + position) : (position + 1);
                saveLastRead(suraIdStr, actualAyahId, finalNameRussian);

                AudioPlayer.seekTo((long) (timestamp.getStart() * 1000));
                if (!AudioPlayer.isPlaying()) {
                    SuraAdapter.fetchAudioUrlAndPlay(this, suraId, playPauseImg, audioSeekBar, null, 0);
                }
            });

            findViewById(R.id.audio_play_pause).setOnClickListener(v -> {
                if (AudioPlayer.getCurrentSuraId() == suraId) {
                    if (!AudioPlayer.isPlaying() && intentStartAyah > 0 && intentEndAyah > 0) {
                        AudioPlayer.seekTo((long)(finalVersesTimestamps.get(0).getStart() * 1000));
                    }
                    AudioPlayer.playUrl(this, AudioPlayer.getCurrentUrl(), suraId, playPauseImg, audioSeekBar);
                } else {
                    long seekMs = (intentStartAyah > 0 && intentEndAyah > 0) ? (long)(finalVersesTimestamps.get(0).getStart() * 1000) : -1;
                    SuraAdapter.fetchAudioUrlAndPlay(this, suraId, playPauseImg, audioSeekBar, null, seekMs);
                }
            });

            findViewById(R.id.audio_prev).setOnClickListener(v -> {
                if (intentStartAyah > 0 && intentEndAyah > 0) {
                     AudioPlayer.seekTo((long)(finalVersesTimestamps.get(0).getStart() * 1000));
                } else {
                     AudioPlayer.seekTo(0);
                }
            });

            adapter.setOnTafsirClickListener(ayahId -> showTafsirBottomSheet(suraId, ayahId));

            if (btnSavePlaylist != null) {
                btnSavePlaylist.setOnClickListener(v -> {
                    int[] range = adapter.getSelectedRange();
                    if (range != null) {
                        // Здесь нужно учитывать, что если список уже отфильтрован, 
                        // индексы в адаптере начинаются с 0, но соответствуют startIndex в оригинале.
                        int actualStart = (intentStartAyah > 0) ? (intentStartAyah - 1 + range[0]) : range[0];
                        int actualEnd = (intentStartAyah > 0) ? (intentStartAyah - 1 + range[1]) : range[1];

                        PlaylistItem item = new PlaylistItem(suraIdStr, actualStart + 1, actualEnd + 1);
                        playlistManager.savePlaylist(item);
                        Toast.makeText(this, R.string.playlist_created, Toast.LENGTH_SHORT).show();
                        adapter.clearSelection();
                    }
                });
            }
        }

        final String finalNameRussianProgress = nameRussianDisplay;

        AudioPlayer.setOnProgressUpdateListener(positionMs -> {
            if (currentTimestamps[0] == null) return;
            double currentTimeSec = positionMs / 1000.0;
            
            // Если мы в режиме плейлиста и дошли до конца диапазона
            if (intentEndAyah > 0) {
                List<VerseTimestamp> originalTimestamps = suraTimestampsList.get(suraIndex).getVerses();
                if (intentEndAyah <= originalTimestamps.size()) {
                    double endTime = originalTimestamps.get(intentEndAyah - 1).getEnd();
                    if (currentTimeSec >= endTime) {
                        AudioPlayer.pause();
                        runOnUiThread(() -> playPauseImg.setImageResource(R.drawable.media_playr_play));
                        return;
                    }
                }
            }

            List<VerseTimestamp> timestamps = currentTimestamps[0];
            for (int i = 0; i < timestamps.size(); i++) {
                VerseTimestamp vt = timestamps.get(i);
                if (currentTimeSec >= vt.getStart() && currentTimeSec < vt.getEnd()) {
                    if (i != lastActiveAyahIndex) {
                        int finalI = i;
                        runOnUiThread(() -> {
                            adapter.updateActivePosition(finalI);
                            recyclerView.smoothScrollToPosition(finalI);
                            
                            int actualAyahId = (intentStartAyah > 0 && intentEndAyah > 0) ? (intentStartAyah + finalI) : (finalI + 1);
                            saveLastRead(suraIdStr, actualAyahId, finalNameRussianProgress);
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
