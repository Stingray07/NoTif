package com.example.notif

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.notif.ui.theme.NoTifTheme


class MainActivity : ComponentActivity() {

        private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoTifTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("Android")
                }
            }
        }

        dbHelper = DatabaseHelper(this)
        val db = dbHelper.writableDatabase

        dbHelper.insertUser(db, "Stingray")
        dbHelper.insertConversation(db, "HAHAY", 0, "Instagram")
        dbHelper.insertMessage(db, "TEST MESSAGE", 1, 1)

        println(dbHelper.getAllMessages(db))

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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NoTifTheme {
        Greeting("Android")
    }
}



