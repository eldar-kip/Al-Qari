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
import com.example.ychicoran.ApiQuranJson.Classes.SuraList;
import java.util.List;

import com.example.ychicoran.Api_Al_Qrai.Class.SuraAydio;
import com.example.ychicoran.Api_Al_Qrai.Class.Reciters.Haf;
import com.example.ychicoran.Api_Al_Qrai.Class.Reciters.Qaloun;
import com.example.ychicoran.Api_Al_Qrai.Class.Reciters.Reciter;
import com.example.ychicoran.Api_Al_Qrai.Class.Reciters.Warsh;
import com.example.ychicoran.MainActivity;
import com.example.ychicoran.dopclasses.AudioPlayer;
import android.widget.SeekBar;
import java.util.List;
import android.content.SharedPreferences;
import com.example.ychicoran.Api_Al_Qrai.Interfases.SuraAudioInterface;
import com.example.ychicoran.dopclasses.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.media3.exoplayer.ExoPlayer;

public class SuraAdapter extends RecyclerView.Adapter<SuraAdapter.SuraViewHolder> {

    private final List<SuraList> suraList;
    private final Context context;

    public SuraAdapter(List<SuraList> suraList, Context context) {
        this.suraList = suraList;
        this.context = context;
    }

    @NonNull
    @Override
    public SuraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sura, parent, false);
        return new SuraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuraViewHolder holder, int position) {
        SuraList sura = suraList.get(position);


        holder.numberSura.setText("Cура "+String.valueOf(sura.getId()));
        holder.arabName.setText(sura.getName());
        holder.systemName.setText(sura.getTransliteration());

        // Проверка по ID суры, играет ли она сейчас
        if (sura.getId() == AudioPlayer.getCurrentSuraId()) {
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
            intent.putExtra("SURA_ID", String.valueOf(sura.getId()));
            intent.putExtra("SURA_NAME", sura.getTranslation());
            context.startActivity(intent);
        });

        // Клик по кнопке плеер
        holder.playBtn.setOnClickListener(v -> {
            fetchAudioUrlAndPlay(sura.getId(), holder);
        });
    }

    private void fetchAudioUrlAndPlay(int suraId, SuraViewHolder holder) {
        // ЕСЛИ ЭТА СУРА УЖЕ В ПЛЕЕРЕ - ПРОСТО ПЕРЕКЛЮЧАЕМ (ПЛЕЙ/ПАУЗА) МГНОВЕННО
        if (suraId == AudioPlayer.getCurrentSuraId()) {
            AudioPlayer.playUrl(context, AudioPlayer.getCurrentUrl(), suraId, holder.playBtn, holder.seekBar);
            notifyDataSetChanged();
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
        Reciter data = MainActivity.recitersData;
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
                    AudioPlayer.playUrl(context, audioUrl, suraId, holder.playBtn, holder.seekBar);
                    notifyDataSetChanged();
                } else {
                    System.out.println("Ошибка API: " + response.code());
                    try {
                        if (response.errorBody() != null) {
                            System.out.println("Error body: " + response.errorBody().string());
                        }
                    } catch (Exception e) { e.printStackTrace(); }
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
        return suraList != null ? suraList.size() : 0;
    }

    public static class SuraViewHolder extends RecyclerView.ViewHolder {
        TextView numberSura, arabName, systemName;
        ImageView playBtn;
        SeekBar seekBar;

        public SuraViewHolder(@NonNull View itemView) {
            super(itemView);
            numberSura = itemView.findViewById(R.id.number_sura);
            arabName = itemView.findViewById(R.id.name_sura);
            systemName = itemView.findViewById(R.id.translation_sura);
            playBtn = itemView.findViewById(R.id.play_pause_item);
            seekBar = itemView.findViewById(R.id.seekBar_item);
        }
    }
}