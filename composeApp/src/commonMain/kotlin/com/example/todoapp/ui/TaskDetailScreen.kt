package com.example.todoapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.todoapp.database.TaskEntity

class TaskDetailScreen(private val screenModel: TodoScreenModel, private val taskId: Long?) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        
        var title by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var completed by remember { mutableStateOf(false) }
        var dueDateTime by remember { mutableStateOf<Long?>(null) }
        var selectedCategoryId by remember { mutableStateOf<Long?>(null) }
        var existingTask by remember { mutableStateOf<TaskEntity?>(null) }

        val categories by screenModel.categories.collectAsState()
        var showCategoryDropdown by remember { mutableStateOf(false) }

        LaunchedEffect(taskId) {
            if (taskId != null) {
                val task = screenModel.getTaskById(taskId)
                if (task != null) {
                    existingTask = task
                    title = task.title
                    description = task.description ?: ""
                    completed = task.completed == 1L
                    dueDateTime = task.dueDateTime
                    selectedCategoryId = task.categoryId
                }
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(if (taskId == null) "New Task" else "Edit Task") },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        if (taskId != null) {
                            IconButton(onClick = {
                                existingTask?.let {
                                    screenModel.deleteTask(it.id, it.notificationId)
                                    navigator.pop()
                                }
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete Task")
                            }
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                // Category Dropdown
                Box {
                    OutlinedButton(onClick = { showCategoryDropdown = true }) {
                        val categoryName = categories.find { it.id == selectedCategoryId }?.name ?: "No Category"
                        Text("Category: $categoryName")
                    }
                    DropdownMenu(
                        expanded = showCategoryDropdown,
                        onDismissRequest = { showCategoryDropdown = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("No Category") },
                            onClick = {
                                selectedCategoryId = null
                                showCategoryDropdown = false
                            }
                        )
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    selectedCategoryId = category.id
                                    showCategoryDropdown = false
                                }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Checkbox(checked = completed, onCheckedChange = { completed = it })
                    Text("Completed")
                }

                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            if (existingTask != null) {
                                screenModel.updateTask(
                                    TaskEntity(
                                        id = existingTask!!.id,
                                        title = title,
                                        description = description.ifBlank { null },
                                        completed = if (completed) 1L else 0L,
                                        dueDateTime = dueDateTime,
                                        createdAt = existingTask!!.createdAt,
                                        categoryId = selectedCategoryId,
                                        notificationId = existingTask!!.notificationId
                                    )
                                )
                            } else {
                                screenModel.addTask(
                                    title = title,
                                    description = description.ifBlank { null },
                                    dueDateTime = dueDateTime,
                                    categoryId = selectedCategoryId
                                )
                            }
                            navigator.pop()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save")
                }
            }
        }
    }
}

