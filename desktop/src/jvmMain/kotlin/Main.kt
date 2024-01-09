
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import funny.buildapp.Database
import funny.buildapp.common.UIShow
import funny.buildapp.common.database.DriverFactory
import funny.buildapp.common.database.database

fun main() = application {
    database=Database(DriverFactory().createDriver())
    Window(onCloseRequest = ::exitApplication, title = "WorkLite") {
        MaterialTheme(colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()) {
            UIShow()
        }
    }
}
