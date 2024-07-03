package com.example.notif

import android.app.Notification
import android.database.sqlite.SQLiteDatabase
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log


class MyNotificationListenerService : NotificationListenerService() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var db: SQLiteDatabase

    override fun onCreate() {
        super.onCreate()
        dbHelper = DatabaseHelper(this)
        db = dbHelper.writableDatabase
        dbHelper.resetTables(db)
        insertValues(dbHelper, db)

        println("CONVERSATION ALREADY IN DB = " + dbHelper.returnConversationID(db, "TEST CONVOS"))
        println("USER ALREADY IN DB = " + dbHelper.returnUserID(db, "TESTS"))

        println(dbHelper.getAllMessages(db))
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)

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

//            dbHelper.insertMessage(db, conversationName, sender, platform, tickerText)
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

    private fun insertValues(dbHelper: DatabaseHelper, db: SQLiteDatabase) {
        dbHelper.insertUser(db, "TEST")
        dbHelper.insertConversation(db, "TEST CONVO", "PLATFORM")
        dbHelper.insertMessage(db, "CONTENT", 0, 0)
    }
}