package com.example.taskthree

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat


class Notification(val context: Context) {

    companion object{
        const val NOTIFICATION_ID = 111
        const val CHANNEL_ID = "channel 228"
        const val NAME = "contact_app"
    }

    fun createNotification(text:String?){
        try{
            createNotificationChannel()
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.phone_icon)
                .setContentTitle("Данные контакта")
                .setContentText(text)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

            val notifyManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notifyManager.notify(NOTIFICATION_ID, builder)
        }
        catch (e: Exception){
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val notifyManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notifyManager.createNotificationChannel(
                NotificationChannel(CHANNEL_ID, NAME,NotificationManager.IMPORTANCE_DEFAULT)
            )
        }

    }


}