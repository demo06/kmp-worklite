package funny.buildapp.common.ui.route

import androidx.compose.runtime.Composable
import funny.buildapp.common.ui.page.plan.PlanPage
import funny.buildapp.common.ui.page.plan.detail.PlanDetailPage
import funny.buildapp.common.ui.page.plan.newPlan.NewPlanPage
import funny.buildapp.common.ui.page.schedule.SchedulePage
import funny.buildapp.common.ui.page.schedule.create.CreateTodoPage
import funny.buildapp.common.ui.page.todo.TodoPage
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.path

@Composable
public fun AppNav(navCtrl: Navigator) {
    NavHost(
        navigator = navCtrl,
        initialRoute = Route.HOME,
    ) {
        //home
        scene(route = Route.HOME,/*navTransition= NavTransition(createTransition = fadeIn())*/) {
            PlanPage(navCtrl)
        }
        //new task
        scene(
            route = Route.NEW_PLAN + "/{id}",
//            arguments = listOf(navArgument("id") {
//                type = NavType.IntType
//                defaultValue = 0
//            })
        ) {
            val id = it.path<Int>("id") ?: 0
            NewPlanPage(navCtrl = navCtrl, id = id)
        }
        //task
        scene(route = Route.TODO) {
            TodoPage()
        }
        //detail
        scene(
            route = Route.PLAN_DETAIL + "/{id}",
            // TODO: change do this
//            arguments = listOf(navArgument("id") {
//                type = NavType.IntType
//                defaultValue = 0
//            })
        ) {
//            PlanDetailPage(navCtrl, it)
            PlanDetailPage(navCtrl)
        }
        //schedule
        scene(route = Route.SCHEDULE) {
            SchedulePage(navCtrl)
        }
        //create schedule
        scene(
            route = Route.CREATE_TODO + "/{id}",
            // TODO: change do this
//            arguments = listOf(navArgument("id") {
//                type = NavType.IntType
//                defaultValue = 0
//            })
        ) {
//            CreateTodoPage(navCtrl, it)
            CreateTodoPage(navCtrl)
        }
    }
}

public object Route {
    public const val HOME: String = "home"
    public const val NEW_PLAN: String = "newPlan"
    public const val TODO: String = "todo"
    public const val PLAN_DETAIL: String = "planDetail"
    public const val SCHEDULE: String = "schedule"
    public const val CREATE_TODO: String = "createTodo"
}
