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

        // Клик по всей карточке
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SuraDetals.class);
            intent.putExtra("SURA_ID", String.valueOf(sura.getId()));
            intent.putExtra("SURA_NAME", sura.getTranslation());
            context.startActivity(intent);
        });

        // Клик по кнопке плеер
        holder.playBtn.setOnClickListener(v -> {
            // Ваша логика аудио
        });
    }

    @Override
    public int getItemCount() {
        return suraList != null ? suraList.size() : 0;
    }

    public static class SuraViewHolder extends RecyclerView.ViewHolder {
        TextView numberSura, arabName, systemName;
        ImageView playBtn;

        public SuraViewHolder(@NonNull View itemView) {
            super(itemView);
            numberSura = itemView.findViewById(R.id.number_sura);
            arabName = itemView.findViewById(R.id.name_sura);
            systemName = itemView.findViewById(R.id.translation_sura);
            playBtn = itemView.findViewById(R.id.play_pause_item);
        }
    }
}