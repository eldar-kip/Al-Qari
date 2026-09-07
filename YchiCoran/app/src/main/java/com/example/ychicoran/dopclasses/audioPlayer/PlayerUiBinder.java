package com.example.ychicoran.dopclasses.audioPlayer;

import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.ychicoran.R;

public class PlayerUiBinder {

    private final PlayerController playerController;
    private final RepeatController repeatController;
    private final PrevController prevController;

    private ImageView playPauseBtn;
    private ImageView repeatBtn;
    private View prevBtn;
    private SeekBar seekBar;

    public PlayerUiBinder(PlayerController playerController, RepeatController repeatController, PrevController prevController) {
        this.playerController = playerController;
        this.repeatController = repeatController;
        this.prevController = prevController;

        if (this.playerController != null) {
            this.playerController.addOnPlayerStateChangeListener(new PlayerController.OnPlayerStateChangeListener() {
                @Override
                public void onIsPlayingChanged(boolean isPlaying) {
                    updatePlayPauseIcon(isPlaying);
                }

                @Override
                public void onPlaybackEnded() {
                    if (repeatController != null && !repeatController.handlePlaybackEnded(playerController)) {
                        updatePlayPauseIcon(false);
                    }
                }

                @Override
                public void onProgressUpdate(long positionMs) {
                    // Progress callback for additional listeners
                }
            });
        }
    }

    public void bindViews(ImageView playPauseBtn, SeekBar seekBar, ImageView repeatBtn, View prevBtn) {
        this.playPauseBtn = playPauseBtn;
        this.seekBar = seekBar;
        this.repeatBtn = repeatBtn;
        this.prevBtn = prevBtn;

        if (playerController != null) {
            playerController.attachSeekBar(seekBar);
            updatePlayPauseIcon(playerController.isPlaying());
        }

        if (playPauseBtn != null && playerController != null) {
            playPauseBtn.setOnClickListener(v -> playerController.togglePlayPause());
        }

        if (repeatBtn != null && repeatController != null) {
            repeatController.updateUi(repeatBtn);
            repeatBtn.setOnClickListener(v -> repeatController.toggleRepeatMode(repeatBtn));
        }

        if (prevBtn != null && prevController != null && playerController != null) {
            prevController.bindPrevButton(prevBtn, playerController);
        }
    }

    private void updatePlayPauseIcon(boolean isPlaying) {
        if (playPauseBtn != null) {
            playPauseBtn.setImageResource(isPlaying ? R.drawable.media_playr_pause : R.drawable.media_playr_play);
        }
    }
}
