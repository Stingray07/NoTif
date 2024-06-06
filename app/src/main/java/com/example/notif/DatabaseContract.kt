package com.example.notif
import android.provider.BaseColumns

object DatabaseContract {
    object Conversation: BaseColumns {
        const val TABLE_NAME = "conversation"
        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_CONVERSATION_NAME = "conversationName"
        const val COLUMN_NAME_IS_GC = "isGC"
        const val COLUMN_NAME_PLATFORM = "platform"
    }

    object Message: BaseColumns {
        const val TABLE_NAME = "message"
        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_CONTENT = "content"
        const val COLUMN_NAME_SENDER = "sender"
        const val COLUMN_NAME_CONVERSATION = "conversation"
    }

    object User: BaseColumns {
        const val TABLE_NAME = "user"
        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_USERNAME = "username"
    }
}
