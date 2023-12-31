package funny.buildapp.common.ui.page.schedule.create

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import funny.buildapp.Plans
import funny.buildapp.common.ui.page.DispatchEvent
import funny.buildapp.common.ui.page.plan.ProgressCard
import funny.buildapp.common.ui.page.plan.newPlan.PlanTitle
import funny.buildapp.common.ui.page.plan.newPlan.TaskItem
import funny.buildapp.common.ui.route.RouteUtils.back
import funny.buildapp.common.ui.theme.backgroundGradient
import funny.buildapp.common.ui.theme.cyan
import funny.buildapp.common.ui.theme.red
import funny.buildapp.common.ui.theme.themeColor
import funny.buildapp.common.ui.theme.transparent
import funny.buildapp.common.ui.theme.white
import funny.buildapp.common.utils.currentDate
import funny.buildapp.common.utils.daysBetweenDates
import funny.buildapp.common.utils.toFraction
import funny.buildapp.common.widgets.AppToolsBar
import funny.buildapp.common.widgets.CustomBottomSheet
import funny.buildapp.common.widgets.FillWidthButton
import funny.buildapp.common.widgets.MyDatePicker
import funny.buildapp.common.widgets.RoundCard
import funny.buildapp.common.widgets.SpaceLine
import funny.buildapp.common.widgets.SwitchButton
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.viewmodel.viewModel


