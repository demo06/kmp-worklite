package funny.buildapp.common.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import funny.buildapp.common.ui.route.AppNav
import funny.buildapp.common.ui.route.Route
import funny.buildapp.common.ui.route.RouteUtils
import funny.buildapp.common.ui.theme.backgroundGradient
import funny.buildapp.common.ui.theme.themeColor
import funny.buildapp.common.widgets.BottomBar
import moe.tlaster.precompose.navigation.rememberNavigator

@Composable
public fun HomePage() {
    val navCtrl= rememberNavigator()
    val navBackStackEntry by navCtrl.currentEntry.collectAsState(null)
    val currentDestination = navBackStackEntry?.path
    Scaffold(
        modifier = Modifier.background(backgroundGradient)
            .systemBarsPadding(),
        floatingActionButton = {
            if (currentDestination == Route.HOME || currentDestination == Route.SCHEDULE) {
                FloatingActionButton(
                    containerColor = themeColor,
                    onClick = {
                        if (currentDestination == Route.HOME) {
                            RouteUtils.navTo(navCtrl, Route.NEW_PLAN, 0)
                        } else if (currentDestination == Route.SCHEDULE) {
                            RouteUtils.navTo(navCtrl, Route.CREATE_TODO, 0)
                        }

                    }
                ) {
                    Icon(
                        if (currentDestination == Route.HOME) {
                            Icons.Filled.Edit
                        } else {
                            Icons.Filled.AddTask
                        },
                        contentDescription = "Localized description",
                        tint = Color.White
                    )
                }
            }
        },
        bottomBar = {
            when (currentDestination) {
                Route.HOME -> BottomBar(navCtrl)
                Route.TODO -> BottomBar(navCtrl)
                Route.SCHEDULE -> BottomBar(navCtrl)
                else -> {}
            }
        },//nothing to do
        content = {
            AppNav(navCtrl = navCtrl)
        },
    )
}