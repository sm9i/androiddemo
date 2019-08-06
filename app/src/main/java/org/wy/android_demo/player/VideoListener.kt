package org.wy.android_demo.player

import tv.danmaku.ijk.media.player.IMediaPlayer

/**
 * Video 监听
 */

interface VideoListener : IMediaPlayer.OnBufferingUpdateListener,
        IMediaPlayer.OnCompletionListener, IMediaPlayer.OnPreparedListener,
        IMediaPlayer.OnInfoListener, IMediaPlayer.OnVideoSizeChangedListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnSeekCompleteListener

