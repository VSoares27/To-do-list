package com.example.todoapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.todoapp.database.CategoryEntity

class CategoryListScreen(private val screenModel: TodoScreenModel) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val categories by screenModel.categories.collectAsState()
        var showAddDialog by remember { mutableStateOf(false) }
        var showEditDialog by remember { mutableStateOf<CategoryEntity?>(null) }
        
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Categories") },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { showAddDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Category")
                }
            }
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                LazyColumn {
                    items(categories) { category ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = category.name, style = MaterialTheme.typography.titleMedium)
                                Row {
                                    IconButton(onClick = { showEditDialog = category }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Edit Category")
                                    }
                                    IconButton(onClick = { screenModel.deleteCategory(category.id) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete Category")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showAddDialog) {
            CategoryDialog(
                initialName = "",
                onConfirm = { name ->
                    screenModel.addCategory(name)
                    showAddDialog = false
                },
                onDismiss = { showAddDialog = false }
            )
        }

        showEditDialog?.let { category ->
            CategoryDialog(
                initialName = category.name,
                onConfirm = { name ->
                    screenModel.updateCategory(category.id, name)
                    showEditDialog = null
                },
                onDismiss = { showEditDialog = null }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDialog(
    initialName: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(initialName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialName.isEmpty()) "Add Category" else "Edit Category") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Category Name") }
            )
        },
        confirmButton = {
            TextButton(
                onClick = { if (name.isNotBlank()) onConfirm(name) }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

