package funny.buildapp.common.ui.page.todo

import funny.buildapp.common.data.TodoRepository
import funny.buildapp.common.data.source.daily.Daily
import funny.buildapp.common.ui.page.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.random.Random

public class TodoViewModel : BaseViewModel<TodoAction>() {
private val todoRepo : TodoRepository = TodoRepository()
    private val _uiState = MutableStateFlow(TodoState())
    public val uiState: MutableStateFlow<TodoState> = _uiState

    override fun dispatch(action: TodoAction) {
        when (action) {
            is TodoAction.Load -> getDailyTodo()
            is TodoAction.UpTodayTask -> upsertDaily(action.daily)
        }
    }

    private fun getDailyTodo() {
//        todoRepo.getAll()
//        val a=todoRepo.select()
//        println(a)
//        _uiState.setState {
//            copy(str=a.toString())
//        }
//        fetchData(
//            request = { todoRepo.getTodoFormDaily() },
//            onSuccess = {
//                _uiState.setState {
//                    copy(todos = it)
//                }
//            }
//        )
        _uiState.setState {
            copy(todos = listOf(TestTodo(),TestTodo(),TestTodo(),TestTodo(),TestTodo(),))
        }
    }

    private fun upsertDaily(daily: Daily) {
//        fetchData(
//            request = { todoRepo.upsertDaily(daily.copy(state = !daily.state)) },
//            onSuccess = {
//                getDailyTodo()
//            }
//        )
    }
}

public data class TodoState(
    val str:String="",
//    val todos: List<DailyWithTodo> = emptyList(),
    val todos: List<TestTodo> = emptyList(),
)

public data class TestTodo(
    val title:String="this is a todo ",
    val state:Boolean= Random(1).nextBoolean(),
)
public sealed class TodoAction {

    public data object Load : TodoAction()
    public class UpTodayTask(public val daily: Daily) : TodoAction()
}