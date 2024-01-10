package funny.buildapp.common.ui.page.plan

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import funny.buildapp.common.ui.route.Route
import funny.buildapp.common.ui.route.RouteUtils
import funny.buildapp.common.ui.theme.H3
import funny.buildapp.common.ui.theme.ToolBarHeight
import funny.buildapp.common.ui.theme.backgroundGradient
import funny.buildapp.common.ui.theme.themeColor
import funny.buildapp.common.utils.currentDate
import funny.buildapp.common.utils.daysBetweenDates
import funny.buildapp.common.utils.toFraction
import funny.buildapp.common.widgets.clickWithoutWave
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.viewmodel.viewModel

@Composable
public fun PlanPage(navCtrl: Navigator) {
    val viewModel = viewModel(PlanViewModel::class) { PlanViewModel() }
    val uiState by viewModel.uiState.collectAsState()
    val plans = uiState.plans
    LaunchedEffect(Unit) {
        viewModel.dispatch(PlanAction.GetAll)
    }
    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        item {
            ScheduleToolBar(title = "项目进度")
        }
        items(
            items = plans,
            key = { it.id },
            itemContent = {
                val percentage =
                    if (it.initialValue == 0L || it.targetValue == 0L) {
                        0.0
                    } else {
                        (it.initialValue.toDouble() / it.targetValue.toDouble() * 100).toFraction()
                    }
                val lastDay =
                    daysBetweenDates(it.endDate, currentDate())
                ProgressCard(
                    progress = percentage,
                    title = it.title,
                    status = when (it.state.toInt()) {
                        0 -> "未开始"
                        1 -> "进行中"
                        2 -> "已完成"
                        else -> "未知"
                    },
                    lastDay = lastDay.toLong(),
                    proportion = "${it.initialValue}/${it.targetValue}",
                    onClick = { RouteUtils.navTo(navCtrl, Route.PLAN_DETAIL, it.id) }
                )
            },
        )
        item {
            Box(Modifier.fillMaxWidth().height(70.dp))
        }
    }
}

@Composable
public fun ScheduleToolBar(modifier: Modifier = Modifier, title: String) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(ToolBarHeight + 40.dp),
    ) {
        Text(
            text = title,
            fontSize = H3,
            color = themeColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
public fun ProgressCard(
    progress: Double = 0.00,
    title: String = "",
    status: String = "",
    proportion: String = "0/0",
    lastDay: Long = 0,
    onClick: () -> Unit = {}
) {
    val isShowPlaceHolder by remember {
        mutableStateOf(false)
    }
    Column(
        Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(8.dp))
            .clickWithoutWave { onClick() }
            .padding(12.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
            /*.placeholder(isShowPlaceHolder)*/,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = status, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Icon(Icons.Rounded.ArrowRight, contentDescription = "")
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
            /*.placeholder(isShowPlaceHolder)*/,
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (lastDay < 0) "已延期" else if (lastDay == 0L) "今" else "$lastDay",
                fontSize = 12.sp,
                color = Color.Red
            )
            if (lastDay >= 0) {
                Text(text = "天后结束", fontSize = 12.sp, color = Color.Gray)
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
            /*.placeholder(isShowPlaceHolder)*/,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "完成${if (progress.isNaN()) 0.0 else progress}%", fontSize = 12.sp)
            Text(text = proportion, fontSize = 12.sp, color = Color.Gray)
        }
        Spacer(modifier = Modifier.padding(2.dp))
        LinearProgressIndicator(
            progress = if (progress.isNaN()) 0.0f else (progress / 100).toFloat(),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
            /*.placeholder(isShowPlaceHolder)*/,
            color = themeColor,
            trackColor = themeColor.copy(0.2f)
        )
    }
}
