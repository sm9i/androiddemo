package org.wy.android_demo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_player_1.setOnClickListener {
            startActivity(Intent(this, Player1::class.java))
        }
        btn_player_2.setOnClickListener {
            startActivity(Intent(this, Player2::class.java))
        }
        flavor.text = BuildConfig.FLAVOR
    }


}
