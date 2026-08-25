package com.example.ychicoran;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ychicoran.ApiQuranJson.Classes.Verse;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AyahAdapter extends RecyclerView.Adapter<AyahAdapter.AyahViewHolder> {

    private final List<Verse> ayahList;
    private final String suraId;
    private final Set<Integer> selectedPositions = new HashSet<>();
    private boolean isSelectionMode = false;

    public interface OnSelectionListener {
        void onSelectionChanged(int count);
    }

    private OnSelectionListener selectionListener;

    public void setOnSelectionListener(OnSelectionListener listener) {
        this.selectionListener = listener;
    }

    public AyahAdapter(List<Verse> ayahList, String suraId) {
        this.ayahList = ayahList;
        this.suraId = suraId;
    }

    @NonNull
    @Override
    public AyahViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ayah, parent, false);
        return new AyahViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AyahViewHolder holder, int position) {
        Verse verse = ayahList.get(position);

        holder.arabText.setText(verse.getText());
        holder.transcriptionText.setText(verse.getTransliteration());
        holder.translateText.setText(verse.getTranslation());
        holder.numberAyah.setText(suraId + ":" + verse.getId());

        // Visual feedback for selection
        if (selectedPositions.contains(position)) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#E0E0E0")); // Highlight color
        } else {
            holder.cardView.setCardBackgroundColor(Color.WHITE);
        }

        holder.itemView.setOnLongClickListener(v -> {
            if (!isSelectionMode) {
                isSelectionMode = true;
                toggleSelection(position);
            }
            return true;
        });

        holder.itemView.setOnClickListener(v -> {
            if (isSelectionMode) {
                toggleSelection(position);
            }
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

    public void clearSelection() {
        isSelectionMode = false;
        selectedPositions.clear();
        notifyDataSetChanged();
        if (selectionListener != null) {
            selectionListener.onSelectionChanged(0);
        }
    }

    @Override
    public int getItemCount() {
        return ayahList != null ? ayahList.size() : 0;
    }

    public static class AyahViewHolder extends RecyclerView.ViewHolder {
        TextView arabText, transcriptionText, translateText, numberAyah;
        CardView cardView;
        public AyahViewHolder(@NonNull View itemView) {
            super(itemView);
            arabText = itemView.findViewById(R.id.ayah_arab_text);
            transcriptionText = itemView.findViewById(R.id.ayah_transcription_text);
            translateText = itemView.findViewById(R.id.ayah_translate_text);
            numberAyah = itemView.findViewById(R.id.number_ayah);
            cardView = (CardView) itemView;
        }
    }
}
