package funny.buildapp.common.ui.route

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.material.icons.rounded.Today
import androidx.compose.ui.graphics.vector.ImageVector


public sealed class BottomNavRoute(
    public var routeName: String,
    public var title: String,
    public var normalIcon: ImageVector,
    public var pressIcon: ImageVector
) {
    public object Home : BottomNavRoute(
        routeName = Route.HOME,
        title = "进度",
        normalIcon = Icons.Rounded.Home,
        pressIcon = Icons.Rounded.Home
    )

    public object Task : BottomNavRoute(
        routeName = Route.TODO,
        title = "待办",
        normalIcon = Icons.Rounded.TaskAlt,
        pressIcon = Icons.Rounded.TaskAlt
    )
    public object Schedule : BottomNavRoute(
        routeName = Route.SCHEDULE,
        title = "日程",
        normalIcon = Icons.Rounded.Today,
        pressIcon = Icons.Rounded.Today
    )
}


public object Route {
    public const val HOME: String = "home"
    public const val NEW_PLAN: String = "newPlan"
    public const val TODO: String = "todo"
    public const val PLAN_DETAIL: String = "planDetail"
    public const val SCHEDULE: String = "schedule"
    public const val CREATE_TODO: String = "createTodo"
}
