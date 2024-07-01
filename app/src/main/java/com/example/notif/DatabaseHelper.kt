package com.example.notif

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "NoTif.db"

        private const val SQL_CREATE_CONVERSATIONS =
            """
            CREATE TABLE ${DatabaseContract.Conversation.TABLE_NAME} (
            ${DatabaseContract.Conversation.COLUMN_NAME_ID} INTEGER PRIMARY KEY,
            ${DatabaseContract.Conversation.COLUMN_NAME_CONVERSATION_NAME} TEXT,
            ${DatabaseContract.Conversation.COLUMN_NAME_PLATFORM} TEXT
            )
            """

        private const val SQL_CREATE_MESSAGES =
            """
            CREATE TABLE ${DatabaseContract.Message.TABLE_NAME} (
            ${DatabaseContract.Message.COLUMN_NAME_ID} INTEGER PRIMARY KEY,
            ${DatabaseContract.Message.COLUMN_NAME_CONTENT} TEXT,
            ${DatabaseContract.Message.COLUMN_NAME_DATE_TIME} TEXT,
            ${DatabaseContract.Message.COLUMN_NAME_SENDER} INTEGER,
            ${DatabaseContract.Message.COLUMN_NAME_CONVERSATION} INTEGER,
            FOREIGN KEY (${DatabaseContract.Message.COLUMN_NAME_SENDER}) REFERENCES ${DatabaseContract.User.TABLE_NAME}(${DatabaseContract.User.COLUMN_NAME_ID}),
            FOREIGN KEY (${DatabaseContract.Message.COLUMN_NAME_CONVERSATION}) REFERENCES ${DatabaseContract.Conversation.TABLE_NAME}(${DatabaseContract.Conversation.COLUMN_NAME_ID})
            )    
            """

        private const val SQL_CREATE_USERS =
            """
            CREATE TABLE ${DatabaseContract.User.TABLE_NAME} (
            ${DatabaseContract.User.COLUMN_NAME_ID} INTEGER PRIMARY KEY,
            ${DatabaseContract.User.COLUMN_NAME_USERNAME} TEXT
            )    
            """


        private const val SQL_DELETE_CONVERSATIONS = "DROP TABLE IF EXISTS ${DatabaseContract.Conversation.TABLE_NAME}"
        private const val SQL_DELETE_MESSAGES = "DROP TABLE IF EXISTS ${DatabaseContract.Message.TABLE_NAME}"
        private const val SQL_DELETE_USERS = "DROP TABLE IF EXISTS ${DatabaseContract.User.TABLE_NAME}"
    }

    fun insertUser(db: SQLiteDatabase, username: String) {
        val SQL_INSERT_QUERY = "INSERT INTO user (username) VALUES (?)".trimIndent()

        val stmt = db.compileStatement(SQL_INSERT_QUERY)
        stmt.bindString(1, username)
        stmt.executeInsert()

        println("INSERT USER SUCCESSFUL")
    }

    fun insertConversation(db: SQLiteDatabase, conversationName: String, platform: String) {
        val SQL_INSERT_QUERY = "INSERT INTO conversation (conversationName, platform) VALUES (?, ?, ?)".trimIndent()

        val stmt = db.compileStatement(SQL_INSERT_QUERY)
        stmt.bindString(1, conversationName)
        stmt.bindString(3, platform)
        stmt.executeInsert()

        println("INSERT CONVERSATION SUCCESSFUL")
    }

    fun returnUserID(db: SQLiteDatabase, username: String): Int? {
        val SQL_SELECT_QUERY = "SELECT id FROM user WHERE username = ?".trimIndent()

        val cursor = db.rawQuery(SQL_SELECT_QUERY, arrayOf(username))
        return if (cursor.moveToFirst()) {
            val userID = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.User.COLUMN_NAME_ID))
            cursor.close()
            userID
        } else {
            cursor.close()
            null
        }
    }

    fun returnConversationID(db: SQLiteDatabase, conversationName: String): Int? {
        val SQL_SELECT_QUERY = "SELECT id FROM conversation where conversationName = ?".trimIndent()

        val cursor = db.rawQuery(SQL_SELECT_QUERY, arrayOf(conversationName))
        return if (cursor.moveToFirst()) {
            val conversationID = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.Conversation.COLUMN_NAME_ID))
            cursor.close()
            conversationID
        } else {
            cursor.close()
            null
        }
    }

    fun insertMessage(db: SQLiteDatabase, content: String, sender: Int, conversation: Int) {
        val SQL_INSERT_QUERY = "INSERT INTO message (content, sender, conversation, datetime) VALUES (?, ?, ?, ?)".trimIndent()

        val stmt = db.compileStatement(SQL_INSERT_QUERY)

        val currentDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(Date())

        stmt.bindString(1, content)
        stmt.bindLong(2, sender.toLong())
        stmt.bindLong(3, conversation.toLong())
        stmt.bindString(4, currentDateTime)
        stmt.executeInsert()

        println("INSERT MESSAGE SUCCESSFUL")
    }

    fun getAllMessages(db: SQLiteDatabase): List<Message> {
        val messages = mutableListOf<Message>()
        val cursor: Cursor = db.query(
            DatabaseContract.Message.TABLE_NAME,  // The table to query
            null,                                 // The columns to return (null means all columns)
            null,                                 // The columns for the WHERE clause
            null,                                 // The values for the WHERE clause
            null,                                 // Group the rows
            null,                                 // Filter by row groups
            null                                  // The sort order
        )

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.Message.COLUMN_NAME_ID))
                val content = getString(getColumnIndexOrThrow(DatabaseContract.Message.COLUMN_NAME_CONTENT))
                val sender = getInt(getColumnIndexOrThrow(DatabaseContract.Message.COLUMN_NAME_SENDER))
                val conversation = getInt(getColumnIndexOrThrow(DatabaseContract.Message.COLUMN_NAME_CONVERSATION))
                val datetime = getString(getColumnIndexOrThrow(DatabaseContract.Message.COLUMN_NAME_DATE_TIME))
                messages.add(Message(id, content, sender, conversation, datetime))
            }
        }
        cursor.close()
        return messages
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_CONVERSATIONS)
        db.execSQL(SQL_CREATE_USERS)
        db.execSQL(SQL_CREATE_MESSAGES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_MESSAGES)
        db.execSQL(SQL_DELETE_CONVERSATIONS)
        db.execSQL(SQL_DELETE_USERS)
        onCreate(db)
    }
}