package com.example.notif.frontend

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.*
import androidx.compose.ui.tooling.preview.Preview

import com.example.notif.ui.theme.NoTifTheme
import com.example.notif.R


object UIComponents {
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
        Column {
            for (item in conversationList){
                ShowConversationCard(string = item)
            }
        }
    }

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

    @Composable
    fun GreetingPreview() {
        NoTifTheme {
            Greeting("Android")
        }
    }
}

@Preview
@Composable
fun ChatText() {
    Text(
        text = "Chats",
    )
}