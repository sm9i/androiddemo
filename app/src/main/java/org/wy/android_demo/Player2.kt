package org.wy.android_demo

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import cn.jzvd.JzvdStd
import com.jennifer.andy.simpleeyes.utils.kotlin.bindView

class Player2 : AppCompatActivity() {

    val video: JzvdStd by bindView(R.id.jz_video)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player2)

        video.setUp("http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4", "神奇女侠")
        video.thumbImageView.setImageURI(Uri.parse("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640"))


    }
}
