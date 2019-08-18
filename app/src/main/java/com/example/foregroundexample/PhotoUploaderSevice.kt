package com.example.foregroundexample

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class PhotoUploaderSevice : Service() {

    val NOTIFICATION_CHANNEL="photo_uploader_service"


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notification=NotificationCompat.Builder(this, NOTIFICATION_CHANNEL).apply {
            setContentTitle("Uploading  Photos")
            setContentText("initializing...")
            setSmallIcon(R.drawable.ic_cloud_upload_black_24dp)

        }.build()

        startForeground(1, notification)
        return START_NOT_STICKY
    }


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }


    fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel=  NotificationChannel(NOTIFICATION_CHANNEL, "Photo Uploader Service", NotificationManager.IMPORTANCE_DEFAULT)
            (getSystemService(NotificationManager::class.java) as NotificationManager).createNotificationChannel(serviceChannel)
        }
    }
}