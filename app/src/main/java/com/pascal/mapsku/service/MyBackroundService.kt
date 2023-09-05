package com.pascal.mapsku.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import com.pascal.mapsku.R

class MyBackroundService : Service() {

    private val CHANNEL_ID = "my_channel_id"
    private val NOTIFICATION_ID = 1

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "My Channel Name",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val handler = Handler(Looper.getMainLooper())
        val notificationRunnable = object : Runnable {
            override fun run() {
                Log.e("tag backround", "start")
                sendNotification()
                handler.postDelayed(this, 60000)
            }
        }

        handler.post(notificationRunnable)

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun sendNotification() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("MapsKu")
            .setContentText("Aplikasi Sedang Berjalan...")
            .setSmallIcon(R.drawable.ic_notif)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}
