package com.example.foregroundexample

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class PhotoUploaderSevice : Service() {

    val NOTIFICATION_CHANNEL="photo_uploader_service"

    val notificationId: Int
    init {
        notificationId=111;
    }

    var notificationManager: NotificationManager?=null

    override fun onCreate() {
        super.onCreate()

    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        notificationManager=ContextCompat.getSystemService(this, NotificationManager::class.java)
        val notification= getSimpleNotification().build()

        startForeground(notificationId, notification)
        val timer=object: CountDownTimer(10000, 1000){
            override fun onFinish() {
                    val updateNotification=getProgressNotification().apply { setProgress(100, 50, true) }.build()
                    notificationManager?.notify(notificationId, updateNotification)
            }

            override fun onTick(p0: Long) {
               val updatedNotificationBundle= getProgressNotification().apply {
                   val progress=(((10000-p0)*100)/10000).toInt()
                   Log.d("TEST", "Millis: $p0 Progress: $progress")
                    setProgress(100, progress,  false)
                }
                notificationManager?.notify(notificationId, updatedNotificationBundle.build())

            }

        }

        timer.start()

        return START_NOT_STICKY
    }

    private fun getSimpleNotification(): NotificationCompat.Builder {
       return  NotificationCompat.Builder(this, NOTIFICATION_CHANNEL).apply {
            setContentTitle("Uploading  Photos")
            setContentText("initializing...")
            setSmallIcon(R.drawable.ic_cloud_upload_black_24dp)
        }
    }


    private fun getProgressNotification(): NotificationCompat.Builder {
        return  NotificationCompat.Builder(this, NOTIFICATION_CHANNEL).apply {
            setContentTitle("Uploading  Photos")
            setContentText("progress..")
            setSmallIcon(R.drawable.ic_cloud_upload_black_24dp)

        }
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