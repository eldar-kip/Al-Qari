package com.example.ychicoran;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import com.example.ychicoran.Api_Al_Qrai.Class.SuraAydio;
import com.example.ychicoran.Api_Al_Qrai.Class.Reciters.Haf;
import com.example.ychicoran.Api_Al_Qrai.Class.Reciters.Qaloun;
import com.example.ychicoran.Api_Al_Qrai.Class.Reciters.Reciter;
import com.example.ychicoran.Api_Al_Qrai.Class.Reciters.Warsh;
import com.example.ychicoran.Api_Al_Qrai.Class.Text.ArabText;
import com.example.ychicoran.Api_Al_Qrai.Class.Text.TranscriptionSura;
import com.example.ychicoran.dopclasses.AudioPlayer;
import android.widget.SeekBar;
import android.content.SharedPreferences;
import com.example.ychicoran.Api_Al_Qrai.Interfases.SuraAudioInterface;
import com.example.ychicoran.dopclasses.ParsingFails;
import com.example.ychicoran.dopclasses.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.media3.exoplayer.ExoPlayer;

public class SuraAdapter extends RecyclerView.Adapter<SuraAdapter.SuraViewHolder> {

    private final List<ArabText> arabText;
    private final List<TranscriptionSura> transcriptionSuraList;
    private final Context context;

