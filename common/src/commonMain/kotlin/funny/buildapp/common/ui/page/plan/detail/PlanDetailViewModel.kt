package funny.buildapp.common.ui.page.plan.detail

import funny.buildapp.Plans
import funny.buildapp.Todos
import funny.buildapp.common.data.PlanRepository
import funny.buildapp.common.data.TodoRepository
import funny.buildapp.common.ui.page.BaseViewModel
import funny.buildapp.common.utils.currentDate
import kotlinx.coroutines.flow.MutableStateFlow

public class PlanDetailViewModel : BaseViewModel<PlanDetailAction>() {
    private val planRepo by lazy { PlanRepository() }
    private val todoRepo by lazy { TodoRepository() }
    private val _uiState = MutableStateFlow(PlanDetailState())
    public val uiState: MutableStateFlow<PlanDetailState> = _uiState

    override fun dispatch(action: PlanDetailAction) {
        when (action) {
            is PlanDetailAction.GetPlanDetail -> getPlanDetail(action.planId)
            is PlanDetailAction.GetTodos -> getTodos(action.planId)
        }
    }

    private fun getPlanDetail(id: Int) {
        fetchData(
            request = { planRepo.selectById(id.toLong()) },
            onSuccess = {
                _uiState.setState { copy(plan = it) }
                dispatch(PlanDetailAction.GetTodos(id))
            }
        )
    }

    private fun getTodos(id: Int) {
        fetchData(
            request = { todoRepo.selectByPlanId(id.toLong()) },
            onSuccess = {
                _uiState.setState {
                    copy(todos = it)
                }
            }
        )
    }


}

public data class PlanDetailState(
    val plan: Plans = Plans(
        id = 0,
        title = "",
        startDate = currentDate(),
        endDate = currentDate(),
        initialValue = 0,
        targetValue = 100,
        autoAdjust = false,
        state = 0
    ),
    val todos: List<Todos> = emptyList(),
)

public sealed class PlanDetailAction {
    public data class GetPlanDetail(val planId: Int) : PlanDetailAction()
    public data class GetTodos(val planId: Int) : PlanDetailAction()
}