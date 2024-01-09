package funny.buildapp.common.ui.page.plan.newPlan

import funny.buildapp.Plans
import funny.buildapp.common.data.PlanRepository
import funny.buildapp.common.ui.page.BaseViewModel
import funny.buildapp.common.ui.page.DispatchEvent
import funny.buildapp.common.utils.currentDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.datetime.toLocalDate

public class NewPlanViewModel : BaseViewModel<NewPlanAction>() {
    private val repo: PlanRepository by lazy { PlanRepository() }

    private val _uiState = MutableStateFlow(NewPlanUiState())
    public val uiState: MutableStateFlow<NewPlanUiState> = _uiState

    override fun dispatch(action: NewPlanAction) {
        when (action) {
            is NewPlanAction.SendEvent -> _event.sendEvent(action.event)
            is NewPlanAction.Save -> savePlan()
            is NewPlanAction.Delete -> deletePlan()
            is NewPlanAction.SetTitle -> setTitle(action.title)
            is NewPlanAction.SetStartTime -> setStartTime(action.date)
            is NewPlanAction.SetEndTime -> setEndTime(action.date)
            is NewPlanAction.SetInitialValue -> setInitialValue(action.value)
            is NewPlanAction.SetTargetValue -> setEndValue(action.value)
            is NewPlanAction.SetDialogState -> setDialogState()
            is NewPlanAction.GetPlanDetail -> getPlanDetail(action.id)
            is NewPlanAction.SetAdjustState -> setAdjustState()
        }
    }


    private fun getPlanDetail(id: Int) {
        val plan = repo.selectById(id.toLong())
        _uiState.setState {
            copy(
                plan = plan,
                datePickerDialog = false
            )
        }
    }


    private fun checkParams(): Boolean {
        return if (_uiState.value.plan.title.isEmpty()) {
            "标题不能为空".toast()
            false
        } else if (_uiState.value.plan.startDate.toLocalDate() > _uiState.value.plan.endDate.toLocalDate()) {
            "结束时间不能早于开始时间".toast()
            false
        } else if (_uiState.value.plan.initialValue > _uiState.value.plan.targetValue) {
            "初始值不能大于目标值".toast()
            false
        } else {
            true
        }
    }

    private fun savePlan() {
        if (!checkParams()) return
        repo.insert(_uiState.value.plan)
        _event.sendEvent(DispatchEvent.Back)
    }

    private fun deletePlan() {
        repo.delete(_uiState.value.plan.id)
        _event.sendEvent(DispatchEvent.Back)
    }

    private fun setTitle(title: String) {
        _uiState.setState { copy(plan = _uiState.value.plan.copy(title = title)) }
    }

    private fun setStartTime(date: String) {
        _uiState.setState { copy(plan = _uiState.value.plan.copy(startDate = date)) }
    }

    private fun setEndTime(date: String) {
        _uiState.setState { copy(plan = _uiState.value.plan.copy(endDate = date)) }
    }

    private fun setInitialValue(value: Int) {
        _uiState.setState { copy(plan = _uiState.value.plan.copy(initialValue = value.toLong())) }

    }

    private fun setEndValue(value: Int) {
        _uiState.setState { copy(plan = _uiState.value.plan.copy(targetValue = value.toLong())) }
    }

    private fun setDialogState() {
        _uiState.setState { copy(datePickerDialog = !_uiState.value.datePickerDialog) }
    }

    private fun setAdjustState() {
        _uiState.setState {
            copy(
                plan =
                _uiState.value.plan.copy(autoAdjust = !_uiState.value.plan.autoAdjust)
            )
        }
    }

}

public data class NewPlanUiState(
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
    val datePickerDialog: Boolean = false,
)

public sealed class NewPlanAction {
    public class SendEvent(public val event: DispatchEvent) : NewPlanAction()
    public data object Save : NewPlanAction()
    public data object Delete : NewPlanAction()
    public class GetPlanDetail(public val id: Int) : NewPlanAction()
    public data object SetDialogState : NewPlanAction()
    public class SetTitle(public val title: String) : NewPlanAction()
    public class SetStartTime(public val date: String) : NewPlanAction()
    public class SetEndTime(public val date: String) : NewPlanAction()
    public class SetInitialValue(public val value: Int) : NewPlanAction()
    public class SetTargetValue(public val value: Int) : NewPlanAction()
    public object SetAdjustState : NewPlanAction()

}