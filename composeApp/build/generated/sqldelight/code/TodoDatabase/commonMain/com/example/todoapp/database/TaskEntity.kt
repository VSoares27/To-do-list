package com.example.todoapp.database

import kotlin.Long
import kotlin.String

public data class TaskEntity(
  public val id: Long,
  public val title: String,
  public val description: String?,
  public val completed: Long,
  public val dueDateTime: Long?,
  public val createdAt: Long,
  public val categoryId: Long?,
  public val notificationId: Long?,
)
