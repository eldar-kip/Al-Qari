package com.example.ychicoran.dopclasses.audioPlayer;

import android.content.Context;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.media3.exoplayer.ExoPlayer;

public class AudioPlayer {

    public interface OnProgressUpdateListener {
        void onProgressUpdate(long position);
    }

    private static PlayerController playerController;
    private static final RepeatController repeatController = new RepeatController();
    private static final PrevController prevController = new PrevController();

    private static String currentUrl = "";
    private static int currentSuraId = -1;
    private static OnProgressUpdateListener progressListener;

    public static PlayerController getController(Context context) {
        if (playerController == null) {
            playerController = new PlayerController(context.getApplicationContext());
            playerController.addOnPlayerStateChangeListener(new PlayerController.OnPlayerStateChangeListener() {
                @Override
                public void onIsPlayingChanged(boolean isPlaying) {
                }

                @Override
                public void onPlaybackEnded() {
                    repeatController.handlePlaybackEnded(playerController);
                }

                @Override
                public void onProgressUpdate(long positionMs) {
                    if (progressListener != null) {
                        progressListener.onProgressUpdate(positionMs);
                    }
                }
            });
        }
        return playerController;
    }

    public static RepeatController getRepeatController() {
        return repeatController;
    }

    public static PrevController getPrevController() {
        return prevController;
    }

    public static void setOnProgressUpdateListener(OnProgressUpdateListener listener) {
        progressListener = listener;
    }

    public static ExoPlayer getPlayer(Context context) {
        return getController(context).getExoPlayer();
    }

    public static void playUrl(Context context, String url, int suraId, ImageView playBtn, SeekBar seekBar) {
        PlayerController controller = getController(context);

        if (currentUrl.equals(url)) {
            controller.togglePlayPause();
        } else {
            currentUrl = url;
            currentSuraId = suraId;
            controller.playUrl(url);
        }

        if (seekBar != null) {
            controller.attachSeekBar(seekBar);
        }
    }

    public static void attachSeekBar(SeekBar seekBar) {
        if (playerController != null) {
            playerController.attachSeekBar(seekBar);
        }
    }

    public static void setPlaybackRange(long startMs, long endMs) {
        if (playerController != null) {
            playerController.setPlaybackRange(startMs, endMs);
        }
        prevController.setStartRangeMs(startMs);
    }

    public static void clearPlaybackRange() {
        if (playerController != null) {
            playerController.clearPlaybackRange();
        }
        prevController.setStartRangeMs(0);
    }

    public static void seekTo(long positionMs) {
        if (playerController != null) {
            playerController.seekTo(positionMs);
        }
    }

    public static boolean isPlaying() {
        return playerController != null && playerController.isPlaying();
    }

    public static void pause() {
        if (playerController != null) {
            playerController.pause();
        }
    }

    public static void release() {
        if (playerController != null) {
            playerController.release();
            playerController = null;
        }
    }

    public static String getCurrentUrl() {
        return currentUrl;
    }

    public static int getCurrentSuraId() {
        return currentSuraId;
    }
}
