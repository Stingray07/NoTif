package com.example.notif

import android.app.Notification
import android.content.Context
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
        val conversationName = extras.getString(Notification.EXTRA_TITLE)
        val tickerText = sbn.notification.tickerText?.toString()
        val packageName = sbn.packageName

        if (conversationName == null || tickerText == null) {
            return
        }

        val sender: StringBuilder

        when (packageName) {
            INSTAGRAM_PACKAGE_NAME -> {
                Log.d("NotificationListener", "From INSTAGRAM")
                sender = StringBuilder(conversationName)
            }

            MESSENGER_PACKAGE_NAME -> {
                Log.d("NotificationListener", "From MESSENGER")
                sender = getSender(tickerText)
            }

            else -> {
                println("Package Not Found")
                return
            }
        }

        if (isFromGroupChat(sender, conversationName)) {
            Log.d("NotificationListener", "IS FROM GC")
        }

        Log.d("NotificationListener", "Conversation Name: $conversationName")
        Log.d("NotificationListener", "Message Sender: $sender")
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

    private fun isFromGroupChat(sender: StringBuilder, title: String): Boolean {
        val stringSender = sender.toString()
        return stringSender != title
    }
}