package com.example.notif

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "NoTif.db"

        private const val SQL_CREATE_CONVERSATIONS =
            """
            CREATE TABLE ${DatabaseContract.Conversation.TABLE_NAME} (
            ${DatabaseContract.Conversation.COLUMN_NAME_ID} INTEGER PRIMARY KEY,
            ${DatabaseContract.Conversation.COLUMN_NAME_CONVERSATION_NAME} TEXT,
            ${DatabaseContract.Conversation.COLUMN_NAME_IS_GC} INTEGER,
            ${DatabaseContract.Conversation.COLUMN_NAME_PLATFORM} TEXT
            )
            """

        private const val SQL_CREATE_MESSAGES =
            """
            CREATE TABLE ${DatabaseContract.Message.TABLE_NAME} (
            ${DatabaseContract.Message.COLUMN_NAME_ID} INTEGER PRIMARY KEY,
            ${DatabaseContract.Message.COLUMN_NAME_CONTENT} TEXT,
            ${DatabaseContract.Message.COLUMN_NAME_DATE_TIME} TEXT,
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

    fun insertUser() {
        TODO()
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