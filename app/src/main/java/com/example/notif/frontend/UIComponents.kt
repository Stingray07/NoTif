package com.example.notif.frontend

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

import com.example.notif.ui.theme.NoTifTheme
import com.example.notif.R


object UIComponents {
    @Composable
    fun ShowConversationCard(string: String, onclick: () -> Unit) {
        Row(modifier = Modifier
            .clickable { onclick() }
            .fillMaxWidth()
            .height(60.dp)) {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "Contact profile picture",
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
            )

            Column {
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = string)
            }
        }
    }

    private fun print() {
        println("Clicked")
    }

    @Composable
    fun ShowConversationList(conversationList: List<String>) {
        LazyColumn{
            items(conversationList) { message ->
                ShowConversationCard(string = message) { print() }
            }
        }
    }

    @Composable
    fun ShowChatText() {
        Text(
            text = "Chats",
            fontSize = 30.sp,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun ChatText() {
    Text(
        text = "Chats",
        fontSize = 50.sp,
        modifier = Modifier
            .fillMaxWidth()
    )
}