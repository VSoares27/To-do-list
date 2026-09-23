package com.example.todoapp.database.composeApp

import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.AfterVersion
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import com.example.todoapp.database.TodoDatabase
import com.example.todoapp.database.TodoDatabaseQueries
import kotlin.Long
import kotlin.Unit
import kotlin.reflect.KClass

internal val KClass<TodoDatabase>.schema: SqlSchema<QueryResult.Value<Unit>>
  get() = TodoDatabaseImpl.Schema

internal fun KClass<TodoDatabase>.newInstance(driver: SqlDriver): TodoDatabase =
    TodoDatabaseImpl(driver)

private class TodoDatabaseImpl(
  driver: SqlDriver,
) : TransacterImpl(driver), TodoDatabase {
  override val todoDatabaseQueries: TodoDatabaseQueries = TodoDatabaseQueries(driver)

  public object Schema : SqlSchema<QueryResult.Value<Unit>> {
    override val version: Long
      get() = 1

    override fun create(driver: SqlDriver): QueryResult.Value<Unit> {
      driver.execute(null, """
          |CREATE TABLE CategoryEntity (
          |    id INTEGER PRIMARY KEY AUTOINCREMENT,
          |    name TEXT NOT NULL
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE TaskEntity (
          |    id INTEGER PRIMARY KEY AUTOINCREMENT,
          |    title TEXT NOT NULL,
          |    description TEXT,
          |    completed INTEGER NOT NULL DEFAULT 0,
          |    dueDateTime INTEGER,
          |    createdAt INTEGER NOT NULL,
          |    categoryId INTEGER,
          |    notificationId INTEGER,
          |    FOREIGN KEY (categoryId) REFERENCES CategoryEntity(id) ON DELETE SET NULL
          |)
          """.trimMargin(), 0)
      return QueryResult.Unit
    }

    override fun migrate(
      driver: SqlDriver,
      oldVersion: Long,
      newVersion: Long,
      vararg callbacks: AfterVersion,
    ): QueryResult.Value<Unit> = QueryResult.Unit
  }
}
