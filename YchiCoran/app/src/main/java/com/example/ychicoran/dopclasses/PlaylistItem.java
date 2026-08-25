package com.example.ychicoran.dopclasses;

public class PlaylistItem {
    public String name;       // "Al fatiha"
    public String detail;     // "Сура 1: Аяты 2-7"
    public String arabicName; // "الفاتحة"

    public PlaylistItem(String name, String detail, String arabicName) {
        this.name = name;
        this.detail = detail;
        this.arabicName = arabicName;
    }
}
