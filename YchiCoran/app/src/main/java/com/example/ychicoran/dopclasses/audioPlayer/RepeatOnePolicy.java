package com.example.ychicoran.dopclasses.audioPlayer;

import com.example.ychicoran.R;

public class RepeatOnePolicy extends RepeatPolicy {

    @Override
    public boolean onPlaybackEnded(PlayerController playerController) {
        if (playerController != null) {
            playerController.seekTo(playerController.getRangeStartMs());
            playerController.play();
            return true; // Loop restarted
        }
        return false;
    }

    @Override
    public int getIconResId() {
        return R.drawable.media_playr_repeat;
    }

    @Override
    public float getAlpha() {
        return 1.0f; // Active bright button when repeat is ON
    }
}
