package com.example.todoapp

import androidx.compose.ui.window.ComposeUIViewController
import com.example.todoapp.data.DatabaseDriverFactory
import com.example.todoapp.data.TodoRepository
import com.example.todoapp.database.TodoDatabase
import com.example.todoapp.notifications.NotificationHelper

fun MainViewController() = ComposeUIViewController {
    val driver = DatabaseDriverFactory().createDriver()
    val database = TodoDatabase(driver)
    val notificationHelper = NotificationHelper()
    val repository = TodoRepository(database, notificationHelper)
    
    App(repository)
}

