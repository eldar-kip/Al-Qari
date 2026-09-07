package com.example.ychicoran.dopclasses.audioPlayer;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.SeekBar;

import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayerController {

    public interface OnPlayerStateChangeListener {
        void onIsPlayingChanged(boolean isPlaying);
        void onPlaybackEnded();
        void onProgressUpdate(long positionMs);
    }

    private ExoPlayer player;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable progressRunnable;
    private SeekBar attachedSeekBar;
    private final List<OnPlayerStateChangeListener> listeners = new CopyOnWriteArrayList<>();

    private long rangeStartMs = 0;
    private long rangeEndMs = -1;

    public PlayerController(Context context) {
        player = new ExoPlayer.Builder(context.getApplicationContext()).build();
        player.addListener(new Player.Listener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                if (isPlaying) {
                    startProgressUpdate();
                }
                notifyIsPlayingChanged(isPlaying);
            }

            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (playbackState == Player.STATE_ENDED) {
                    notifyPlaybackEnded();
                }
            }
        });
    }

    public ExoPlayer getExoPlayer() {
        return player;
    }

    public void addOnPlayerStateChangeListener(OnPlayerStateChangeListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeOnPlayerStateChangeListener(OnPlayerStateChangeListener listener) {
        listeners.remove(listener);
    }

    public void setOnPlayerStateChangeListener(OnPlayerStateChangeListener listener) {
        addOnPlayerStateChangeListener(listener);
    }

    private void notifyIsPlayingChanged(boolean isPlaying) {
        for (OnPlayerStateChangeListener l : listeners) {
            l.onIsPlayingChanged(isPlaying);
        }
    }

    private void notifyPlaybackEnded() {
        for (OnPlayerStateChangeListener l : listeners) {
            l.onPlaybackEnded();
        }
    }

    private void notifyProgressUpdate(long positionMs) {
        for (OnPlayerStateChangeListener l : listeners) {
            l.onProgressUpdate(positionMs);
        }
    }

    public void setPlaybackRange(long startMs, long endMs) {
        this.rangeStartMs = startMs;
        this.rangeEndMs = endMs;
    }

    public void clearPlaybackRange() {
        this.rangeStartMs = 0;
        this.rangeEndMs = -1;
    }

    public long getRangeStartMs() {
        return rangeStartMs;
    }

    public long getRangeEndMs() {
        return rangeEndMs;
    }

    public void playUrl(String url) {
        if (player == null) return;
        MediaItem mediaItem = MediaItem.fromUri(url);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.setPlayWhenReady(true);
        startProgressUpdate();
    }

    public void play() {
        if (player != null) {
            player.play();
            startProgressUpdate();
        }
    }

    public void pause() {
        if (player != null) {
            player.pause();
        }
        handler.removeCallbacks(progressRunnable);
    }

    public void togglePlayPause() {
        if (player == null) return;
        if (player.isPlaying()) {
            pause();
        } else {
            play();
        }
    }

    public void seekTo(long positionMs) {
        if (player != null) {
            player.seekTo(positionMs);
        }
    }

    public void attachSeekBar(SeekBar seekBar) {
        this.attachedSeekBar = seekBar;
        if (seekBar == null || player == null) return;

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (rangeEndMs > 0) {
                        player.seekTo(rangeStartMs + progress);
                    } else {
                        player.seekTo(progress);
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

        if (player.isPlaying() || player.getPlayWhenReady()) {
            startProgressUpdate();
        }
    }

    private void startProgressUpdate() {
        if (player == null) return;

        handler.removeCallbacks(progressRunnable);
        progressRunnable = new Runnable() {
            @Override
            public void run() {
                if (player != null && (player.isPlaying() || player.getPlaybackState() == Player.STATE_BUFFERING)) {
                    long duration = player.getDuration();
                    long position = player.getCurrentPosition();

                    // Остановка при выходе за границы диапазона
                    if (rangeEndMs > 0 && position >= rangeEndMs) {
                        pause();
                        notifyPlaybackEnded();
                        return;
                    }

                    if (attachedSeekBar != null) {
                        if (rangeEndMs > 0) {
                            long rangeDuration = rangeEndMs - rangeStartMs;
                            attachedSeekBar.setMax((int) rangeDuration);
                            attachedSeekBar.setProgress((int) (position - rangeStartMs));
                        } else if (duration > 0) {
                            attachedSeekBar.setMax((int) duration);
                            attachedSeekBar.setProgress((int) position);
                        }
                    }

                    notifyProgressUpdate(position);

                    handler.postDelayed(this, 100);
                }
            }
        };
        handler.post(progressRunnable);
    }

    public boolean isPlaying() {
        return player != null && player.isPlaying();
    }

    public void release() {
        if (player != null) {
            player.release();
            player = null;
        }
        handler.removeCallbacks(progressRunnable);
    }
}
