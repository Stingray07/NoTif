package com.example.notif

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class MyNotificationListenerService : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)
        val notification = sbn.notification
        val extras = notification.extras
        val title = extras.getString(Notification.EXTRA_TITLE)
        var tickerText = sbn.notification.tickerText?.toString()
        val packageName = sbn.packageName

        if (title == null || tickerText == null) {
            return
        }

        // FROM INSTAGRAM
        if (isFromInstagram(packageName)) {
            Log.d("NotificationListener", "From INSTAGRAM")
        }

        // FROM FACEBOOK MESSENGER
        else if (isFromMessenger(packageName)) {
            Log.d("NotificationListener", "From MESSENGER")
            val senderLength = title.length
            val tickerTextStartIndex = senderLength + 2
            tickerText = tickerText.substring(tickerTextStartIndex, tickerText.length)
        }
        else {
            return
        }

        Log.d("NotificationListener", "Message Sender: $title")
        Log.d("NotificationListener", "Notification TickerText: $tickerText")
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        println("onNotificationRemoved")
    }

    private fun isFromInstagram(packageName: String): Boolean {
        val instagramPackageName = "com.instagram.android"
        return instagramPackageName == packageName
    }

    private fun isFromMessenger(packageName: String): Boolean {
        val messengerPackageName = "com.facebook.orca"
        return messengerPackageName == packageName
    }
}