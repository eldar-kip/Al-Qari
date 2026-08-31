package com.example.ychicoran.models;

public class PlaylistItem {
    private final String suraId;
    private final int startAyah;
    private final int endAyah;

    public PlaylistItem(String suraId, int startAyah, int endAyah) {
        this.suraId = suraId;
        this.startAyah = startAyah;
        this.endAyah = endAyah;
    }

    public String getSuraId() { return suraId; }
    public int getStartAyah() { return startAyah; }
    public int getEndAyah() { return endAyah; }
}
