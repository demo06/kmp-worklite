package funny.buildapp.common

import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Applier
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import funny.buildapp.common.ui.route.Route
import funny.buildapp.common.ui.theme.themeColor
import funny.buildapp.common.widgets.BottomBar

@Composable
internal fun App() {
    Scaffold(
        modifier = Modifier
            .systemBarsPadding(),
        floatingActionButton = {
            FloatingActionButton(
                containerColor = themeColor,
                onClick = {
                }
            ) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = "Localized description",
                    tint = Color.White
                )
            }
        },
        bottomBar = {
            when (Route.HOME) {
                Route.HOME -> BottomBar()
                Route.TODO -> BottomBar()
                Route.SCHEDULE -> BottomBar()
                else -> {}
            }
        },//nothing to do
        content = {
                  Text("this is home")
//            AppNav(navCtrl = navCtrl, padding = it)
        },
    )
}