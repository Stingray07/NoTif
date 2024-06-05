package com.example.notif
import android.provider.BaseColumns

object DatabaseContract {
    object ExampleEntry: BaseColumns {
        const val TABLE_NAME = "chat"
    }
}

// Conversation
// ID            ConvName          IsGC?       Platform
// Message
// ID            Content           Sender(User)      Conversation(Conversation)
// User
// ID            Username