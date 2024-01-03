package funny.buildapp.common.ui.page.plan.detail

import funny.buildapp.common.data.source.plan.Plan
import funny.buildapp.common.data.source.todo.Todo
import funny.buildapp.common.ui.page.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow

public class PlanDetailViewModel(
) : BaseViewModel<PlanDetailAction>() {

//    private val repo: PlanRepository,
//    private val todoRepo: TodoRepository
    private val _uiState = MutableStateFlow(PlanDetailState())
    public val uiState: MutableStateFlow<PlanDetailState> = _uiState

    override fun dispatch(action: PlanDetailAction) {
        when (action) {
            is PlanDetailAction.GetPlanDetail -> getPlanDetail(action.planId)
            is PlanDetailAction.GetTodos -> getTodos(action.planId)
        }
    }

    private fun getPlanDetail(id: Int) {
//        fetchData(
//            request = { repo.getPlanDetail(id.toLong()) },
//            onSuccess = {
//                _uiState.setState { copy(plan = it) }
//                dispatch(PlanDetailAction.GetTodos(id))
//            }
//        )
    }

    private fun getTodos(id: Int) {
//        fetchData(
//            request = { todoRepo.getTodoByPlanId(id) },
//            onSuccess = { _uiState.setState {
//                copy(todos = it)
//            } }
//        )
    }


}

public data class PlanDetailState(
    val plan: Plan = Plan(0),
    val todos: List<Todo> = emptyList(),
)

public sealed class PlanDetailAction {
    public data class GetPlanDetail(val planId: Int) : PlanDetailAction()
    public data class GetTodos(val planId: Int) : PlanDetailAction()
}