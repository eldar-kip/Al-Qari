package com.example.ychicoran.Api_Al_Qrai.Class.Text;

import java.util.ArrayList;

public class TranscriptionSura {
    private int id;
    private String name;
    private ArrayList<Verse> verses;

    public int getId() {
        return id;
    }

    public ArrayList<Verse> getVerses() {
        return verses;
    }

    public String getName() {
        return name;
    }
}

