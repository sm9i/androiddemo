package org.wy.android_demo.player;

import android.content.Context;


import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.io.IOException;
import java.util.Map;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class IjkVideo extends FrameLayout {

    public IjkVideo(@NonNull Context context) {
        this(context, null);
    }

    public IjkVideo(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IjkVideo(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        setBackgroundColor(Color.BLACK);
        createSurfaceView();
        mAudioManager = (AudioManager) mContext.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        mAudioFocusHelper = new AudioFocusHelper();
    }

    private IMediaPlayer mMediaPlayer = null;
    private String mPath;
    private Map<String, String> mHeader;
    private SurfaceView mSurfaceView;
    private Context mContext;
    private boolean mEnableMediaCodec;
    private AudioManager mAudioManager;
    private VideoListener mListener;
    private AudioFocusHelper mAudioFocusHelper;

    /**
     * 创建一个SurfaceView
     */
    private void createSurfaceView() {
        mSurfaceView = new SurfaceView(mContext);
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if (mMediaPlayer != null) {
                    mMediaPlayer.setDisplay(holder);
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        addView(mSurfaceView, 0, layoutParams);
    }

    /**
     * 创建一个player
     *
     * @return
     */
    private IMediaPlayer createPlayer() {
        IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 1);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "http-detect-range-support", 1);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "min-frames", 100);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1);
        ijkMediaPlayer.setVolume(1.0f, 1.0f);
        setEnableMediaCodec(ijkMediaPlayer, mEnableMediaCodec);
        return ijkMediaPlayer;
    }

    /**
     * 设置是否硬件解码
     *
     * @param ijkMediaPlayer
     * @param isEnable
     */
    private void setEnableMediaCodec(IjkMediaPlayer ijkMediaPlayer, boolean isEnable) {
        int value = isEnable ? 1 : 0;
        //开启硬解码
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", value);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", value);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", value);

    }

    public void setEnableMediaCodec(boolean isEnable) {
        mEnableMediaCodec = isEnable;
    }

    //设置监听
    private void setListener(IMediaPlayer player) {
        player.setOnPreparedListener(mPreparedListener);
        player.setOnVideoSizeChangedListener(mVideoSizeChangedListener);
    }

    public void setVideoListener(VideoListener listener) {
        mListener = listener;
    }

    public void setPath(String path) {
        setPath(path, null);
    }

    public void setPath(String path, Map<String, String> header) {
        mPath = path;
        mHeader = header;
    }

    //加载视频
    public void load() throws IOException {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
        mMediaPlayer = createPlayer();
        setListener(mMediaPlayer);
        mMediaPlayer.setDisplay(mSurfaceView.getHolder());
        mMediaPlayer.setDataSource(mContext, Uri.parse(mPath), mHeader);
        mMediaPlayer.prepareAsync();
    }

    public void start() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
            mAudioFocusHelper.requestFocus();
        }
    }

    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
            mAudioFocusHelper.abandonFocus();
        }
    }
    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
            mAudioFocusHelper.abandonFocus();
        }
    }

    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mAudioFocusHelper.abandonFocus();
        }
    }


    public void reset() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mAudioFocusHelper.abandonFocus();
        }
    }
    public long getDuration() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getDuration();
        } else {
            return 0;
        }
    }
    public long getCurrentPosition() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }


    public void seekTo(long l) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(l);
        }
    }
    public boolean isPlaying(){
        if(mMediaPlayer != null) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    /*
    监听赋值
     */
    private IMediaPlayer.OnPreparedListener mPreparedListener = new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer iMediaPlayer) {
            if (mListener != null) {
                mListener.onPrepared(iMediaPlayer);
            }
        }
    };

    private IMediaPlayer.OnVideoSizeChangedListener mVideoSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
            int videoWidth = iMediaPlayer.getVideoWidth();
            int videoHeight = iMediaPlayer.getVideoHeight();
            if (videoWidth != 0 && videoHeight != 0) {
                mSurfaceView.getHolder().setFixedSize(videoWidth, videoHeight);
            }
        }
    };

    /**
     * 音频焦点改变监听
     */
    private class AudioFocusHelper implements AudioManager.OnAudioFocusChangeListener {

        boolean startRequest = false;
        boolean pausedForLoss = false;
        int currentFocus = 0;

        @Override
        public void onAudioFocusChange(int focusChange) {
            if (currentFocus == focusChange) {
                return;
            }
            currentFocus = focusChange;
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN: //获得焦点
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT: //暂时获得焦点
                    if (startRequest || pausedForLoss) {
                        start();
                        startRequest = false;
                        pausedForLoss = false;
                    }
                    if (mMediaPlayer != null) {
                        mMediaPlayer.setVolume(1.0f, 1.0f);
                    }
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    if (isPlaying()) {
                        pausedForLoss = true;
                        pause();
                    }
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    if (mMediaPlayer != null && isPlaying()) {
                        mMediaPlayer.setVolume(0.1f, 0.1f);
                    }
                    break;
            }
        }

        boolean requestFocus() {
            if (currentFocus == AudioManager.AUDIOFOCUS_GAIN) {
                return true;
            }
            if (mAudioManager == null) {
                return false;
            }
            int status = mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            if (AudioManager.AUDIOFOCUS_REQUEST_GRANTED == status) {
                currentFocus = AudioManager.AUDIOFOCUS_GAIN;
                return true;
            }

            startRequest = true;
            return false;
        }

        boolean abandonFocus() {
            if (mAudioManager == null) {
                return false;
            }
            startRequest = false;
            int status = mAudioManager.abandonAudioFocus(this);
            return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == status;
        }
    }
}
