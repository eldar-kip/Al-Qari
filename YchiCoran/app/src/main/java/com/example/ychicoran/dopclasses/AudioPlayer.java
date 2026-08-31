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
    private static OnProgressUpdateListener progressListener;
    private static long rangeStartMs = 0;
    private static long rangeEndMs = -1;

    public interface OnProgressUpdateListener {
        void onProgressUpdate(long position);
    }

    public static void setOnProgressUpdateListener(OnProgressUpdateListener listener) {
        progressListener = listener;
    }

    public static void setPlaybackRange(long startMs, long endMs) {
        rangeStartMs = startMs;
        rangeEndMs = endMs;
    }

    public static void clearPlaybackRange() {
        rangeStartMs = 0;
        rangeEndMs = -1;
    }

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
        if (player != null) {
            setupSeekBar(player, seekBar);
            if (player.isPlaying() || player.getPlayWhenReady()) {
                startProgressUpdate();
            }
        }
    }

    private static void setupSeekBar(ExoPlayer exoPlayer, SeekBar seekBar) {
        if (seekBar == null) return;
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (rangeEndMs > 0) {
                        exoPlayer.seekTo(rangeStartMs + progress);
                    } else {
                        exoPlayer.seekTo(progress);
                    }
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
        if (player == null) return;

        handler.removeCallbacks(progressRunnable);
        progressRunnable = new Runnable() {
            @Override
            public void run() {
                if (player != null && (player.isPlaying() || player.getPlaybackState() == Player.STATE_BUFFERING)) {
                    long duration = player.getDuration();
                    long position = player.getCurrentPosition();

                    // Остановка если вышли за пределы диапазона
                    if (rangeEndMs > 0 && position >= rangeEndMs) {
                        player.pause();
                        if (progressListener != null) progressListener.onProgressUpdate(position);
                        return;
                    }

                    if (activeSeekBar != null) {
                        if (rangeEndMs > 0) {
                            long rangeDuration = rangeEndMs - rangeStartMs;
                            activeSeekBar.setMax((int) rangeDuration);
                            activeSeekBar.setProgress((int) (position - rangeStartMs));
                        } else if (duration > 0) {
                            activeSeekBar.setMax((int) duration);
                            activeSeekBar.setProgress((int) position);
                        }
                    }
                    
                    if (progressListener != null) {
                        progressListener.onProgressUpdate(position);
                    }
                    handler.postDelayed(this, 100);
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
    public static void seekTo(long position) {
        if (player != null) {
            player.seekTo(position);
        }
    }
    public static boolean isPlaying() {
        return player != null && player.isPlaying();
    }
}
