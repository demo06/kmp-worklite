package funny.buildapp.common.ui.page.schedule

import funny.buildapp.common.data.source.todo.Todo
import funny.buildapp.common.ui.page.BaseViewModel
import funny.buildapp.common.ui.page.DispatchEvent
import kotlinx.coroutines.flow.MutableStateFlow

public class ScheduleViewModel : BaseViewModel<ScheduleAction>() {
    //private  val repo: TodoRepository
    private val _uiState = MutableStateFlow(ScheduleState())
    public val uiState: MutableStateFlow<ScheduleState> = _uiState
    override fun dispatch(action: ScheduleAction) {
        when (action) {
            is ScheduleAction.SendEvent -> _event.sendEvent(action.event)
            is ScheduleAction.GetScheduleList -> getTodoList(action.date)
        }
    }

    private fun getTodoList(date: Long) {
//        fetchData(
//            request = { repo.getTodoByDate(date) },
//            onSuccess = {
//                _uiState.setState {
//                    copy(todos = it)
//                }
//            }
//        )
    }


}

public data class ScheduleState(
    val todos: List<Todo> = emptyList(),
)

public sealed class ScheduleAction {
    public class SendEvent(public val event: DispatchEvent) : ScheduleAction()
    public class GetScheduleList(public val date: Long) : ScheduleAction()

}

