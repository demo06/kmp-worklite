package funny.buildapp.common

import androidx.compose.runtime.Composable
import funny.buildapp.common.ui.page.HomePage
import moe.tlaster.precompose.PreComposeApp

@Composable
internal fun App() {

   PreComposeApp {
      HomePage()
   }
}