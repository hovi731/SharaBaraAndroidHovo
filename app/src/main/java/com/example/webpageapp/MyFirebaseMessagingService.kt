package com.example.webpageapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId = "notification_channel"
const val channelName = "com.thewise.sharabara"

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remotrMessage: RemoteMessage) {
        if(remotrMessage.getNotification() != null){
            generateNotification(remotrMessage.notification!!.title!!,remotrMessage.notification!!.body!!)
        }
    }


    fun getRemoteView(title: String, message: String): RemoteViews{
        val remoteView = RemoteViews("com.thewise.sharabara", R.layout.notification)
        remoteView.setTextViewText(R.id.title,title)
        remoteView.setTextViewText(R.id.message,message)
        remoteView.setImageViewResource(R.id.app_logo,R.drawable.shb)

        return remoteView
    }

    fun generateNotification(title: String, message: String){

        val intent = Intent(this, WebPageActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)

        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.shb)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
        builder =builder.setContent(getRemoteView(title,message,))


        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notivicationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notivicationChannel)

        }

        notificationManager.notify(0,builder.build())


    }

}