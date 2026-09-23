package com.example.todoapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.example.todoapp.data.TodoRepository
import com.example.todoapp.ui.TaskListScreen
import com.example.todoapp.ui.TodoScreenModel

@Composable
fun App(repository: TodoRepository) {
    val screenModel = TodoScreenModel(repository)
    
    MaterialTheme {
        Navigator(TaskListScreen(screenModel))
    }
}

