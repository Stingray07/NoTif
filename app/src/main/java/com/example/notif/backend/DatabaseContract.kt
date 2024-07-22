package com.example.notif.backend
import android.provider.BaseColumns

object DatabaseContract {
    object Conversation: BaseColumns {
        const val TABLE_NAME = "conversation"
        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_CONVERSATION_NAME = "conversationName"
        const val COLUMN_NAME_PLATFORM = "platform"
    }

    object Message: BaseColumns {
        const val TABLE_NAME = "message"
        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_CONTENT = "content"
        const val COLUMN_NAME_SENDER = "sender"
        const val COLUMN_NAME_CONVERSATION = "conversation"
        const val COLUMN_NAME_DATE_TIME = "datetime"
    }

    object User: BaseColumns {
        const val TABLE_NAME = "user"
        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_USERNAME = "username"
    }
}

data class Message(
    val id: Int,
    val content: String,
    val platform: Int,
    val conversation: Int,
    val datetime: String
)

data class Conversation(
    val id: Int,
    val conversationName: String
)
