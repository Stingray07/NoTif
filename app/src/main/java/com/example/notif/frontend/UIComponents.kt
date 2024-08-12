package com.example.notif.frontend

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Surface
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.notif.R
import com.example.notif.backend.Conversation
import com.example.notif.backend.Message
import com.example.notif.backend.DatabaseHelper


object UIComponents {
    @Composable
    fun Navigation(dbHelper: DatabaseHelper){
        val conversationList = dbHelper.getAllConversations()
        println(conversationList)
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Screen.MainScreen.route, builder = {
            composable(route = Screen.MainScreen.route){
                MainScreen(conversationList = conversationList, navController = navController)
            }

            composable(
                route = Screen.ChatScreen.route + "/{id}",
                arguments = listOf(
                    navArgument("id") { type = NavType.StringType }
                )
            ){ entry ->
                val id = entry.arguments?.getString("id")
                if (id != null) {
                    ChatScreen(id, dbHelper)
                }
            }
        })
    }

    @Composable
    fun MainScreen(conversationList: List<Conversation>, navController: NavController) {
            Column {
                ShowChatText()
                Spacer(modifier = Modifier.height(10.dp))
                ShowConversationList(conversationList = conversationList, navController)
            }
    }

    @Composable
    fun ChatScreen(conversationID: String, dbHelper: DatabaseHelper){
        val messageList = dbHelper.getMessagesByConversation(conversationID)
        ShowConversation(messageList = messageList)
    }

    @Composable
    fun ShowConversationCard(conversation: Conversation, navController: NavController) {
        val conversationID = conversation.id
        Row(modifier = Modifier
            .clickable { navController.navigate(Screen.ChatScreen.withArgs(conversationID)) }
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
                Text(text = conversation.conversationName)
            }
        }
    }

    @Composable
    fun ShowMessageCard(message: Message) {
        Row(modifier = Modifier.padding(all = 8.dp)) {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = message.sender,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleSmall
                )

                Spacer(modifier = Modifier.height(4.dp))

                Surface(shape = MaterialTheme.shapes.medium, shadowElevation = 1.dp) {
                    Text(
                        text = message.content,
                        modifier = Modifier.padding(all = 4.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }

    @Composable
    fun ShowConversation(messageList: List<Message>) {
        LazyColumn{
            items(messageList) {message ->
                ShowMessageCard(message)
            }
        }
    }

    @Composable
    fun ShowConversationList(conversationList: List<Conversation>, navController: NavController) {
        LazyColumn{
            items(conversationList) { conversation ->
                ShowConversationCard(conversation= conversation, navController)
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
