package com.capstone.belink.Utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.capstone.belink.R
import com.capstone.belink.UIActivity.PopupActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title = remoteMessage.data["title"]
        val body = remoteMessage.data["body"]
        val click_action = remoteMessage.data["click_action"]
        val storeId = remoteMessage.data["storeId"]
        val teamId = remoteMessage.data["teamId"]
        //showNotification(remoteMessage.getData().get("storeId"), remoteMessage.getData().get("isOk"))
        //showNotification(remoteMessage.notification?.title.toString(), remoteMessage.notification?.body.toString())
        showNotification(title, body, click_action,storeId,teamId)
    }
    private fun showNotification(
        title: String?,
        isOk: String?,
        click_action: String?,
        storeId: String?,
        teamId: String?
    ) {
//        val intent = Intent(this, MainActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        lateinit var intent: Intent
        if(click_action.equals("goActivity")) {
            intent = Intent(this, PopupActivity::class.java)
            Log.d("가게 아이디",storeId)
            Log.d("팀 아이디",teamId)
            intent.putExtra("FirebaseStoreId",storeId)
            intent.putExtra("FirebaseTeamId",teamId)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val channelId = "channelid"

        val defaultSoundUri =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder =
                NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(isOk)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)
        val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "name"
            val channel =
                    NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }
}