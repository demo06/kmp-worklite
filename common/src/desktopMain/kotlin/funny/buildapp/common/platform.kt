package funny.buildapp.common

import androidx.compose.runtime.Composable

public actual fun getPlatformName(): String {
    return "worklite"
}

@Composable
public fun UIShow() {

    App()
}