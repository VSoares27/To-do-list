package com.example.todoapp.notifications

expect class NotificationHelper {
    fun scheduleNotification(taskId: Long, title: String, timeInMillis: Long, notificationId: Int)
    fun cancelNotification(notificationId: Int)
}

