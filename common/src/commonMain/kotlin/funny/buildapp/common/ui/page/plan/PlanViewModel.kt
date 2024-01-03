package funny.buildapp.common.ui.page.plan

import funny.buildapp.common.data.source.plan.Plan
import funny.buildapp.common.ui.page.BaseViewModel
import funny.buildapp.common.ui.page.DispatchEvent
import kotlinx.coroutines.flow.MutableStateFlow

public class PlanViewModel:    BaseViewModel<PlanAction>() {
//    private val repo: PlanRepository
    private val _uiState = MutableStateFlow(PlanUiState())
    public val uiState: MutableStateFlow<PlanUiState> = _uiState

    override fun dispatch(action: PlanAction) {
        when (action) {
            is PlanAction.SendEvent -> _event.sendEvent(action.event)
            is PlanAction.GetAll -> getAll()
        }
    }

    init {
        getAll()
    }

    private fun getAll() {
//        fetchData(
//            request = { repo.getAll() },
//            onSuccess = { _uiState.setState { copy(plans = it) } },
//            onFailed = { _event.sendEvent(DispatchEvent.ShowToast(it)) }
//        )
    }

}


public data class PlanUiState(
    val plans: List<Plan> = emptyList(),
)


public sealed class PlanAction {
    public class SendEvent(public val event: DispatchEvent) : PlanAction()
    public data object GetAll : PlanAction()
}