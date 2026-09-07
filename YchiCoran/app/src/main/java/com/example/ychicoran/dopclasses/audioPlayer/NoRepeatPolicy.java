package com.example.ychicoran.dopclasses.audioPlayer;

import com.example.ychicoran.R;

public class NoRepeatPolicy extends RepeatPolicy {

    @Override
    public boolean onPlaybackEnded(PlayerController playerController) {
        return false; // Do not loop, stop playback
    }

    @Override
    public int getIconResId() {
        return R.drawable.media_playr_repeat;
    }

    @Override
    public float getAlpha() {
        return 0.4f; // Dimmed button when repeat is OFF
    }
}
