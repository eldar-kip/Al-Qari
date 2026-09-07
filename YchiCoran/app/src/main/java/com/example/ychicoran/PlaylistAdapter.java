package com.example.ychicoran;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.ychicoran.Api_Al_Qrai.Class.Text.ArabText;
import com.example.ychicoran.Api_Al_Qrai.Class.Text.TranslateSura;
import com.example.ychicoran.Api_Al_Qrai.Class.Timecode.SuraTimestamps;
import com.example.ychicoran.Api_Al_Qrai.Class.Timecode.VerseTimestamp;
import com.example.ychicoran.dopclasses.audioPlayer.AudioPlayer;
import com.example.ychicoran.dopclasses.ParsingFails;
import com.example.ychicoran.models.PlaylistItem;
import com.example.ychicoran.utils.PlaylistManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {
    private final List<PlaylistItem> playlists;
    private final Context context;
    private final Set<Integer> selectedPositions = new HashSet<>();
    private boolean isSelectionMode = false;
    private int playingPosition = -1;
    private long currentRangeStartMs = 0;
    private long currentRangeEndMs = 0;

    public interface OnSelectionListener {
        void onSelectionChanged(int count);
    }

    private OnSelectionListener selectionListener;

    public void setOnSelectionListener(OnSelectionListener listener) {
        this.selectionListener = listener;
    }

    public List<Integer> getSelectedPositions() {
        return new ArrayList<>(selectedPositions);
    }

    public void clearSelection() {
        isSelectionMode = false;
        selectedPositions.clear();
        notifyDataSetChanged();
        if (selectionListener != null) {
            selectionListener.onSelectionChanged(0);
        }
    }

    public PlaylistAdapter(List<PlaylistItem> playlists, Context context) {
        this.playlists = playlists;
        this.context = context;
        setupAudioListener();
    }

    public void setupAudioListener() {
        AudioPlayer.setOnProgressUpdateListener(positionMs -> {
            if (context instanceof Activity) {
                ((Activity) context).runOnUiThread(() -> {
                    // Проверка остановки по достижении конца диапазона теперь в AudioPlayer,
                    // здесь мы только обновляем иконки, если плеер остановился
                    if (playingPosition != -1 && !AudioPlayer.isPlaying()) {
                        int lastPos = playingPosition;
                        playingPosition = -1;
                        notifyItemChanged(lastPos);
                    }
                });
            }
        });
    }

    private void checkPlaybackRange(long positionMs) {
        // Метод больше не нужен
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_play_list, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        PlaylistItem item = playlists.get(position);

        String suraId = item.getSuraId();
        int suraIndex = Integer.parseInt(suraId) - 1;

        String nameArabic = "";

        if (ParsingFails.transcriptionSuraList != null && suraIndex >= 0 && suraIndex < ParsingFails.transcriptionSuraList.size()) {
            holder.nameRussian.setText(ParsingFails.transcriptionSuraList.get(suraIndex).getName());
        } else {
            holder.nameRussian.setText(context.getString(R.string.sura) + " " + suraId);
        }

        if (ParsingFails.arabText != null && suraIndex >= 0 && suraIndex < ParsingFails.arabText.size()) {
            nameArabic = ParsingFails.arabText.get(suraIndex).getName();
        }
        holder.nameArabic.setText(nameArabic);
        
        String rangeStr = context.getString(R.string.sura) + " " + suraId + ": " + 
                         context.getString(R.string.ayat) + " " + item.getStartAyah() + "-" + item.getEndAyah();
        holder.rangeText.setText(rangeStr);

        // Visual feedback for selection
        if (selectedPositions.contains(position)) {
            holder.itemView.setAlpha(0.5f);
        } else {
            holder.itemView.setAlpha(1.0f);
        }

        // Play/Pause button logic
        if (Integer.parseInt(suraId) == AudioPlayer.getCurrentSuraId() && playingPosition == position) {
            if (AudioPlayer.isPlaying()) {
                holder.playBtn.setImageResource(R.drawable.media_playr_pause);
                AudioPlayer.attachSeekBar(holder.seekBar);
            } else {
                holder.playBtn.setImageResource(R.drawable.media_playr_play);
            }
        } else {
            holder.playBtn.setImageResource(R.drawable.media_playr_play);
            holder.seekBar.setProgress(0);
        }

        holder.playBtn.setOnClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;
            
            int currentSuraId = Integer.parseInt(item.getSuraId());
            
            long startPosMs = 0;
            long endPosMs = 0;
            if (ParsingFails.suraTimestampsList != null && suraIndex < ParsingFails.suraTimestampsList.size()) {
                List<VerseTimestamp> timestamps = ParsingFails.suraTimestampsList.get(suraIndex).getVerses();
                if (item.getStartAyah() > 0 && item.getStartAyah() <= timestamps.size()) {
                    startPosMs = (long) (timestamps.get(item.getStartAyah() - 1).getStart() * 1000);
                }
                if (item.getEndAyah() > 0 && item.getEndAyah() <= timestamps.size()) {
                    endPosMs = (long) (timestamps.get(item.getEndAyah() - 1).getEnd() * 1000);
                }
            }

            if (AudioPlayer.getCurrentSuraId() == currentSuraId && playingPosition == pos) {
                // Если эта же карточка - просто плей/пауза
                AudioPlayer.setPlaybackRange(startPosMs, endPosMs);
                AudioPlayer.playUrl(context, AudioPlayer.getCurrentUrl(), currentSuraId, holder.playBtn, holder.seekBar);
            } else {
                // Если новая карточка
                int prevPlaying = playingPosition;
                playingPosition = pos;
                if (prevPlaying != -1) notifyItemChanged(prevPlaying);

                currentRangeStartMs = startPosMs;
                currentRangeEndMs = endPosMs;
                
                AudioPlayer.setPlaybackRange(startPosMs, endPosMs);
                SuraAdapter.fetchAudioUrlAndPlay(context, currentSuraId, holder.playBtn, holder.seekBar, () -> notifyItemChanged(pos), startPosMs);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;
            
            if (isSelectionMode) {
                toggleSelection(pos);
            } else {
                Intent intent = new Intent(context, SuraDetals.class);
                intent.putExtra("SURA_ID", item.getSuraId());
                intent.putExtra("START_AYAH", item.getStartAyah());
                intent.putExtra("END_AYAH", item.getEndAyah());
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return false;
            
            if (!isSelectionMode) {
                isSelectionMode = true;
                toggleSelection(pos);
            }
            return true;
        });
    }

    private void toggleSelection(int position) {
        if (selectedPositions.contains(position)) {
            selectedPositions.remove(position);
        } else {
            selectedPositions.add(position);
        }

        if (selectedPositions.isEmpty()) {
            isSelectionMode = false;
        }

        notifyItemChanged(position);
        if (selectionListener != null) {
            selectionListener.onSelectionChanged(selectedPositions.size());
        }
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        TextView nameRussian, nameArabic, rangeText;
        ImageView playBtn;
        SeekBar seekBar;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            nameRussian = itemView.findViewById(R.id.sura_name_transcription);
            nameArabic = itemView.findViewById(R.id.sura_name_arabic); 
            rangeText = itemView.findViewById(R.id.sura_number);
            playBtn = itemView.findViewById(R.id.btn_play_playlist);
            seekBar = itemView.findViewById(R.id.seekbar_playlist);
        }
    }
}
