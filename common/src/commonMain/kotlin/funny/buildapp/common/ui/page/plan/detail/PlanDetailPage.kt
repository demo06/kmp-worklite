package funny.buildapp.common.ui.page.plan.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import funny.buildapp.common.data.source.todo.Todo
import funny.buildapp.common.ui.page.plan.newPlan.NewPlanPage
import funny.buildapp.common.ui.page.todo.TodoItem
import funny.buildapp.common.ui.route.Route
import funny.buildapp.common.ui.route.RouteUtils
import funny.buildapp.common.ui.route.RouteUtils.back
import funny.buildapp.common.ui.theme.black
import funny.buildapp.common.ui.theme.red2
import funny.buildapp.common.ui.theme.themeColor
import funny.buildapp.common.ui.theme.transparent
import funny.buildapp.common.ui.theme.white
import funny.buildapp.common.widgets.AppToolsBar
import funny.buildapp.common.widgets.CustomBottomSheet
import kotlinx.datetime.Clock
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.viewmodel.viewModel
import kotlin.math.abs


@Composable
public fun PlanDetailPage(navCtrl: Navigator) {
    val viewModel: PlanDetailViewModel = viewModel(PlanDetailViewModel::class) {
        PlanDetailViewModel()
    }
    val uiState by viewModel.uiState.collectAsState()
//    val id = navBackStackEntry?.arguments?.getInt("id") ?: 0
    val id =  0
    val plan = uiState.plan
    val todos = uiState.todos
    var bottomSheet by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = Unit, block = {
        viewModel.dispatch(PlanDetailAction.GetPlanDetail(id))
    })
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(themeColor.copy(0.2f))
    ) {
        Column {
            AppToolsBar(title = "计划详情",
                backgroundColor = transparent,
                tint = themeColor,
                rightText = "编辑",
                onBack = { navCtrl.back() },
                onRightClick = { bottomSheet = !bottomSheet }
            )
            val percentage = plan.initialValue.toDouble() / plan.targetValue.toDouble() * 100
            val progress =20.00
//                if (percentage.isNaN()) 0.00 else String.format("%.1f", percentage).toDouble()
            val lastDay =Clock.System.now().epochSeconds
//                daysBetweenDates(getCurrentDate().dateToString(), plan.endDate.dateToString())
            DetailContent(
//                title = plan.title,
                title = "this is a plant",
                startTime ="2024-1-1",
                endTime = "2024-12-1",
                progress = progress,
                proportion = "${plan.initialValue}/${plan.targetValue}",
                surplus = "${
//                    daysBetweenDates(
//                        getCurrentDate().dateToString(),
//                        plan.endDate.dateToString()
//                    ) 
                      10
                }",
                delay = lastDay
            )
            Schedule(todos, noDataClick = {
                RouteUtils.navTo(navCtrl, Route.CREATE_TODO, 0)
            })
        }
        CustomBottomSheet(
            modifier = Modifier
                .background(black.copy(0.4f))
                .align(Alignment.BottomCenter),
            visible = bottomSheet,
            content = {
                NewPlanPage(
                    navCtrl = navCtrl,
                    isEditMode = true,
                    onDismiss = { bottomSheet = !bottomSheet },
                    id = id,
                )
            })
    }
}

@Composable
public fun DetailContent(
    title: String,
    startTime: String,
    endTime: String,
    progress: Double = 0.00,
    proportion: String,
    surplus: String,
    delay: Long
) {
    LazyColumn(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
    ) {
        item {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 18.sp,
            )
        }
        item { Spacer(modifier = Modifier.padding(vertical = 12.dp)) }
        item {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "从 $startTime  ",
                    color = white,
                )
                Text(
                    text = "到 $endTime",
                    color = white,
                )
            }
        }
        item {
            val text = buildAnnotatedString {
                if (delay < 0) {
                    append("已延期")
                    withStyle(style = SpanStyle(color = red2)) {
                        append(abs(delay).toString())
                    }
                    append("天")
                } else {
                    withStyle(style = SpanStyle(color = red2)) {
                        append(surplus)
                    }
                    append("天后结束  ")
                }
            }
            Text(
                text = text,
                color = white,
            )
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "完成$progress%")
                    Text(text = proportion, color = Color.Gray)
                }
                Spacer(modifier = Modifier.padding(2.dp))
                LinearProgressIndicator(
                    progress = (progress / 100).toFloat(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    color = themeColor,
                    trackColor = themeColor.copy(0.2f)
                )
            }
        }
        item { Spacer(modifier = Modifier.padding(vertical = 10.dp)) }
        item {
            Text(
                text = "关联日程",
            )
        }

    }
}

@Composable
public fun Schedule(todos: List<Todo>, noDataClick: (() -> Unit?)? = null) {
    if (todos.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(
                items = todos,
                key = { it.id },
                itemContent = {
                    TodoItem(
                        selected = it.status == 1,
                        title = it.title,
                        showIcon = false,
                        isRepeatable = it.repeatable,
                    )
                }
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 2.dp, bottom = 8.dp, start = 8.dp, end = 8.dp)
                .background(white, shape = RoundedCornerShape(8.dp)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "赶快去添加日程吧！~",
                modifier = Modifier
                    .clickable { noDataClick?.invoke() }
                    .background(themeColor, RoundedCornerShape(4.dp))
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                color = white,
            )
        }
    }
}