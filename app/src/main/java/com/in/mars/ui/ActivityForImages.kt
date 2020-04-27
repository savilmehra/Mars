package com.`in`.mars

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide


class ActivityForImages : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.only_image)
        val iv: ImageView = findViewById(R.id.ivBannerPic)
        Glide.with(this@ActivityForImages)
            .load(intent.getStringExtra("url"))
            .into(iv)
    }
}