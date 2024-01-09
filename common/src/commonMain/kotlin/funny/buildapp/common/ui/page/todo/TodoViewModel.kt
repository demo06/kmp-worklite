package funny.buildapp.common.ui.page.todo

import funny.buildapp.Todos
import funny.buildapp.common.data.TodoRepository
import funny.buildapp.common.ui.page.BaseViewModel
import funny.buildapp.common.utils.currentDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.random.Random

public class TodoViewModel : BaseViewModel<TodoAction>() {
    private val todoRepo: TodoRepository by lazy { TodoRepository() }
    private val _uiState = MutableStateFlow(TodoState())
    public val uiState: MutableStateFlow<TodoState> = _uiState

    override fun dispatch(action: TodoAction) {
        when (action) {
            is TodoAction.Load -> getDailyTodo()
            is TodoAction.UpTodayTask -> updateTodo(action.todo)
        }
    }

    private fun getDailyTodo() {
        fetchData(
            request = { todoRepo.selectByDate(currentDate(), currentDate()) },
            onSuccess = {
                _uiState.setState {
                    copy(todos = it)
                }
            }
        )
    }

    private fun updateTodo(todo: Todos) {
        fetchData(
            request = { todoRepo.update(todo) },
            onSuccess = {
                getDailyTodo()
            }
        )
    }
}

public data class TodoState(
    val str: String = "",
    val todos: List<Todos> = emptyList(),
)

public data class TestTodo(
    val title: String = "this is a todo ",
    val state: Boolean = Random(1).nextBoolean(),
)

public sealed class TodoAction {

    public data object Load : TodoAction()
    public class UpTodayTask(public val todo: Todos) : TodoAction()
}