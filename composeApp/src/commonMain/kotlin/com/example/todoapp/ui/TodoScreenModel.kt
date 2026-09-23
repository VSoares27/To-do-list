package com.example.todoapp.ui

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.todoapp.data.TodoRepository
import com.example.todoapp.database.CategoryEntity
import com.example.todoapp.database.SelectAllTasksWithCategory
import com.example.todoapp.database.TaskEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class TaskFilter {
    ALL, PENDING, COMPLETED
}

class TodoScreenModel(private val repository: TodoRepository) : ScreenModel {

    private val _statusFilter = MutableStateFlow(TaskFilter.ALL)
    val statusFilter: StateFlow<TaskFilter> = _statusFilter

    private val _categoryFilter = MutableStateFlow<Long?>(null)
    val categoryFilter: StateFlow<Long?> = _categoryFilter

    val categories: StateFlow<List<CategoryEntity>> = repository.allCategories
        .stateIn(screenModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tasks: StateFlow<List<SelectAllTasksWithCategory>> = combine(
        repository.allTasks,
        _statusFilter,
        _categoryFilter
    ) { tasks, status, categoryId ->
        var filtered = tasks
        if (categoryId != null) {
            filtered = filtered.filter { it.categoryId == categoryId }
        }
        filtered = when (status) {
            TaskFilter.ALL -> filtered
            TaskFilter.PENDING -> filtered.filter { it.completed == 0L }
            TaskFilter.COMPLETED -> filtered.filter { it.completed == 1L }
        }
        filtered
    }.stateIn(screenModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setStatusFilter(filter: TaskFilter) {
        _statusFilter.value = filter
    }

    fun setCategoryFilter(categoryId: Long?) {
        _categoryFilter.value = categoryId
    }

    fun addTask(title: String, description: String?, dueDateTime: Long?, categoryId: Long?) {
        screenModelScope.launch {
            repository.insertTask(title, description, dueDateTime, categoryId)
        }
    }

    fun updateTask(task: TaskEntity) {
        screenModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun deleteTask(id: Long, notificationId: Long?) {
        screenModelScope.launch {
            repository.deleteTask(id, notificationId)
        }
    }

    fun toggleTaskCompletion(taskWithCategory: SelectAllTasksWithCategory) {
        screenModelScope.launch {
            val task = repository.getTaskById(taskWithCategory.id)
            if (task != null) {
                repository.updateTask(task.copy(completed = if (task.completed == 1L) 0L else 1L))
            }
        }
    }

    fun addCategory(name: String) {
        screenModelScope.launch {
            repository.insertCategory(name)
        }
    }

    fun updateCategory(id: Long, name: String) {
        screenModelScope.launch {
            repository.updateCategory(id, name)
        }
    }

    fun deleteCategory(id: Long) {
        screenModelScope.launch {
            repository.deleteCategory(id)
        }
    }

    fun getTaskById(id: Long): TaskEntity? {
        return repository.getTaskById(id)
    }
}

