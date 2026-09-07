package com.example.ychicoran.dopclasses.audioPlayer;

public abstract class RepeatPolicy {
    /**
     * Called when playback reaches the end of current track/range.
     * @param playerController The player controller instance.
     * @return true if playback was looped/restarted, false if stopped.
     */
    public abstract boolean onPlaybackEnded(PlayerController playerController);

    /**
     * @return Drawable resource ID for the repeat button icon.
     */
    public abstract int getIconResId();

    /**
     * @return Alpha level for visual state (0.4f for disabled, 1.0f for active).
     */
    public float getAlpha() {
        return 1.0f;
    }
}
