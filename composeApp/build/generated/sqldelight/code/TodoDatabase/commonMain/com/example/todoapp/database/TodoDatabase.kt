package com.example.todoapp.database

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import com.example.todoapp.database.composeApp.newInstance
import com.example.todoapp.database.composeApp.schema
import kotlin.Unit

public interface TodoDatabase : Transacter {
  public val todoDatabaseQueries: TodoDatabaseQueries

  public companion object {
    public val Schema: SqlSchema<QueryResult.Value<Unit>>
      get() = TodoDatabase::class.schema

    public operator fun invoke(driver: SqlDriver): TodoDatabase =
        TodoDatabase::class.newInstance(driver)
  }
}
