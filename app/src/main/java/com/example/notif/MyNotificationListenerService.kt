package com.example.notif

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import java.lang.StringBuilder

class MyNotificationListenerService : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)

        val INSTAGRAM_PACKAGE_NAME = "com.instagram.android"
        val MESSENGER_PACKAGE_NAME = "com.facebook.orca"

        val notification = sbn.notification
        val extras = notification.extras
        val title = extras.getString(Notification.EXTRA_TITLE)
        val tickerText = sbn.notification.tickerText?.toString()
        val packageName = sbn.packageName

        if (title == null || tickerText == null) {
            return
        }

        when (packageName) {
            INSTAGRAM_PACKAGE_NAME -> {
                Log.d("NotificationListener", "From INSTAGRAM")
            }

            MESSENGER_PACKAGE_NAME -> {
                Log.d("NotificationListener", "From MESSENGER")
                val sender = getSender(tickerText)
                Log.d("NotificationListener", "sender = $sender")
            }

            else -> {
                println("Package Not Found")
            }
        }

        Log.d("NotificationListener", "Message Sender: $title")
        Log.d("NotificationListener", "Notification TickerText: $tickerText")
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        println("onNotificationRemoved")
    }

    private fun getSender(tickerText: String): StringBuilder {
        val sender = StringBuilder("")
        for (char in tickerText) {
            if (char == ':') {
                return sender
            }
            sender.append(char)
        }
        return sender
    }

    private fun isFromGroupChat(sender: String, title: String): Boolean {
        return sender == title
    }
}