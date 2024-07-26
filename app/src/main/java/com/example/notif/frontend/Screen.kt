package com.example.notif.frontend

sealed class Screen(val route: String) {
    data object MainScreen: Screen("main_screen")
    data object ChatScreen: Screen("chat_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}