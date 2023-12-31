package funny.buildapp.common.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

/**
 * Click without wave
 *
 * @param onClick
 * @receiver
 */
public fun Modifier.clickWithoutWave(onClick: () -> Unit): Modifier = composed {
    Modifier.clickable(
        onClick = { onClick.invoke() },
        indication = null,
        interactionSource = remember {
            MutableInteractionSource()
        })
}
