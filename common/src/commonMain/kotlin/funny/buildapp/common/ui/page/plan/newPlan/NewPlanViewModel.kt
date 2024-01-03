package funny.buildapp.common.ui.page.plan.newPlan

import funny.buildapp.common.data.source.plan.Plan
import funny.buildapp.common.ui.page.BaseViewModel
import funny.buildapp.common.ui.page.DispatchEvent
import kotlinx.coroutines.flow.MutableStateFlow

public class NewPlanViewModel : BaseViewModel<NewPlanAction>() {
//    private val repo: PlanRepository

    private val _uiState = MutableStateFlow(NewPlanUiState())
    public val uiState: MutableStateFlow<NewPlanUiState> = _uiState

    override fun dispatch(action: NewPlanAction) {
        when (action) {
            is NewPlanAction.SendEvent -> _event.sendEvent(action.event)
            is NewPlanAction.Save -> savePlan()
            is NewPlanAction.Delete -> deletePlan()
            is NewPlanAction.SetTitle -> setTitle(action.title)
            is NewPlanAction.SetStartTime -> setStartTime(action.time)
            is NewPlanAction.SetEndTime -> setEndTime(action.time)
            is NewPlanAction.SetInitialValue -> setInitialValue(action.value)
            is NewPlanAction.SetTargetValue -> setEndValue(action.value)
            is NewPlanAction.SetDialogState -> setDialogState()
            is NewPlanAction.GetPlanDetail -> getPlanDetail(action.id)
            is NewPlanAction.SetAdjustState -> setAdjustState()
        }
    }


    private fun getPlanDetail(id: Int) {
//        fetchData(
//            request = { repo.getPlanDetail(id.toLong()) },
//            onSuccess = {
//                _uiState.setState {
//                    copy(
//                        plan = it,
//                        datePickerDialog = false
//                    )
//                }
//            }
//        )
    }


    private fun checkParams(): Boolean {
        if (_uiState.value.plan.title.isEmpty()) {
            "标题不能为空".toast()
            return false
        }
//        if (!compareDate(
//                _uiState.value.plan.startDate.dateToString(),
//                _uiState.value.plan.endDate.dateToString()
//            )
//        ) {
//            "结束时间不能早于开始时间".toast()
//            return false
//        }
        if (_uiState.value.plan.initialValue > _uiState.value.plan.targetValue) {
            "初始值不能大于目标值".toast()
            return false
        }
        return true
    }

    private fun savePlan() {
//        if (!checkParams()) return
//        fetchData(
//            request = { repo.upsert(_uiState.value.plan) },
//            onSuccess = {
//                "保存成功".toast()
//                _event.sendEvent(DispatchEvent.Back)
//            },
//            onFailed = {
//                "保存失败".toast()
//            }
//        )
    }

    private fun deletePlan() {
//        fetchData(
//            request = { repo.delete(_uiState.value.plan.id) },
//            onSuccess = {
//                "删除成功".toast()
//                _event.sendEvent(DispatchEvent.Back)
//            },
//            onFailed = {
//                "删除失败".toast()
//            }
//        )

    }

    private fun setTitle(title: String) {
        _uiState.setState { copy(plan = _uiState.value.plan.copy(title = title)) }
    }

    private fun setStartTime(time: Long) {
//        _uiState.setState { copy(plan = _uiState.value.plan.copy(startDate = time)) }
    }

    private fun setEndTime(time: Long) {
//        _uiState.setState { copy(plan = _uiState.value.plan.copy(endDate = time)) }
    }

    private fun setInitialValue(value: Int) {
        _uiState.setState { copy(plan = _uiState.value.plan.copy(initialValue = value)) }

    }

    private fun setEndValue(value: Int) {
        _uiState.setState { copy(plan = _uiState.value.plan.copy(targetValue = value)) }
    }

    private fun setDialogState() {
        _uiState.setState { copy(datePickerDialog = !_uiState.value.datePickerDialog) }
    }

    private fun setAdjustState() {
        _uiState.setState { copy(plan = _uiState.value.plan.copy(autoAdjust = !_uiState.value.plan.autoAdjust)) }
    }

}

public data class NewPlanUiState(
    val plan: Plan = Plan(),
    val datePickerDialog: Boolean = false,
)

public sealed class NewPlanAction {
    public class SendEvent(public val event: DispatchEvent) : NewPlanAction()
    public data object Save : NewPlanAction()
    public data object Delete : NewPlanAction()
    public class GetPlanDetail(public val id: Int) : NewPlanAction()
    public data object SetDialogState : NewPlanAction()
    public class SetTitle(public val title: String) : NewPlanAction()
    public class SetStartTime(public val time: Long) : NewPlanAction()
    public class SetEndTime(public val time: Long) : NewPlanAction()
    public class SetInitialValue(public val value: Int) : NewPlanAction()
    public class SetTargetValue(public val value: Int) : NewPlanAction()
    public object SetAdjustState : NewPlanAction()

}