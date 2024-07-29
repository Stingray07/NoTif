package com.example.notif.backend

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
        dbHelper.resetTables()
        insertValues(dbHelper)
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
        var conversationID = dbHelper.returnConversationID(conversationName)
        if (conversationID == null) {
            dbHelper.insertConversation(conversationName, platform)
            conversationID = dbHelper.returnConversationID(conversationName)
        }

        // get userID if not in DB else add to DB and get
        var userID = dbHelper.returnUserID(sender)
        if (userID == null) {
            dbHelper.insertUser(sender)
            userID = dbHelper.returnUserID(sender)
        }

        dbHelper.insertMessage(message, conversationID, userID)

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

        // conversationName looks like this: MyUsername: sender
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

    private fun insertValues(dbHelper: DatabaseHelper) {
        dbHelper.insertUser("TEST")
        dbHelper.insertUser("2nd User")
        dbHelper.insertConversation("TEST CONVO", "PLATFORM")
        dbHelper.insertConversation("2nd Conversation", "MESSENGER")
        dbHelper.insertMessage("2nd CONTENT", 2, 2)
        dbHelper.insertMessage("3rd CONTENT", 2, 2)
        dbHelper.insertMessage("4th CONTENT", 2, 2)
        dbHelper.insertMessage("CONTENT", 1, 1)
    }
}