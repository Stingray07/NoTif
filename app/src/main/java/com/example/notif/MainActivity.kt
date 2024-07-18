package com.example.notif

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.notif.ui.theme.NoTifTheme

import androidx.compose.material3.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource


class MainActivity : ComponentActivity() {

        private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DatabaseHelper(this)
        val conversations = dbHelper.getAllConversations()

        setContent {
            NoTifTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("Android")
                    ShowConversationList(conversationList = conversations)
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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            modifier = modifier
    )
}

@Composable
fun ShowConversationCard(string: String) {
    Row(modifier = Modifier
        .padding(all = 1.dp)
        .fillMaxWidth()) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = "Contact profile picture",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )

        Column {
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = string)
        }
    }
}

@Composable
fun ShowConversationList(conversationList: List<String>) {
    println("HEHEHE")
    Column {
        for (item in conversationList){
            ShowConversationCard(string = item)
        }
    }
}

@Preview
@Composable
fun ShowButton() {
    // Obtain the context
    val context = LocalContext.current

    // Button composable
    Button(
        onClick = {
            // Show a Toast message on button click
            Toast.makeText(context, "Button Clicked!", Toast.LENGTH_SHORT).show()
        },
        modifier = Modifier.padding(1.dp)
    ) {
        Text(text = "Click Me")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NoTifTheme {
        Greeting("Android")
    }
}



