package funny.buildapp.common.ui.page.schedule.create

import funny.buildapp.common.data.source.plan.Plan
import funny.buildapp.common.data.source.todo.Todo
import funny.buildapp.common.ui.page.BaseViewModel
import funny.buildapp.common.ui.page.DispatchEvent
import kotlinx.coroutines.flow.MutableStateFlow

public class CreateScheduleViewModel : BaseViewModel<CreateScheduleAction>() {


//    private val repo: TodoRepository
//    private val planRepo: PlanRepository
    private val _uiState = MutableStateFlow(CreateScheduleState())
    public val uiState: MutableStateFlow<CreateScheduleState> = _uiState

    override fun dispatch(action: CreateScheduleAction) {
        when (action) {
            is CreateScheduleAction.SendEvent -> _event.sendEvent(action.event)
            is CreateScheduleAction.ChangeBottomSheet -> setDialogState()
            is CreateScheduleAction.GetTodoDetail -> getTodoDetail(action.id)
            is CreateScheduleAction.GetPlans -> getPlans()
            is CreateScheduleAction.Save -> saveTodo()
            is CreateScheduleAction.Delete -> delete()
            is CreateScheduleAction.SetStartDate -> setStartDate(action.time)
            is CreateScheduleAction.SetTargetDate -> setTargetDate(action.time)
            is CreateScheduleAction.SetAssociateState -> setAssociateState()
            is CreateScheduleAction.SetIsRepeat -> setIsRepeat()
            is CreateScheduleAction.SetPlan -> setPlan(action.id)
            is CreateScheduleAction.SetTitle -> setTitle(action.title)
        }
    }

    private fun setPlan(id: Long) {
//        fetchData(
//            request = { planRepo.getPlanDetail(id) },
//            onSuccess = {
//                _uiState.setState {
//                    copy(
//                        todo = todo.copy(
//                            associateId = id,
//                            startDate = it.startDate,
//                            endDate = it.endDate
//                        ),
//                        plan = it,
//                        planBottomSheet = false,
//                        startTime = _uiState.value.todo.startDate,
//                        endTime = _uiState.value.todo.endDate,
//                    )
//                }
//            }
//        )
    }


    private fun getPlans() {
//        fetchData(
//            request = { planRepo.getAll() },
//            onSuccess = { _uiState.setState { copy(plans = it, planBottomSheet = true) } }
//        )
    }

    private fun getPlanDetail(id: Long) {
//        fetchData(
//            request = { planRepo.getPlanDetail(id) },
//            onSuccess = { _uiState.setState { copy(plan = it) } }
//        )
    }

    private fun getTodoDetail(id: Long) {
//        fetchData(
//            request = { repo.getTodoById(id) },
//            onSuccess = {
//                _uiState.setState {
//                    copy(todo = it, startTime = it.startDate, endTime = it.endDate)
//                }
//                if (it.isAssociatePlan && it.associateId != 0L) {
//                    getPlanDetail(it.associateId)
//                }
//            }
//        )
    }

    //
    private fun checkParams(): Boolean {
        if (_uiState.value.todo.title.isEmpty()) {
            "标题不能为空".toast()
            return false
        }
//        if (!compareDate(
//                _uiState.value.todo.startDate.dateToString(),
//                _uiState.value.todo.endDate.dateToString()
//            )
//        ) {
//            "结束时间不能早于开始时间".toast()
//            return false
//        }
        if (_uiState.value.todo.isAssociatePlan && _uiState.value.plan.id.toInt() == 0) {
            "请选择相关联的计划".toast()
            return false
        }
        return true
    }

    private fun saveTodo() {
//        if (!checkParams()) return
//        fetchData(
//            request = {
//                repo.upsert(
//                    todo = _uiState.value.todo.copy(
//                        startDate = if (_uiState.value.todo.repeatable) _uiState.value.plan.startDate else _uiState.value.startTime,
//                        endDate = if (_uiState.value.todo.repeatable) _uiState.value.plan.endDate else _uiState.value.endTime
//                    )
//                )
//            },
//            onSuccess = {
//                increaseTodoCount()
//            },
//            onFailed = {
//                "保存失败".toast()
//            }
//        )
    }


    private fun delete() {
//        fetchData(
//            request = { repo.delete(_uiState.value.todo.id) },
//            onSuccess = {
//                subtractTodoCount()
//                _event.sendEvent(DispatchEvent.Back)
//                "删除成功".toast()
//            },
//            onFailed = {
//                "删除失败".toast()
//            }
//        )
    }


