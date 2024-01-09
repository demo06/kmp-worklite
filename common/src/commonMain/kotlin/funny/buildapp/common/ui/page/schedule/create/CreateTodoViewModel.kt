package funny.buildapp.common.ui.page.schedule.create

import funny.buildapp.Plans
import funny.buildapp.Todos
import funny.buildapp.common.data.PlanRepository
import funny.buildapp.common.data.TodoRepository
import funny.buildapp.common.ui.page.BaseViewModel
import funny.buildapp.common.ui.page.DispatchEvent
import funny.buildapp.common.utils.currentDate
import funny.buildapp.common.utils.daysBetweenDates
import kotlinx.coroutines.flow.MutableStateFlow

public class CreateScheduleViewModel : BaseViewModel<CreateScheduleAction>() {
    private val repo: TodoRepository by lazy { TodoRepository() }
    private val planRepo: PlanRepository by lazy { PlanRepository() }
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
        planRepo.selectById(id)
        fetchData(
            request = { planRepo.selectById(id) },
            onSuccess = {
                _uiState.setState {
                    copy(
                        todo = todo.copy(
                            planId = id,
                            startDate = it.startDate,
                            endDate = it.endDate
                        ),
                        plan = it,
                        planBottomSheet = false,
                        startTime = _uiState.value.todo.startDate,
                        endTime = _uiState.value.todo.endDate,
                    )
                }
            }
        )
    }


    private fun getPlans() {
        fetchData(
            request = { planRepo.selectAll() },
            onSuccess = { _uiState.setState { copy(plans = it, planBottomSheet = true) } }
        )
    }

    private fun getPlanDetail(id: Long) {
        fetchData(
            request = { planRepo.selectById(id) },
            onSuccess = { _uiState.setState { copy(plan = it) } }
        )
    }

    private fun getTodoDetail(id: Long) {
        fetchData(
            request = { repo.selectById(id) },
            onSuccess = {
                _uiState.setState {
                    copy(todo = it, startTime = it.startDate, endTime = it.endDate)
                }
                if (it.planId != 0L) {
                    getPlanDetail(it.planId ?: 0L)
                }
            }
        )
    }

    //
    private fun checkParams(): Boolean {
        return if (_uiState.value.todo.title.isEmpty()) {
            "标题不能为空".toast()
            false
        } else if (_uiState.value.todo.startDate > _uiState.value.todo.endDate) {
            "结束时间不能早于开始时间".toast()
            false
        } else if (_uiState.value.todo.planId != 0L && _uiState.value.plan.id.toInt() == 0) {
            "请选择相关联的计划".toast()
            false
        } else {
            true
        }
    }

    private fun saveTodo() {
        if (!checkParams()) return
        fetchData(
            request = {
                repo.insert(
                    todo = _uiState.value.todo.copy(
                        startDate = if (_uiState.value.todo.repeatable == true) _uiState.value.plan.startDate else _uiState.value.startTime,
                        endDate = if (_uiState.value.todo.repeatable == true) _uiState.value.plan.endDate else _uiState.value.endTime
                    )
                )
            },
            onSuccess = {
                increaseTodoCount()
            },
            onFailed = {
                "保存失败".toast()
            }
        )
    }


    private fun delete() {
        fetchData(
            request = { repo.delete(_uiState.value.todo.id) },
            onSuccess = {
                subtractTodoCount()
                _event.sendEvent(DispatchEvent.Back)
                "删除成功".toast()
            },
            onFailed = {
                "删除失败".toast()
            }
        )
    }


    private fun increaseTodoCount() {
        fetchData(
            request = {
                if (_uiState.value.todo.planId != 0L) {
                    planRepo.update(_uiState.value.plan.copy(targetValue = _uiState.value.plan.targetValue + 1))
                } else {
                    if (_uiState.value.todo.repeatable == true) {// if it's repeatable
                        val days = daysBetweenDates(
                            endDate = _uiState.value.plan.endDate,
                            startDate = _uiState.value.plan.startDate
                        )
                        val targetValue = (_uiState.value.plan.targetValue + days)
                        planRepo.update(_uiState.value.plan.copy(targetValue = targetValue))
                    } else {
                        planRepo.update(_uiState.value.plan.copy(targetValue = _uiState.value.plan.targetValue + 1))
                    }
                }
            },
            onSuccess = {
                _event.sendEvent(DispatchEvent.Back)
                "保存成功".toast()
            }, onFailed = { }
        )
    }

    private fun subtractTodoCount() {
        if (_uiState.value.todo.planId == 0L) return
        if (_uiState.value.plan.targetValue == 0L) return
        fetchData(
            request = {
                if (_uiState.value.todo.repeatable == true) {// if it's repeatable
                    val days = daysBetweenDates(
                        endDate = _uiState.value.plan.endDate,
                        startDate = _uiState.value.plan.startDate
                    )
                    val targetValue = (_uiState.value.plan.targetValue - days).toInt()
                    planRepo.update(_uiState.value.plan.copy(targetValue = targetValue.toLong()))
                } else {
                    planRepo.update(_uiState.value.plan.copy(targetValue = _uiState.value.plan.targetValue - 1))
                }
            },
            onSuccess = {
            }, onFailed = { }
        )
    }


    private fun setTitle(title: String) {
        _uiState.setState { copy(todo = _uiState.value.todo.copy(title = title)) }
    }

    private fun setStartDate(date: String) {
        _uiState.setState {
            copy(
                todo = _uiState.value.todo.copy(startDate = date),
                startTime = date
            )
        }
    }

    private fun setTargetDate(date: String) {
        _uiState.setState {
            copy(
                todo = _uiState.value.todo.copy(endDate = date),
                endTime = date,
            )
        }
    }

    private fun setAssociateState() {
        _uiState.setState {
            copy(
                isRelated = !_uiState.value.isRelated,
            )
        }
        setIsRepeat()
    }

    private fun setIsRepeat() {
        _uiState.setState {
            copy(
                todo = _uiState.value.todo.copy(
                    repeatable = _uiState.value.todo.repeatable?.not(),
                    startDate = if (_uiState.value.todo.repeatable == true) _uiState.value.plan.startDate else _uiState.value.startTime,
                    endDate = if (_uiState.value.todo.repeatable == true) _uiState.value.plan.endDate else _uiState.value.endTime,
                ),
            )
        }

    }

    private fun setDialogState() {
        _uiState.setState { copy(planBottomSheet = !_uiState.value.planBottomSheet) }
        if (_uiState.value.planBottomSheet) {
            getTodoDetail(_uiState.value.todo.id)
        }
    }


}


public data class CreateScheduleState(
    val isRelated: Boolean = false,
    val planBottomSheet: Boolean = false,
    val startTime: String = currentDate(),
    val endTime: String = currentDate(),
    val todo: Todos = Todos(
        id = 0,
        title = "",
        startDate = currentDate(),
        endDate = currentDate(),
        planId = 0,
        repeatable = false,
        state = 0
    ),
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
    val plans: List<Plans> = emptyList(),
)

public sealed class CreateScheduleAction {
    public class SendEvent(public val event: DispatchEvent) : CreateScheduleAction()
    public data object Save : CreateScheduleAction()
    public data object ChangeBottomSheet : CreateScheduleAction()
    public data object Delete : CreateScheduleAction()
    public data object GetPlans : CreateScheduleAction()
    public class GetTodoDetail(public val id: Long) : CreateScheduleAction()
    public class SetTitle(public val title: String) : CreateScheduleAction()
    public class SetStartDate(public val time: String) : CreateScheduleAction()
    public class SetTargetDate(public val time: String) : CreateScheduleAction()
    public class SetAssociateState() : CreateScheduleAction()
    public data object SetIsRepeat : CreateScheduleAction()
    public class SetPlan(public val id: Long) : CreateScheduleAction()
}
