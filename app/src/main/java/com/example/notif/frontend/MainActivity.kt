package com.example.notif.frontend

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Modifier
import androidx.compose.material3.*
import androidx.compose.ui.unit.dp

import com.example.notif.ui.theme.NoTifTheme
import com.example.notif.backend.DatabaseHelper


class MainActivity : ComponentActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DatabaseHelper(this)
        val conversations = dbHelper.getAllConversations()

        setContent {
            NoTifTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Column {
                        UIComponents.ShowChatText()
                        Spacer(modifier = Modifier.height(10.dp))
                        UIComponents.ShowConversationList(conversationList = conversations)
                    }
                }
            }
        }

        if (!isNotificationServiceEnabled(this)) {
            requestNotificationPermission()
        } else {
            Toast.makeText(this, "Notification Access Granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }

    private fun isNotificationServiceEnabled(context:Context): Boolean {
        val packageName = context.packageName
        val flat = Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")
        return flat != null && flat.contains(packageName)
    }

    private fun requestNotificationPermission() {
        val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        startActivity(intent)
        Toast.makeText(this, "Please enable notification access for this app", Toast.LENGTH_LONG).show()
    }
}



