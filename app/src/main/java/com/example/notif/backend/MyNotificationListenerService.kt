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
        val notificationTitle = extras.getString(Notification.EXTRA_TITLE)
        val tickerText = sbn.notification.tickerText?.toString()
        val packageName = sbn.packageName

        if (notificationTitle == null || tickerText == null) {
            return
        }

        val (sender, platform) = getSenderAndPlatform(packageName, notificationTitle, tickerText) ?: return

        Log.d("NotificationListener", "Notification Title: $notificationTitle")
        Log.d("NotificationListener", "Message Sender: $sender")
        Log.d("NotificationListener", "Notification TickerText: $tickerText")

        val message = getMessage(sender, platform, tickerText) ?: return

        // get conversationID if not in DB else add to DB and get
        var conversationID = dbHelper.returnConversationID(notificationTitle)
        if (conversationID == null) {
            val conversationName = getConversationName(notificationTitle, platform)
            if (conversationName != null) {
                dbHelper.insertConversation(conversationName, platform)
            }
            conversationID = conversationName?.let { dbHelper.returnConversationID(it) }
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

    private fun getValuesBeforeSemiColon(text: String): String {

        // text looks like this: sender: message
        // get string values before ':' then return

        val sender = StringBuilder("")
        for (char in text) {
            if (char == ':') {
                break
            }
            sender.append(char)
        }
        return sender.toString()
    }

    private fun getValuesAfterSemiColon(text: String): String {

        // text looks like this: MyUsername: sender
        // get string values after ':' then return

        val sender = StringBuilder("")
        var addToSender = false
        for (char in text) {
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

    private fun getSenderAndPlatform(packageName: String, notificationTitle: String, tickerText: String): Pair<String, String>? =
        when (packageName) {
            "com.instagram.android" -> Pair(getValuesAfterSemiColon(notificationTitle), "INSTAGRAM")
            "com.facebook.orca" -> Pair(getValuesBeforeSemiColon(tickerText), "MESSENGER")
            else -> {
                Log.d("NotificationListener", "Package Not Found")
                null
            }
        }

    private fun getConversationName(notificationTitle: String, platform: String): String? =
        when (platform) {
            "MESSENGER" -> getValuesBeforeSemiColon(notificationTitle)
            "INSTAGRAM" -> getValuesAfterSemiColon(notificationTitle)
            else -> {
                Log.d("NotificationListener", "Message Not Found")
                null
            }
        }

    private fun getMessage(sender: String, platform: String, tickerText: String): String? =
        when (platform) {
            "MESSENGER" -> getMessageMessenger(tickerText, sender)
            "INSTAGRAM" -> tickerText
            else -> {
                Log.d("NotificationListener", "Message Not Found")
                null
            }
        }
}
