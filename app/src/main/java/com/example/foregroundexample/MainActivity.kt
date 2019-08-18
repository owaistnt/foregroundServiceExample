package com.example.foregroundexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button).setOnClickListener {
            startUploading()
        }
    }

    private fun startUploading() {
        val serviceInttent = Intent(this, PhotoUploaderSevice::class.java)
        ContextCompat.startForegroundService(this, serviceInttent)
    }
}
