package com.example.ychicoran;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ychicoran.Api_Al_Qrai.Class.Text.Verse;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AyahAdapter extends RecyclerView.Adapter<AyahAdapter.AyahViewHolder> {

    private final List<Verse> transcription;
    private final List<Verse> arabText;
    private final List<Verse> translate;
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

    public int[] getSelectedRange() {
        if (selectedPositions.isEmpty()) return null;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int pos : selectedPositions) {
            if (pos < min) min = pos;
            if (pos > max) max = pos;
        }
        return new int[]{min, max};
    }

    public AyahAdapter(List<Verse> transcription, List<Verse> arabText, List<Verse> translate, String suraId) {
        this.transcription = transcription;
        this.arabText = arabText;
        this.translate = translate;
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
        Verse arabVerse = arabText.get(position);
        Verse transcrVerse = transcription.get(position);
        Verse translVerse = translate.get(position);

        holder.arabText.setText(arabVerse.getText());
        holder.transcriptionText.setText(transcrVerse.getText());
        holder.translateText.setText(translVerse.getText());
        holder.numberAyah.setText(suraId + ":" + arabVerse.getId());

        // Visual feedback for selection
        if (selectedPositions.contains(position)) {
            holder.mainLayout.setAlpha(0.5f); // Semi-transparent when selected
        } else {
            holder.mainLayout.setAlpha(1.0f);
        }

        holder.itemView.setOnLongClickListener(v -> {
            isSelectionMode = true;
            toggleSelection(position);
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
        return arabText != null ? arabText.size() : 0;
    }

    public static class AyahViewHolder extends RecyclerView.ViewHolder {
        TextView arabText, transcriptionText, translateText, numberAyah;
        CardView cardView;
        LinearLayout mainLayout;

        public AyahViewHolder(@NonNull View itemView) {
            super(itemView);
            arabText = itemView.findViewById(R.id.ayah_arab_text);
            transcriptionText = itemView.findViewById(R.id.ayah_transcription_text);
            translateText = itemView.findViewById(R.id.ayah_translate_text);
            numberAyah = itemView.findViewById(R.id.number_ayah);
            cardView = (CardView) itemView;
            mainLayout = itemView.findViewById(R.id.ayah_main_layout);
        }
    }
}
