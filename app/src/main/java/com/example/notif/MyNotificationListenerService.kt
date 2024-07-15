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

        // initialize dbHelper and db

        dbHelper = DatabaseHelper(this)
        db = dbHelper.writableDatabase
        dbHelper.resetTables(db)
        insertValues(dbHelper, db)

        println(dbHelper.getAllConversations(db))
    }

    override fun onDestroy() {
        super.onDestroy()
        db.close()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)

        // get values from notification
        val notification = sbn.notification
        val extras = notification.extras
        val conversationName = extras.getString(Notification.EXTRA_TITLE)
        val tickerText = sbn.notification.tickerText?.toString()
        val packageName = sbn.packageName

        if (conversationName == null || tickerText == null) {
            return
        }

        val (sender, platform) = getSenderAndPlatform(packageName, conversationName, tickerText) ?: return

        Log.d("NotificationListener", "Conversation Name: $conversationName")
        Log.d("NotificationListener", "Message Sender: $sender")
        Log.d("NotificationListener", "Notification TickerText: $tickerText")

        val message = getMessage(sender, platform, tickerText) ?: return

        // get conversationID if not in DB else add to DB and get
        var conversationID = dbHelper.returnConversationID(db, conversationName)
        if (conversationID == null) {
            dbHelper.insertConversation(db, conversationName, platform)
            conversationID = dbHelper.returnConversationID(db, conversationName)
        }

        // get userID if not in DB else add to DB and get
        var userID = dbHelper.returnUserID(db, sender)
        if (userID == null) {
            dbHelper.insertUser(db, sender)
            userID = dbHelper.returnUserID(db, sender)
        }

        dbHelper.insertMessage(db, message, conversationID, userID)

        println(dbHelper.getAllMessages(db))
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        println("onNotificationRemoved")
    }

    private fun getMessageMessenger(tickerText: String, sender: String): String {
        return tickerText.substring(sender.length + 2)
    }

    private fun getSenderMessenger(tickerText: String): String {

        // tickertext looks like this: sender: message
        // get string values before ':' then return

        val sender = StringBuilder("")
        for (char in tickerText) {
            if (char == ':') {
                break
            }
            sender.append(char)
        }
        return sender.toString()
    }

    private fun getSenderInstagram(conversationName: String): String {

        // conversationName looks like this: username: sender
        // get string values after ':' then return

        val sender = StringBuilder("")
        var addToSender = false
        for (char in conversationName) {
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

    private fun getMessage(sender: String, platform: String, tickerText: String): String? {
        return when (platform) {
            "MESSENGER" -> {
                getMessageMessenger(tickerText, sender)
            }

            "INSTAGRAM" -> {
                tickerText
            }
            else -> {
                Log.d("NotificationListener", "Message Not Found")
                null
            }
        }
    }

    private fun insertValues(dbHelper: DatabaseHelper, db: SQLiteDatabase) {
        dbHelper.insertUser(db, "TEST")
        dbHelper.insertUser(db, "2nd User")
        dbHelper.insertConversation(db, "TEST CONVO", "PLATFORM")
        dbHelper.insertConversation(db, "2nd Conversation", "MESSENGER")
        dbHelper.insertMessage(db, "2nd CONTENT", 2, 2)
        dbHelper.insertMessage(db, "CONTENT", 0, 0)
    }
}