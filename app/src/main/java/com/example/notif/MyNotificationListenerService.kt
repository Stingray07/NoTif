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
            val message: String

            if (conversationName == null || tickerText == null) {
                return
            }

            val (sender, platform) = getSenderAndPlatform(packageName, conversationName, tickerText) ?: return

            if (platform == "MESSENGER") {
                message = getMessageMessenger(tickerText, sender)
            }

            Log.d("NotificationListener", "Conversation Name: $conversationName")
            Log.d("NotificationListener", "Message Sender: $sender")
            Log.d("NotificationListener", "Notification TickerText: $tickerText")

//            try testing returnUserID and returnConversationID functions

            dbHelper.insertMessage(db, conversationName, sender, platform, tickerText)
        } finally {
            db.close()
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        println("onNotificationRemoved")
    }

    private fun getMessageMessenger(tickerText: String, sender: String): String {
        return tickerText.substring(sender.length + 2)
    }

    private fun getSenderMessenger(tickerText: String): String {
        val sender = StringBuilder("")
        for (char in tickerText) {
            if (char == ':') {
                break
            }
            sender.append(char)
        }
        return sender.toString()
    }

    private fun getSenderInstagram(tickerText: String): String {
        val sender = StringBuilder("")
        var addToSender = false
        for (char in tickerText) {
            if (char == ':') {
                addToSender = true
                continue
            }

            if (addToSender) {
                sender.append(char)
            }
        }
        return sender.toString().substring(1)
    }

    private fun getSenderAndPlatform(packageName: String, conversationName: String, tickerText: String): Pair<String, String>? {
        return when (packageName) {
            "com.instagram.android" -> {
                Pair(getSenderInstagram(conversationName), "INSTAGRAM")
            }

            "com.facebook.orca" -> {
                Pair(getSenderMessenger(tickerText), "MESSENGER")
            }
            else -> {
                Log.d("NotificationListener", "Package Not Found")
                null
            }
        }
    }
}