package com.example.ychicoran.Api_Al_Qrai.Class.Text;

import java.util.ArrayList;

public class ArabText {
    private int id;
    private String name;
    private String type;
    private int total_verses;
    private ArrayList<Verse> verses;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getTotal_verses() {
        return total_verses;
    }

    public ArrayList<Verse> getVerses() {
        return verses;
    }
}

