package com.example.foregroundexample

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService


class PhotoUploaderSevice : Service() {

    val NOTIFICATION_CHANNEL = "photo_uploader_service"
    val NOTIFICATION_CHANNEL_LOW = "photo_uploader_service_low"


    val notificationId: Int
    val notificationIdConclusion:Int

    init {
        notificationId = 111;
        notificationIdConclusion=112
    }

    var notificationManager: NotificationManager? = null


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notification = getSimpleNotification().build()

        startForeground(notificationId, notification)
        val initialTimer=object: CountDownTimer(2000, 1000){
            override fun onFinish() {
                val timer = getProgressCountDown()
                timer.start()
            }

            override fun onTick(p0: Long) {}

        }.start()


        return START_NOT_STICKY
    }

    private fun getProgressCountDown(): CountDownTimer {
        return object : CountDownTimer(10000, 1000) {
            override fun onFinish() {
                val updateNotification = getProgressNotification().apply { setProgress(100, 50, true) }.build()
                val updateNotification1 = getCustomNotification(context = applicationContext).build()

                notificationManager?.notify(notificationIdConclusion, updateNotification1)
                stopSelf()

            }

            override fun onTick(p0: Long) {
                val updatedNotificationBundle = getProgressNotification().apply {
                    val progress = (((10000 - p0) * 100) / 10000).toInt()
                    Log.d("TEST", "Millis: $p0 Progress: $progress")
                    setProgress(100, progress, false)
                }

                notificationManager?.notify(notificationId, updatedNotificationBundle.build())

            }

        }
    }

    private fun getSimpleNotification(): NotificationCompat.Builder {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL).apply {
            setContentTitle("Uploading  Photos")
            setContentText("initializing...")
            setSmallIcon(R.drawable.ic_cloud_upload_black_24dp)
            priority = NotificationCompat.PRIORITY_HIGH
            setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_SOUND)
        }
    }


    private fun getProgressNotification(): NotificationCompat.Builder {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_LOW).apply {
            setContentTitle("Uploading  Photos")
            setContentText("progress..")
            setSmallIcon(R.drawable.ic_cloud_upload_black_24dp)
            priority = NotificationCompat.PRIORITY_LOW

        }
    }

    private fun getCustomNotification(context: Context): NotificationCompat.Builder {
        val notificationLayout = RemoteViews(packageName, R.layout.layout_notification_complete)
        val notificationLayoutExpanded = RemoteViews(packageName, R.layout.layout_notification_complete)

        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.ic_cloud_done_white_24dp)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(notificationLayout)
            .setCustomBigContentView(notificationLayoutExpanded)
            .setContent(notificationLayout).apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    priority=NotificationManager.IMPORTANCE_HIGH
                }else{
                    priority = NotificationCompat.PRIORITY_HIGH
                }
                setDefaults(NotificationCompat.DEFAULT_ALL)
                setAutoCancel(true)
            }


    }


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                NOTIFICATION_CHANNEL,
                "Photo Uploader Service",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setShowBadge(true)
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            }

            val serviceChannelLow = NotificationChannel(
                NOTIFICATION_CHANNEL_LOW,
                "Photo Uploader Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                setShowBadge(true)
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            }
            notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager?.createNotificationChannel(serviceChannel)
            notificationManager?.createNotificationChannel(serviceChannelLow)
        }
    }
}