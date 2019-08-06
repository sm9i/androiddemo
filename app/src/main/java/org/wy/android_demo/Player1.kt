package org.wy.android_demo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.jennifer.andy.simpleeyes.utils.kotlin.bindView
import org.wy.android_demo.player.IjkVideo
import org.wy.android_demo.player.VideoListener
import tv.danmaku.ijk.media.player.IMediaPlayer
import java.io.IOException

class Player1 : AppCompatActivity(), VideoListener {
    private val videoPlayer: IjkVideo by bindView(R.id.player_1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player1)
        videoPlayer.setVideoListener(this)
        videoPlayer.setPath("http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4")
        try {
            videoPlayer.load()
        } catch (e: IOException) {
            Toast.makeText(this, "播放失败", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    override fun onBufferingUpdate(p0: IMediaPlayer?, p1: Int) {
    }

    override fun onCompletion(p0: IMediaPlayer?) {
    }

    override fun onPrepared(p0: IMediaPlayer?) {
        videoPlayer.start()
    }

    override fun onInfo(p0: IMediaPlayer?, p1: Int, p2: Int): Boolean {
        return false
    }

    override fun onVideoSizeChanged(p0: IMediaPlayer?, p1: Int, p2: Int, p3: Int, p4: Int) {
    }

    override fun onError(p0: IMediaPlayer?, p1: Int, p2: Int): Boolean {
        return false
    }

    override fun onSeekComplete(p0: IMediaPlayer?) {
    }
}
