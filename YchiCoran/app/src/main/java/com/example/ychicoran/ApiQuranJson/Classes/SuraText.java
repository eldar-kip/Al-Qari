package com.example.ychicoran.ApiQuranJson.Classes;

import java.util.ArrayList;

public class SuraText {
    public Integer id;
    public String name;
    public String transliteration;
    public String translation;
    public String type;
    public Integer total_verses;
    public ArrayList<Verse> verses;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTransliteration() {
        return transliteration;
    }

    public String getTranslation() {
        return translation;
    }

    public String getType() {
        return type;
    }

    public Integer getTotal_verses() {
        return total_verses;
    }

    public ArrayList<Verse> getVerses() {
        return verses;
    }
}
