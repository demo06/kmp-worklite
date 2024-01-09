package funny.buildapp.common.data

import funny.buildapp.Todos
import funny.buildapp.common.database.database

public class TodoRepository {
    public  fun insert(todo: Todos): Unit = database.todoQueries.insert(todo)

    public fun delete(id: Long): Unit = database.todoQueries.delete(id)

    public fun update(todo: Todos): Unit = database.todoQueries.update(
        title = todo.title,
        startDate = todo.startDate,
        endDate = todo.endDate,
        planId = todo.planId,
        repeatable = todo.repeatable,
        state = todo.state,
        id = todo.id
    )

    public fun selectById(id: Long): Todos = database.todoQueries.selectById(id).executeAsOne()


}

