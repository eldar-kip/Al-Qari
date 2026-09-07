package com.example.ychicoran.dopclasses.audioPlayer;

import android.view.View;

public class PrevController {

    private long startRangeMs = 0;

    public void setStartRangeMs(long startRangeMs) {
        this.startRangeMs = startRangeMs;
    }

    public long getStartRangeMs() {
        return startRangeMs;
    }

    public void bindPrevButton(View prevButton, PlayerController playerController) {
        if (prevButton == null) return;

        prevButton.setOnClickListener(v -> {
            if (playerController != null) {
                playerController.seekTo(startRangeMs);
            }
        });
    }
}
