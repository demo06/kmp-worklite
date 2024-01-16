package funny.buildapp.worklite
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
    // Cannot find skiko-windows-x64.dll.sha256, proper native dependency missing
    database =Database(DriverFactory().createDriver())
    Window(onCloseRequest = ::exitApplication, title = "WorkLite") {
        MaterialTheme(colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()) {
            UIShow()
        }
    }
}
