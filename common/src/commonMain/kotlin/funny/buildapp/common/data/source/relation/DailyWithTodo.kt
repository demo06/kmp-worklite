package funny.buildapp.common.data.source.relation

import funny.buildapp.common.data.source.daily.Daily
import funny.buildapp.common.data.source.todo.Todo

public data class DailyWithTodo(
//    @Embedded
    val daily: Daily,
//    @Relation(
//        parentColumn = "todoId",
//        entityColumn = "id"
//    )
    val todo: Todo
)
