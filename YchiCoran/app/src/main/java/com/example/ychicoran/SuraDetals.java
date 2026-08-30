package com.example.ychicoran;

import static com.example.ychicoran.dopclasses.ParsingFails.suraTimestampsList;

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
import com.example.ychicoran.Api_Al_Qrai.Class.Timecode.SuraTimestamps;
import com.example.ychicoran.Api_Al_Qrai.Class.Timecode.VerseTimestamp;
import com.example.ychicoran.dopclasses.AudioPlayer;
import com.example.ychicoran.dopclasses.BaseActivity;
import com.example.ychicoran.dopclasses.ParsingFails;
import com.example.ychicoran.models.PlaylistItem;
import com.example.ychicoran.utils.PlaylistManager;

import java.util.List;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;
import android.widget.SeekBar;
import android.widget.ImageView;
import android.view.ViewGroup;
import com.example.ychicoran.dopclasses.AudioPlayer;

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

        btnSavePlaylist = findViewById(R.id.btn_save_playlist); // Мы добавим этот ID в layout чуть позже или используем существующий если есть
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

        // Плеер
        SeekBar audioSeekBar = findViewById(R.id.audio_seekbar);
        ViewGroup playPauseContainer = findViewById(R.id.audio_play_pause);
        ImageView playPauseImg = (ImageView) playPauseContainer.getChildAt(0);

        // Если сура уже играет — привязываем SeekBar
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

            adapter = new AyahAdapter(transcrVerses, arabVerses, translVerses, verseTimestamps, suraIdStr);
            recyclerView.setAdapter(adapter);

            String finalNameRussian = nameRussian;
            String finalNameArabic = nameArabic;
            adapter.setOnAyahClickListener((position, timestamp) -> {
                // Перематываем аудио на начало аята
                AudioPlayer.seekTo((long) (timestamp.getStart() * 1000));
                // Если плеер на паузе — запускаем, используя метод из SuraAdapter
                if (!AudioPlayer.isPlaying()) {
                    SuraAdapter.fetchAudioUrlAndPlay(this, suraId, playPauseImg, audioSeekBar, null);
                }
            });

            // Кнопка Play/Pause в нижнем плеере
            findViewById(R.id.audio_play_pause).setOnClickListener(v -> {
                SuraAdapter.fetchAudioUrlAndPlay(this, suraId, playPauseImg, audioSeekBar, null);
            });

            findViewById(R.id.audio_prev).setOnClickListener(v -> {
                AudioPlayer.seekTo(0);
            });

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
        AudioPlayer.setOnProgressUpdateListener(positionMs -> {
            if (currentTimestamps[0] == null) return; // Защита от пустых данных

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
                        });
                        lastActiveAyahIndex = i;
                    }
                    break;
                }
            }
        });
    }
}
