package com.example.todoapp.database

import app.cash.sqldelight.Query
import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import kotlin.Any
import kotlin.Long
import kotlin.String

public class TodoDatabaseQueries(
  driver: SqlDriver,
) : TransacterImpl(driver) {
  public fun <T : Any> selectAllCategories(mapper: (id: Long, name: String) -> T): Query<T> =
      Query(-1_177_479_113, arrayOf("CategoryEntity"), driver, "TodoDatabase.sq",
      "selectAllCategories", "SELECT * FROM CategoryEntity") { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!
    )
  }

  public fun selectAllCategories(): Query<CategoryEntity> = selectAllCategories { id, name ->
    CategoryEntity(
      id,
      name
    )
  }

  public fun <T : Any> getCategoryById(id: Long, mapper: (id: Long, name: String) -> T): Query<T> =
      GetCategoryByIdQuery(id) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!
    )
  }

  public fun getCategoryById(id: Long): Query<CategoryEntity> = getCategoryById(id) { id_, name ->
    CategoryEntity(
      id_,
      name
    )
  }

  public fun <T : Any> getTaskById(id: Long, mapper: (
    id: Long,
    title: String,
    description: String?,
    completed: Long,
    dueDateTime: Long?,
    createdAt: Long,
    categoryId: Long?,
    notificationId: Long?,
  ) -> T): Query<T> = GetTaskByIdQuery(id) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2),
      cursor.getLong(3)!!,
      cursor.getLong(4),
      cursor.getLong(5)!!,
      cursor.getLong(6),
      cursor.getLong(7)
    )
  }

  public fun getTaskById(id: Long): Query<TaskEntity> = getTaskById(id) { id_, title, description,
      completed, dueDateTime, createdAt, categoryId, notificationId ->
    TaskEntity(
      id_,
      title,
      description,
      completed,
      dueDateTime,
      createdAt,
      categoryId,
      notificationId
    )
  }

  public fun <T : Any> selectAllTasksWithCategory(mapper: (
    id: Long,
    title: String,
    description: String?,
    completed: Long,
    dueDateTime: Long?,
    createdAt: Long,
    categoryId: Long?,
    notificationId: Long?,
    categoryName: String?,
  ) -> T): Query<T> = Query(1_432_436_759, arrayOf("TaskEntity", "CategoryEntity"), driver,
      "TodoDatabase.sq", "selectAllTasksWithCategory", """
  |SELECT TaskEntity.*, CategoryEntity.name AS categoryName 
  |FROM TaskEntity 
  |LEFT JOIN CategoryEntity ON TaskEntity.categoryId = CategoryEntity.id
  |ORDER BY TaskEntity.createdAt DESC
  """.trimMargin()) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2),
      cursor.getLong(3)!!,
      cursor.getLong(4),
      cursor.getLong(5)!!,
      cursor.getLong(6),
      cursor.getLong(7),
      cursor.getString(8)
    )
  }

  public fun selectAllTasksWithCategory(): Query<SelectAllTasksWithCategory> =
      selectAllTasksWithCategory { id, title, description, completed, dueDateTime, createdAt,
      categoryId, notificationId, categoryName ->
    SelectAllTasksWithCategory(
      id,
      title,
      description,
      completed,
      dueDateTime,
      createdAt,
      categoryId,
      notificationId,
      categoryName
    )
  }

  public fun insertCategory(name: String) {
    driver.execute(-29_502_495, """INSERT INTO CategoryEntity(name) VALUES (?)""", 1) {
          bindString(0, name)
        }
    notifyQueries(-29_502_495) { emit ->
      emit("CategoryEntity")
    }
  }

  public fun updateCategory(name: String, id: Long) {
    driver.execute(-482_461_199, """UPDATE CategoryEntity SET name = ? WHERE id = ?""", 2) {
          bindString(0, name)
          bindLong(1, id)
        }
    notifyQueries(-482_461_199) { emit ->
      emit("CategoryEntity")
    }
  }

  public fun deleteCategory(id: Long) {
    driver.execute(889_357_523, """DELETE FROM CategoryEntity WHERE id = ?""", 1) {
          bindLong(0, id)
        }
    notifyQueries(889_357_523) { emit ->
      emit("CategoryEntity")
      emit("TaskEntity")
    }
  }

  public fun insertTask(
    title: String,
    description: String?,
    completed: Long,
    dueDateTime: Long?,
    createdAt: Long,
    categoryId: Long?,
    notificationId: Long?,
  ) {
    driver.execute(436_090_344, """
        |INSERT INTO TaskEntity(title, description, completed, dueDateTime, createdAt, categoryId, notificationId)
        |VALUES (?, ?, ?, ?, ?, ?, ?)
        """.trimMargin(), 7) {
          bindString(0, title)
          bindString(1, description)
          bindLong(2, completed)
          bindLong(3, dueDateTime)
          bindLong(4, createdAt)
          bindLong(5, categoryId)
          bindLong(6, notificationId)
        }
    notifyQueries(436_090_344) { emit ->
      emit("TaskEntity")
    }
  }

  public fun updateTask(
    title: String,
    description: String?,
    completed: Long,
    dueDateTime: Long?,
    categoryId: Long?,
    notificationId: Long?,
    id: Long,
  ) {
    driver.execute(-826_006_536, """
        |UPDATE TaskEntity 
        |SET title = ?, description = ?, completed = ?, dueDateTime = ?, categoryId = ?, notificationId = ?
        |WHERE id = ?
        """.trimMargin(), 7) {
          bindString(0, title)
          bindString(1, description)
          bindLong(2, completed)
          bindLong(3, dueDateTime)
          bindLong(4, categoryId)
          bindLong(5, notificationId)
          bindLong(6, id)
        }
    notifyQueries(-826_006_536) { emit ->
      emit("TaskEntity")
    }
  }

  public fun deleteTask(id: Long) {
    driver.execute(1_234_513_882, """DELETE FROM TaskEntity WHERE id = ?""", 1) {
          bindLong(0, id)
        }
    notifyQueries(1_234_513_882) { emit ->
      emit("TaskEntity")
    }
  }

  private inner class GetCategoryByIdQuery<out T : Any>(
    public val id: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("CategoryEntity", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("CategoryEntity", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(1_504_434_332, """SELECT * FROM CategoryEntity WHERE id = ?""", mapper,
        1) {
      bindLong(0, id)
    }

    override fun toString(): String = "TodoDatabase.sq:getCategoryById"
  }

  private inner class GetTaskByIdQuery<out T : Any>(
    public val id: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("TaskEntity", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("TaskEntity", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-1_879_970_909, """SELECT * FROM TaskEntity WHERE id = ?""", mapper, 1)
        {
      bindLong(0, id)
    }

    override fun toString(): String = "TodoDatabase.sq:getTaskById"
  }
}