@Composable
public fun CreateTodoPage(navCtrl: Navigator, id: Int = 0) {
    val viewModel = viewModel(CreateScheduleViewModel::class) { CreateScheduleViewModel() }
    val uiState by viewModel.uiState.collectAsState()
    val plan = uiState.plan
    val todo = uiState.todo
    val snackState = remember { SnackbarHostState() }
    var openDialog by remember { mutableStateOf(false) }
    var dialogState by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        if (id != 0) viewModel.dispatch(CreateScheduleAction.GetTodoDetail(id = id.toLong()))
        viewModel.mainEvent.collect {
            when (it) {
                is DispatchEvent.ShowToast -> snackState.showSnackbar(it.msg)
                is DispatchEvent.Back -> navCtrl.back()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .background(backgroundGradient),
        ) {
            item {
                AppToolsBar(
                    title = if (id != 0) "编辑待办" else "添加待办",
                    tint = themeColor,
                    backgroundColor = transparent,
                    onBack = { navCtrl.back() },
                )
            }
            item {
                PlanTitle(
                    text = todo.title,
                    hint = "请在这里输入待办内容",
                    title = "待办内容",
                    onTextChange = {
                        viewModel.dispatch(CreateScheduleAction.SetTitle(it))
                    })
            }
            item {
                Text(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    text = "待办设置",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
            }
            item {
                RoundCard {
                    TaskItem(
                        "执行时间",
                        if (todo.repeatable == true && todo.planId != 0L) plan.startDate
                        else uiState.startTime
                    ) {
                        if (todo.repeatable == false) {
                            dialogState = 0
                            openDialog = !openDialog
                        } else {
                            viewModel.dispatch(
                                CreateScheduleAction.SendEvent(
                                    DispatchEvent.ShowToast(
                                        "已关联计划时间不能修改"
                                    )
                                )
                            )
                        }

                    }
                    SpaceLine()
                    TaskItem(
                        "结束时间",
                        if (todo.repeatable == true && todo.planId != 0L) plan.endDate
                        else uiState.endTime
                    ) {
                        if (todo.repeatable == false) {
                            dialogState = 1
                            openDialog = !openDialog
                        } else {
                            viewModel.dispatch(
                                CreateScheduleAction.SendEvent(
                                    DispatchEvent.ShowToast(
                                        "已关联计划时间不能修改"
                                    )
                                )
                            )
                        }
                    }
                    SpaceLine()
                    TaskItem(
                        title = "关联计划",
                        content = {
                            SwitchButton(
                                modifier = Modifier.height(25.dp),
                                checked = uiState.isRelated,
                                onCheckedChange = {
                                    viewModel.dispatch(CreateScheduleAction.SetAssociateState())
                                })
                        })
                }
            }
            item {
                val percentage =
                    if (plan.initialValue.toInt() == 0 || plan.targetValue.toInt() == 0) {
                        0.0
                    } else {
                        (plan.targetValue.toDouble() / plan.targetValue.toDouble() * 100).toFraction()
                    }
                Column {
                    AnimatedVisibility(visible = uiState.isRelated) {
                        Column {
                            if (plan.id.toInt() != 0) {
                                RoundCard {
                                    TaskItem(
                                        title = plan.title,
                                        content = {
                                            Text(
                                                text = "当前进度：${if (percentage.isNaN()) 0.0 else percentage}%",
                                                fontSize = 14.sp,
                                                color = themeColor,
                                                modifier = Modifier.padding(end = 8.dp)
                                            )
                                        },
                                        onItemClick = { viewModel.dispatch(CreateScheduleAction.GetPlans) })
                                }
                            }
                            AnimatedVisibility(visible = plan.id.toInt() != 0) {
                                RoundCard {
                                    TaskItem("是否在计划内重复", content = {
                                        SwitchButton(
                                            modifier = Modifier.height(25.dp),
                                            checked = todo.repeatable == true,
                                            onCheckedChange = {
                                                viewModel.dispatch(
                                                    CreateScheduleAction.SetIsRepeat
                                                )
                                            })
                                    })
                                }
                            }
                            RoundCard {
                                TaskItem(
                                    "选择计划...",
                                    content = { },
                                    onItemClick = { viewModel.dispatch(CreateScheduleAction.GetPlans) })
                            }
                        }
                    }
                }
            }
            item {
                FillWidthButton(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    text = "保存"
                ) {
                    viewModel.dispatch(CreateScheduleAction.Save)
                }
            }
            if (id != 0) {
                item {
                    FillWidthButton(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        text = "删除",
                        colors = ButtonDefaults.buttonColors(
                            containerColor = themeColor.copy(0.2f),
                        ),
                        fontColor = red.copy(0.6f),
                        onClick = {
                            viewModel.dispatch(CreateScheduleAction.Delete)
                        },
                    )
                }
            }
        }
        if (openDialog) {
            MyDatePicker(
                isStartTime = dialogState == 0,
                onDismiss = {
                    openDialog = !openDialog
                },
                onConfirm = {
                    if (dialogState == 0) {
                        viewModel.dispatch(CreateScheduleAction.SetStartDate(it))
                    } else {
                        viewModel.dispatch(CreateScheduleAction.SetTargetDate(it))
                    }
                })
        }
        CustomBottomSheet(
            modifier = Modifier.align(Alignment.BottomCenter),
            visible = uiState.planBottomSheet,
            content = {
                PlanBottomSheet(
                    onItemClick = { id ->
                        viewModel.dispatch(CreateScheduleAction.SetPlan(id.toLong()))
                    },
                    onDismiss = { viewModel.dispatch(CreateScheduleAction.ChangeBottomSheet) },
                    plans = uiState.plans
                )
            })
        SnackbarHost(hostState = snackState, modifier = Modifier.align(Alignment.BottomCenter))
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
public fun PlanBottomSheet(
    plans: List<Plans>,
    onItemClick: (Int) -> Unit,
    onDismiss: () -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 200.dp)
            .clip(
                RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                )
            )
            .background(cyan),
        content = {
            stickyHeader {
                AppToolsBar(
                    tint = white,
                    title = "选择计划",
                    backgroundColor = cyan,
                    imageVector = Icons.Default.Close,
                    onRightClick = { onDismiss() },
                )
            }
            items(plans, key = { it.id }) {
                val percentage =
                    (it.initialValue.toDouble() / it.targetValue.toDouble() * 100).toFraction()
                val lastDay = daysBetweenDates( it.endDate,currentDate()).toLong()
                ProgressCard(
                    progress = percentage,
                    title = it.title,
                    status = when (it.state) {
                        0L -> "未开始"
                        1L -> "进行中"
                        2L -> "已完成"
                        else -> "未知"
                    },
                    lastDay = lastDay,
                    proportion = "${it.initialValue}/${it.targetValue}",
                    onClick = { onItemClick(it.id.toInt()) })
            }
        })
}

