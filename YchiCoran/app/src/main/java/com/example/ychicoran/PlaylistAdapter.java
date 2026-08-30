package com.example.ychicoran;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

        holder.nameRussian.setText(item.getSuraNameRussian());
        holder.nameArabic.setText(item.getSuraNameArabic());
        holder.rangeText.setText(item.getRangeString());

        // Visual feedback for selection
        if (selectedPositions.contains(position)) {
            holder.itemView.setAlpha(0.5f);
        } else {
            holder.itemView.setAlpha(1.0f);
        }

        holder.itemView.setOnClickListener(v -> {
            if (isSelectionMode) {
                toggleSelection(position);
            } else {
                Intent intent = new Intent(context, SuraDetals.class);
                intent.putExtra("SURA_ID", item.getSuraId());
                intent.putExtra("START_INDEX", item.getStartAyahIndex());
                intent.putExtra("END_INDEX", item.getEndAyahIndex());
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (!isSelectionMode) {
                isSelectionMode = true;
                toggleSelection(position);
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

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            nameRussian = itemView.findViewById(R.id.sura_name_transcription);
            nameArabic = itemView.findViewById(R.id.sura_name_arabic); 
            rangeText = itemView.findViewById(R.id.sura_number);
        }
    }
}
