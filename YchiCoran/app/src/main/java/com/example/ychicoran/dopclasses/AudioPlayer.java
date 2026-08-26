package com.example.ychicoran.dopclasses;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;

import com.example.ychicoran.R;

public class AudioPlayer {
    private static ExoPlayer player;
    private static String currentUrl = "";
    private static int currentSuraId = -1;
    private static Handler handler = new Handler(Looper.getMainLooper());
    private static Runnable progressRunnable;

    private static SeekBar activeSeekBar;

    public static ExoPlayer getPlayer(Context context) {
        if (player == null) {
            player = new ExoPlayer.Builder(context.getApplicationContext()).build();
            player.addListener(new Player.Listener() {
                @Override
                public void onIsPlayingChanged(boolean isPlaying) {
                    if (isPlaying) {
                        startProgressUpdate();
                    }
                }
            });
        }
        return player;
    }

    public static void playUrl(Context context, String url, int suraId, ImageView playBtn, SeekBar seekBar) {
        ExoPlayer exoPlayer = getPlayer(context);
        activeSeekBar = seekBar;

        if (currentUrl.equals(url)) {
            if (exoPlayer.getPlayWhenReady()) {
                exoPlayer.pause();
                if (playBtn != null) playBtn.setImageResource(R.drawable.media_playr_play);
            } else {
                exoPlayer.play();
                if (playBtn != null) playBtn.setImageResource(R.drawable.media_playr_pause);
                startProgressUpdate();
            }
        } else {
            currentUrl = url;
            currentSuraId = suraId;
            MediaItem mediaItem = MediaItem.fromUri(url);
            exoPlayer.setMediaItem(mediaItem);
            exoPlayer.prepare();
            exoPlayer.setPlayWhenReady(true);
            if (playBtn != null) playBtn.setImageResource(R.drawable.media_playr_pause);
            startProgressUpdate();
        }

        setupSeekBar(exoPlayer, seekBar);
    }

    public static void attachSeekBar(SeekBar seekBar) {
        activeSeekBar = seekBar;
        if (player != null && (player.isPlaying() || player.getPlayWhenReady())) {
            setupSeekBar(player, seekBar);
            startProgressUpdate();
        }
    }

    private static void setupSeekBar(ExoPlayer exoPlayer, SeekBar seekBar) {
        if (seekBar == null) return;
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    exoPlayer.seekTo(progress);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeCallbacks(progressRunnable);
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                startProgressUpdate();
            }
        });
    }

    private static void startProgressUpdate() {
        if (activeSeekBar == null || player == null) return;

        handler.removeCallbacks(progressRunnable);
        progressRunnable = new Runnable() {
            @Override
            public void run() {
                if (player != null && (player.isPlaying() || player.getPlaybackState() == Player.STATE_BUFFERING) && activeSeekBar != null) {
                    long duration = player.getDuration();
                    long position = player.getCurrentPosition();
                    if (duration > 0) {
                        activeSeekBar.setMax((int) duration);
                        activeSeekBar.setProgress((int) position);
                    }
                    handler.postDelayed(this, 500); // Обновляем чаще (раз в 0.5 сек)
                }
            }
        };
        handler.post(progressRunnable);
    }

    public static String getCurrentUrl() {
        return currentUrl;
    }

    public static int getCurrentSuraId() {
        return currentSuraId;
    }

    public static void pause() {
        if (player != null) player.pause();
        handler.removeCallbacks(progressRunnable);
    }

    public static void release() {
        if (player != null) {
            player.release();
            player = null;
        }
        handler.removeCallbacks(progressRunnable);
    }
}
