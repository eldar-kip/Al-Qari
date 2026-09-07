package com.example.ychicoran.dopclasses.audioPlayer;

import android.widget.ImageView;

import java.util.Arrays;
import java.util.List;

public class RepeatController {

    private final List<RepeatPolicy> policies = Arrays.asList(
            new NoRepeatPolicy(),
            new RepeatOnePolicy()
    );
    private int currentPolicyIndex = 0;

    public void toggleRepeatMode(ImageView repeatBtn) {
        currentPolicyIndex = (currentPolicyIndex + 1) % policies.size();
        updateUi(repeatBtn);
    }

    public void updateUi(ImageView repeatBtn) {
        if (repeatBtn == null) return;
        RepeatPolicy current = getCurrentPolicy();
        repeatBtn.setImageResource(current.getIconResId());
        repeatBtn.setAlpha(current.getAlpha());
    }

    public boolean handlePlaybackEnded(PlayerController playerController) {
        return getCurrentPolicy().onPlaybackEnded(playerController);
    }

    public RepeatPolicy getCurrentPolicy() {
        return policies.get(currentPolicyIndex);
    }
}
