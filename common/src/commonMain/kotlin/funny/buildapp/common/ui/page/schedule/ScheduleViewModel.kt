package funny.buildapp.common.ui.page.schedule

import funny.buildapp.Todos
import funny.buildapp.common.data.TodoRepository
import funny.buildapp.common.ui.page.BaseViewModel
import funny.buildapp.common.ui.page.DispatchEvent
import kotlinx.coroutines.flow.MutableStateFlow

public class ScheduleViewModel : BaseViewModel<ScheduleAction>() {
    private val repo by lazy { TodoRepository() }
    private val _uiState = MutableStateFlow(ScheduleState())
    public val uiState: MutableStateFlow<ScheduleState> = _uiState
    override fun dispatch(action: ScheduleAction) {
        when (action) {
            is ScheduleAction.SendEvent -> _event.sendEvent(action.event)
            is ScheduleAction.GetScheduleList -> getTodoList(action.date)
        }
    }

    private fun getTodoList(date: String) {
        fetchData(
            request = { repo.selectByDate(date,date) },
            onSuccess = {
                _uiState.setState {
                    copy(todos = it)
                }
            }
        )
    }


}

public data class ScheduleState(
    val todos: List<Todos> = emptyList(),
)

public sealed class ScheduleAction {
    public class SendEvent(public val event: DispatchEvent) : ScheduleAction()
    public class GetScheduleList(public val date: String) : ScheduleAction()

}

