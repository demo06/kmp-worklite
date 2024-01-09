package funny.buildapp.common.ui.page.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import funny.buildapp.Todos
import funny.buildapp.common.ui.route.Route
import funny.buildapp.common.ui.route.RouteUtils
import funny.buildapp.common.ui.theme.backgroundColor
import funny.buildapp.common.ui.theme.themeColor
import funny.buildapp.common.ui.theme.white
import funny.buildapp.common.utils.currentDate
import funny.buildapp.common.utils.timeStampToDate
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.viewmodel.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun SchedulePage(navCtrl: Navigator) {
    val viewModel = viewModel(ScheduleViewModel::class) { ScheduleViewModel() }
    val datePickerState = rememberDatePickerState()
    val uiState by viewModel.uiState.collectAsState()
    val todos = uiState.todos
    LaunchedEffect(datePickerState) {
        snapshotFlow { datePickerState.selectedDateMillis }.collect {
            viewModel.dispatch(
                ScheduleAction.GetScheduleList(
                    it?.timeStampToDate() ?: currentDate()
                )
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(themeColor.copy(0.2f))
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(white),
        ) {
            DatePane(datePickerState)
            ScheduleCard(todos = todos, onItemClick = {
                RouteUtils.navTo(
                    navCtrl = navCtrl,
                    route = Route.CREATE_TODO,
                    args = it.toInt(),
                )
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun DatePane(datePickerState: DatePickerState) {
    DatePicker(
        state = datePickerState,
        modifier = Modifier
            .fillMaxWidth()
            .background(themeColor.copy(0.2f)),
        colors = DatePickerDefaults.colors(
            currentYearContentColor = themeColor,
            selectedYearContainerColor = themeColor,
            selectedDayContainerColor = themeColor,
            todayContentColor = themeColor.copy(0.8f),
            todayDateBorderColor = themeColor.copy(0.8f),
        ), title = null, headline = null, showModeToggle = false
    )
}


@Composable
public fun ScheduleCard(todos: List<Todos>, onItemClick: (Long) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .background(white)
            .fillMaxWidth(),
    ) {
        items(
            items = todos,
            key = { it.id },
            itemContent = {
                ScheduleItem(it.title) {
                    onItemClick(it.id)
                }
            }
        )
    }
}

@Composable
public fun ScheduleItem(text: String, onItemClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(16.dp)
        )
        Spacer(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .background(backgroundColor)
                .fillMaxWidth()
                .height(1.dp)
        )
    }

}
