package com.example.todoapp.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.todoapp.database.CategoryEntity
import com.example.todoapp.database.SelectAllTasksWithCategory
import com.example.todoapp.database.TaskEntity
import com.example.todoapp.database.TodoDatabase
import com.example.todoapp.notifications.NotificationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlin.random.Random

class TodoRepository(
    private val database: TodoDatabase,
    private val notificationHelper: NotificationHelper
) {
    private val queries = database.todoDatabaseQueries

    val allTasks: Flow<List<SelectAllTasksWithCategory>> = 
        queries.selectAllTasksWithCategory()
            .asFlow()
            .mapToList(Dispatchers.IO)

    val allCategories: Flow<List<CategoryEntity>> = 
        queries.selectAllCategories()
            .asFlow()
            .mapToList(Dispatchers.IO)

    fun insertTask(title: String, description: String?, dueDateTime: Long?, categoryId: Long?) {
        val notificationId = Random.nextInt()
        val createdAt = kotlinx.datetime.Clock.System.now().toEpochMilliseconds()
        
        queries.insertTask(
            title = title,
            description = description,
            completed = 0,
            dueDateTime = dueDateTime,
            createdAt = createdAt,
            categoryId = categoryId,
            notificationId = notificationId.toLong()
        )
        
        handleNotification(notificationId, title, dueDateTime)
    }

    fun updateTask(task: TaskEntity) {
        queries.updateTask(
            title = task.title,
            description = task.description,
            completed = task.completed,
            dueDateTime = task.dueDateTime,
            categoryId = task.categoryId,
            notificationId = task.notificationId,
            id = task.id
        )
        
        handleNotification(task.notificationId?.toInt() ?: return, task.title, task.dueDateTime, task.completed == 1L)
    }

    fun deleteTask(id: Long, notificationId: Long?) {
        queries.deleteTask(id)
        notificationId?.let { notificationHelper.cancelNotification(it.toInt()) }
    }

    fun getTaskById(id: Long): TaskEntity? {
        return queries.getTaskById(id).executeAsOneOrNull()
    }

    fun insertCategory(name: String) {
        queries.insertCategory(name)
    }

    fun updateCategory(id: Long, name: String) {
        queries.updateCategory(name, id)
    }

    fun deleteCategory(id: Long) {
        queries.deleteCategory(id)
    }

    private fun handleNotification(notificationId: Int, title: String, dueDateTime: Long?, completed: Boolean = false) {
        notificationHelper.cancelNotification(notificationId)
        
        if (!completed && dueDateTime != null && dueDateTime > kotlinx.datetime.Clock.System.now().toEpochMilliseconds()) {
            notificationHelper.scheduleNotification(
                taskId = 0L, // Placeholder
                title = title,
                timeInMillis = dueDateTime,
                notificationId = notificationId
            )
        }
    }
}

