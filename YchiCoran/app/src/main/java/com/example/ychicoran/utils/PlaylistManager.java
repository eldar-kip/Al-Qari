package com.example.ychicoran.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.ychicoran.models.PlaylistItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PlaylistManager {
    private static final String PREFS_NAME = "PlaylistPrefs";
    private static final String KEY_PLAYLISTS = "playlists";
    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    public PlaylistManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    public void savePlaylist(PlaylistItem item) {
        List<PlaylistItem> currentList = getPlaylists();
        currentList.add(item);
        saveList(currentList);
    }

    public void deletePlaylists(List<Integer> positions) {
        List<PlaylistItem> currentList = getPlaylists();
        // Удаляем с конца, чтобы индексы не съезжали
        positions.sort((a, b) -> b - a);
        for (int pos : positions) {
            if (pos >= 0 && pos < currentList.size()) {
                currentList.remove(pos);
            }
        }
        saveList(currentList);
    }

    private void saveList(List<PlaylistItem> list) {
        String json = gson.toJson(list);
        sharedPreferences.edit().putString(KEY_PLAYLISTS, json).apply();
    }

    public List<PlaylistItem> getPlaylists() {
        String json = sharedPreferences.getString(KEY_PLAYLISTS, null);
        if (json == null) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<List<PlaylistItem>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