    private fun increaseTodoCount() {
//        fetchData(
//            request = {
//                if (!_uiState.value.todo.isAssociatePlan) {
//                    planRepo.upsert(_uiState.value.plan.copy(targetValue = _uiState.value.plan.targetValue + 1))
//                } else {
//                    if (_uiState.value.todo.repeatable) {// if it's repeatable
//                        val days =
//                            calculateDaysBetweenTwoLongs(
//                                startTime = _uiState.value.plan.startDate,
//                                endTime = _uiState.value.plan.endDate
//                            )
//                        val targetValue = (_uiState.value.plan.targetValue + days).toInt()
//                        planRepo.upsert(_uiState.value.plan.copy(targetValue = targetValue))
//                    } else {
//                        planRepo.upsert(_uiState.value.plan.copy(targetValue = _uiState.value.plan.targetValue + 1))
//                    }
//                }
//            },
//            onSuccess = {
//                _event.sendEvent(DispatchEvent.Back)
//                "保存成功".toast()
//            }, onFailed = { }
//        )
    }

    private fun subtractTodoCount() {
//        if (!_uiState.value.todo.isAssociatePlan) return
//        if (_uiState.value.plan.targetValue == 0) return
//        fetchData(
//            request = {
//                if (_uiState.value.todo.repeatable) {// if it's repeatable
//                    val days =
//                        calculateDaysBetweenTwoLongs(
//                            startTime = _uiState.value.plan.startDate,
//                            endTime = _uiState.value.plan.endDate
//                        )
//                    val targetValue = (_uiState.value.plan.targetValue - days).toInt()
//                    planRepo.upsert(_uiState.value.plan.copy(targetValue = targetValue))
//                } else {
//                    planRepo.upsert(_uiState.value.plan.copy(targetValue = _uiState.value.plan.targetValue - 1))
//                }
//            },
//            onSuccess = {
//            }, onFailed = { }
//        )
    }


    private fun setTitle(title: String) {
        _uiState.setState { copy(todo = _uiState.value.todo.copy(title = title)) }
    }

    private fun setStartDate(date: String) {
//        _uiState.setState {
//            copy(
//                todo = _uiState.value.todo.copy(startDate = time),
//                startTime = time
//            )
//        }
    }

    private fun setTargetDate(date: String) {
//        _uiState.setState {
//            copy(
//                todo = _uiState.value.todo.copy(endDate = time),
//                endTime = time,
//            )
//        }
    }

    private fun setAssociateState() {
        _uiState.setState {
            copy(
                todo = _uiState.value.todo.copy(isAssociatePlan = _uiState.value.todo.isAssociatePlan.not()),
                plan = Plan()
            )
        }
        setIsRepeat()
    }

    private fun setIsRepeat() {
//        _uiState.setState {
//            copy(
//                todo = _uiState.value.todo.copy(
//                    repeatable = _uiState.value.todo.repeatable.not(),
//                    startDate = if (_uiState.value.todo.repeatable) _uiState.value.plan.startDate else _uiState.value.startTime,
//                    endDate = if (_uiState.value.todo.repeatable) _uiState.value.plan.endDate else _uiState.value.endTime,
//                ),
//            )
//        }

    }

    private fun setDialogState() {
        _uiState.setState { copy(planBottomSheet = !_uiState.value.planBottomSheet) }
        if (_uiState.value.planBottomSheet) {
            getTodoDetail(_uiState.value.todo.id)
        }
    }


}


public data class CreateScheduleState(
    val planBottomSheet: Boolean = false,
//    val startTime: Long = getCurrentDate(),
//    val endTime: Long = getCurrentDate(false),
    val todo: Todo = Todo(),
    val plan: Plan = Plan(),
    val plans: List<Plan> = emptyList(),
)

public sealed class CreateScheduleAction {
    public class SendEvent(public val event: DispatchEvent) : CreateScheduleAction()
    public data object Save : CreateScheduleAction()
    public data object ChangeBottomSheet : CreateScheduleAction()
    public data object Delete : CreateScheduleAction()
    public data object GetPlans : CreateScheduleAction()
    public  class GetTodoDetail(public val id: Long) : CreateScheduleAction()
    public  class SetTitle(public val title: String) : CreateScheduleAction()
    public   class SetStartDate(public val time: String) : CreateScheduleAction()
    public  class SetTargetDate(public val time: String) : CreateScheduleAction()
    public  data object SetAssociateState : CreateScheduleAction()
    public data object SetIsRepeat : CreateScheduleAction()
    public  class SetPlan(public val id: Long) : CreateScheduleAction()
}
