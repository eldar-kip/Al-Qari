package com.example.ychicoran;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ychicoran.dopclasses.BaseActivity;
import com.example.ychicoran.dopclasses.NavigationProject;
import com.example.ychicoran.utils.PlaylistManager;
import com.example.ychicoran.models.PlaylistItem;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.widget.Button;
import android.view.View;

public class PlaylistActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private PlaylistAdapter adapter;
    private PlaylistManager playlistManager;
    private Button btnDelete;
    private Button btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_playlist);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recycler_playlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        btnDelete = findViewById(R.id.btn_delete_selected_playlists);
        //btnCreate = findViewById(R.id.btn_create_playlist);

        playlistManager = new PlaylistManager(this);
        List<PlaylistItem> playlists = playlistManager.getPlaylists();
        
        adapter = new PlaylistAdapter(playlists, this);
        recyclerView.setAdapter(adapter);

        adapter.setOnSelectionListener(count -> {
            if (btnDelete != null) {
                btnDelete.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
            }
        });

        if (btnCreate != null) {
            btnCreate.setOnClickListener(v -> {
                Intent intent = new Intent(PlaylistActivity.this, QranActivity.class);
                startActivity(intent);
                finish();
            });
        }

        if (btnDelete != null) {
            btnDelete.setOnClickListener(v -> {
                List<Integer> selected = adapter.getSelectedPositions();
                playlistManager.deletePlaylists(selected);
                
                // Обновляем список айтемов
                selected.sort((a, b) -> b - a);
                for (int pos : selected) {
                    playlists.remove((int) pos);
                }
                
                adapter.clearSelection();
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.setupAudioListener();
        }
    }
}