    public SuraAdapter(List<ArabText> arabText, List<TranscriptionSura> transcriptionSuraList, Context context) {
        this.arabText = arabText;
        this.context = context;
        this.transcriptionSuraList = transcriptionSuraList;
    }
    private String typeSura(String type){
        switch (type){
            case "meccan":
                return context.getString(R.string.mecan);
            case "medinan":
                return context.getString(R.string.medina);
            default:
                return "";
        }

    }
    @NonNull
    @Override
    public SuraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sura, parent, false);
        return new SuraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuraViewHolder holder, int position) {
        TranscriptionSura transcriptionSura = transcriptionSuraList.get(position);
        ArabText arabText = ParsingFails.arabText.get(position);



        holder.numberSura.setText(context.getString(R.string.sura)+" "+String.valueOf(transcriptionSura.getId()));
        holder.arabName.setText(arabText.getName());
        holder.suraType.setText(typeSura(arabText.getType())+" \u2022 "+arabText.getTotal_verses()+" "+context.getString(R.string.ayat));
        holder.systemName.setText(transcriptionSura.getName());

        // Проверка по ID суры, играет ли она сейчас
        if (arabText.getId() == AudioPlayer.getCurrentSuraId()) {
            ExoPlayer p = AudioPlayer.getPlayer(context);
            if (p.getPlayWhenReady()) { // Используем playWhenReady для мгновенной реакции UI
                holder.playBtn.setImageResource(R.drawable.media_playr_pause);
                // Привязываем SeekBar текущего холдера к плееру для обновления прогресса
                AudioPlayer.attachSeekBar(holder.seekBar);
            } else {
                holder.playBtn.setImageResource(R.drawable.media_playr_play);
            }
        } else {
            holder.playBtn.setImageResource(R.drawable.media_playr_play);
            holder.seekBar.setProgress(0);
        }

        // Клик по всей карточке
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SuraDetals.class);
            intent.putExtra("SURA_ID", String.valueOf(arabText.getId()));
            intent.putExtra("SURA_NAME", arabText.getName());
            // Передаем URL если эта сура сейчас играет, чтобы в деталях подхватить мгновенно
            if (arabText.getId() == AudioPlayer.getCurrentSuraId()) {
                intent.putExtra("SURA_AUDIO_URL", AudioPlayer.getCurrentUrl());
            }
            context.startActivity(intent);
        });

        // Клик по кнопке плеер
        holder.playBtn.setOnClickListener(v -> {
            fetchAudioUrlAndPlay(context, arabText.getId(), holder.playBtn, holder.seekBar, this::notifyDataSetChanged, -1);
        });
    }

    public static void fetchAudioUrlAndPlay(Context context, int suraId, ImageView playBtn, SeekBar seekBar, Runnable onUpdate, long startPositionMs) {
        // ЕСЛИ ЭТА СУРА УЖЕ В ПЛЕЕРЕ - ПРОСТО ПЕРЕКЛЮЧАЕМ (ПЛЕЙ/ПАУЗА) МГНОВЕННО
        if (suraId == AudioPlayer.getCurrentSuraId()) {
            if (startPositionMs != -1) {
                AudioPlayer.seekTo(startPositionMs);
                if (!AudioPlayer.isPlaying()) {
                    AudioPlayer.playUrl(context, AudioPlayer.getCurrentUrl(), suraId, playBtn, seekBar);
                }
            } else {
                AudioPlayer.playUrl(context, AudioPlayer.getCurrentUrl(), suraId, playBtn, seekBar);
            }
            if (onUpdate != null) onUpdate.run();
            return;
        }

        System.out.println("Метод вызван (новый запрос)");
        SharedPreferences prefs = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String riwayahType = prefs.getString("riwayah_name", "hafs").toLowerCase();
        String reciterName = prefs.getString("reciter_name", "");

        if (reciterName.isEmpty()) {
            System.out.println("Ошибка: reciter_name пуст");
            return;
        }

        // Пытаемся найти bitrate для выбранного чтеца
        String bitrate = "32k"; // Значение по умолчанию
        Reciter data = ParsingFails.recitersData;
        if (data != null) {
            if (riwayahType.contains("hafs") && data.getHafs() != null) {
                for (Haf h : data.getHafs()) if (h.getName().equals(reciterName)) bitrate = h.getBitrate();
            } else if (riwayahType.contains("qaloun") && data.getQaloun() != null) {
                for (Qaloun q : data.getQaloun()) if (q.getName().equals(reciterName)) bitrate = q.getBitrate();
            } else if (riwayahType.contains("warsh") && data.getWarsh() != null) {
                for (Warsh w : data.getWarsh()) if (w.getName().equals(reciterName)) bitrate = w.getBitrate();
            }
        }

        // Используем обычный ID без нулей, так как в OpenAPI указан Integer
        String suraIdStr = String.valueOf(suraId);

        System.out.println("Запрос: riwayah=" + riwayahType + ", reciter=" + reciterName + ", bitrate=" + bitrate + ", sura=" + suraIdStr);

        // Используем базовый URL БЕЗ /quran/, так как мы добавили его в интерфейс
        SuraAudioInterface service = RetrofitClient.getClient("https://bba7k5bpe2kl91r7r8qk.containers.yandexcloud.net/")
                .create(SuraAudioInterface.class);
        service.getSuraAudio(riwayahType, reciterName, bitrate, suraIdStr).enqueue(new Callback<SuraAydio>() {
            @Override
            public void onResponse(Call<SuraAydio> call, Response<SuraAydio> response) {
                System.out.println("Код: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    System.out.println("Все работает! URL: " + response.body().getUrl());
                    String audioUrl = response.body().getUrl();
                    // Передаем suraId в playUrl для синхронизации UI
                    AudioPlayer.playUrl(context, audioUrl, suraId, playBtn, seekBar);
                    if (startPositionMs != -1) {
                        AudioPlayer.seekTo(startPositionMs);
                    }
                    if (onUpdate != null) onUpdate.run();
                } else {
                    System.out.println("Ошибка API: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SuraAydio> call, Throwable t) {
                System.out.println("Ошибка сети или парсинга: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arabText != null ? arabText.size() : 0;
    }

    public static class SuraViewHolder extends RecyclerView.ViewHolder {
        TextView numberSura, arabName, systemName, suraType;
        ImageView playBtn;
        SeekBar seekBar;

        public SuraViewHolder(@NonNull View itemView) {
            super(itemView);
            numberSura = itemView.findViewById(R.id.number_sura);
            arabName = itemView.findViewById(R.id.name_sura);
            systemName = itemView.findViewById(R.id.translation_sura);
            playBtn = itemView.findViewById(R.id.play_pause_item);
            seekBar = itemView.findViewById(R.id.seekBar_item);
            suraType = itemView.findViewById(R.id.type_countAuyh_sura);
        }
    }
}