package com.example.ychicoran.models;

public class PlaylistItem {
    private final String suraId;
    private final int startAyahIndex;
    private final int endAyahIndex;
    private final String suraNameRussian;
    private final String suraNameArabic;

    public PlaylistItem(String suraId, int startAyahIndex, int endAyahIndex, String suraNameRussian, String suraNameArabic) {
        this.suraId = suraId;
        this.startAyahIndex = startAyahIndex;
        this.endAyahIndex = endAyahIndex;
        this.suraNameRussian = suraNameRussian;
        this.suraNameArabic = suraNameArabic;
    }

    public String getSuraId() { return suraId; }
    public int getStartAyahIndex() { return startAyahIndex; }
    public int getEndAyahIndex() { return endAyahIndex; }
    public String getSuraNameRussian() { return suraNameRussian; }
    public String getSuraNameArabic() { return suraNameArabic; }
    
    public String getRangeString() {
        return "Сура " + suraId + ": Аяты " + (startAyahIndex + 1) + "-" + (endAyahIndex + 1);
    }
}
