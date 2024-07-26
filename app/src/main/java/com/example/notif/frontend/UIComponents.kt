package com.example.notif.frontend

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.notif.R
import com.example.notif.backend.Conversation
import com.example.notif.ui.theme.NoTifTheme


object UIComponents {
    @Composable
    fun Navigation(conversationList: List<Conversation>){
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Screen.MainScreen.route, builder = {
            composable(route = Screen.MainScreen.route){
                MainScreen(conversationList = conversationList)
            }
        })
    }

    @Composable
    fun MainScreen(conversationList: List<Conversation>) {
        NoTifTheme {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                Column {
                    ShowChatText()
                    Spacer(modifier = Modifier.height(10.dp))
                    ShowConversationList(conversationList = conversationList)
                }
            }
        }
    }


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

    private fun print(conversationID : Int) {
        println("$conversationID")
    }

    @Composable
    fun ShowConversationList(conversationList: List<Conversation>) {
        LazyColumn{
            items(conversationList) { conversation ->
                ShowConversationCard(string = conversation.conversationName
                ) { print(conversation.id) }
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
