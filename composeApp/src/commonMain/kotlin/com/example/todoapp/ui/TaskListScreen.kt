package com.example.todoapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.todoapp.database.CategoryEntity
import com.example.todoapp.database.SelectAllTasksWithCategory

class TaskListScreen(private val screenModel: TodoScreenModel) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val tasks by screenModel.tasks.collectAsState()
        val categories by screenModel.categories.collectAsState()
        val statusFilter by screenModel.statusFilter.collectAsState()
        val categoryFilter by screenModel.categoryFilter.collectAsState()

        var showFilterDialog by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Tasks") },
                    actions = {
                        IconButton(onClick = { navigator.push(CategoryListScreen(screenModel)) }) {
                            Icon(Icons.Default.Category, contentDescription = "Categories")
                        }
                        IconButton(onClick = { showFilterDialog = true }) {
                            Text("Filter")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { navigator.push(TaskDetailScreen(screenModel, null)) }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Task")
                }
            }
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                if (showFilterDialog) {
                    FilterDialog(
                        currentStatusFilter = statusFilter,
                        currentCategoryFilter = categoryFilter,
                        categories = categories,
                        onStatusSelected = { screenModel.setStatusFilter(it) },
                        onCategorySelected = { screenModel.setCategoryFilter(it) },
                        onDismiss = { showFilterDialog = false }
                    )
                }

                LazyColumn {
                    items(tasks) { task ->
                        TaskItem(
                            task = task,
                            onToggle = { screenModel.toggleTaskCompletion(task) },
                            onClick = { navigator.push(TaskDetailScreen(screenModel, task.id)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: SelectAllTasksWithCategory, onToggle: () -> Unit, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.completed == 1L,
                onCheckedChange = { onToggle() }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (task.completed == 1L) TextDecoration.LineThrough else TextDecoration.None
                )
                if (task.categoryName != null) {
                    Text(
                        text = "Category: ${task.categoryName}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDialog(
    currentStatusFilter: TaskFilter,
    currentCategoryFilter: Long?,
    categories: List<CategoryEntity>,
    onStatusSelected: (TaskFilter) -> Unit,
    onCategorySelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filter Tasks") },
        text = {
            Column {
                Text("Status", style = MaterialTheme.typography.titleSmall)
                Row {
                    TaskFilter.entries.forEach { filter ->
                        FilterChip(
                            selected = currentStatusFilter == filter,
                            onClick = { onStatusSelected(filter) },
                            label = { Text(filter.name) },
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Category", style = MaterialTheme.typography.titleSmall)
                LazyColumn(modifier = Modifier.heightIn(max = 150.dp)) {
                    item {
                        FilterChip(
                            selected = currentCategoryFilter == null,
                            onClick = { onCategorySelected(null) },
                            label = { Text("All") }
                        )
                    }
                    items(categories) { category ->
                        FilterChip(
                            selected = currentCategoryFilter == category.id,
                            onClick = { onCategorySelected(category.id) },
                            label = { Text(category.name) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

