package com.example.notif

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log


class MyNotificationListenerService : NotificationListenerService() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate() {
        super.onCreate()
        dbHelper = DatabaseHelper(this)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)

        val db = dbHelper.writableDatabase

        try {
            val notification = sbn.notification
            val extras = notification.extras
            val conversationName = extras.getString(Notification.EXTRA_TITLE)
            val tickerText = sbn.notification.tickerText?.toString()
            val packageName = sbn.packageName

            if (conversationName == null || tickerText == null) {
                return
            }

            val (sender, platform) = getSenderAndPlatform(packageName, conversationName, tickerText) ?: return
            val isGC = if (isFromGroupChat(sender, conversationName)) 1 else 0
            println(isGC)

            Log.d("NotificationListener", "Conversation Name: $conversationName")
            Log.d("NotificationListener", "Message Sender: $sender")
            Log.d("NotificationListener", "Notification TickerText: $tickerText")

//            dbHelper.insertMessage(db, conversationName, sender, platform, isGC, tickerText)
        } finally {
            db.close()
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        println("onNotificationRemoved")
    }

    private fun getSender(tickerText: String): String {
        val sender = StringBuilder("")
        for (char in tickerText) {
            if (char == ':') {
                break
            }
            sender.append(char)
        }
        return sender.toString()
    }

    private fun isFromGroupChat(sender: String, title: String): Boolean {
        return sender != title
    }

    private fun getSenderAndPlatform(packageName: String, conversationName: String, tickerText: String): Pair<String, String>? {
        return when (packageName) {
            "com.instagram.android" -> {
                Pair(conversationName, "INSTAGRAM")
            }
            "com.facebook.orca" -> {
                Pair(getSender(tickerText), "MESSENGER")
            }
            else -> {
                Log.d("NotificationListener", "Package Not Found")
                null
            }
        }
    }
}