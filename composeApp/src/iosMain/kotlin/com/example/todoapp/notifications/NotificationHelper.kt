package com.example.todoapp.notifications

actual class NotificationHelper {
    actual fun scheduleNotification(taskId: Long, title: String, timeInMillis: Long, notificationId: Int) {
        // iOS UNUserNotificationCenter implementation goes here
    }

    actual fun cancelNotification(notificationId: Int) {
        // iOS UNUserNotificationCenter cancellation goes here
    }
}

