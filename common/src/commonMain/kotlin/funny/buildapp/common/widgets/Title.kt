package funny.buildapp.common.widgets

import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import funny.buildapp.common.ui.theme.H3
import funny.buildapp.common.ui.theme.H4
import funny.buildapp.common.ui.theme.H5
import funny.buildapp.common.ui.theme.H6
import funny.buildapp.common.ui.theme.H7
import funny.buildapp.common.ui.theme.black3
import funny.buildapp.common.ui.theme.grey1

@Composable
public fun LargeTitle(
    title: String,
    modifier: Modifier = Modifier,
    color: Color? = null,
    isLoading: Boolean = false
) {
    Title(
        title = title,
        modifier = modifier,
        fontSize = H3,
        color = color ?: black3,
        fontWeight = FontWeight.Bold,
        isLoading = isLoading
    )
}

@Composable
public fun MainTitle(
    title: String,
    modifier: Modifier = Modifier,
    maxLine: Int = 1,
    textAlign: TextAlign = TextAlign.Start,
    color: Color = black3,
    isLoading: Boolean = false
) {
    Title(
        title = title,
        modifier = modifier,
        fontSize = H4,
        color = color,
        fontWeight = FontWeight.SemiBold,
        maxLine = maxLine,
        textAlign = textAlign,
        isLoading = isLoading
    )
}

@Composable
public fun MediumTitle(
    title: String,
    modifier: Modifier = Modifier,
    color: Color = black3,
    textAlign: TextAlign = TextAlign.Start,
    isLoading: Boolean = false
) {
    Title(
        title = title,
        fontSize = H5,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        isLoading = isLoading
    )
}

@Composable
public fun TextContent(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = grey1,
    maxLines: Int = 99,
    textAlign: TextAlign = TextAlign.Start,
    canCopy: Boolean = false,
    isLoading: Boolean = false
) {
    if (canCopy) {
        SelectionContainer {
            Title(
                title = text,
                modifier = modifier,
                fontSize = H6,
                color = color,
                maxLine = maxLines,
                textAlign = textAlign,
                isLoading = isLoading
            )
        }
    } else {
        Title(
            title = text,
            modifier = modifier,
            fontSize = H6,
            color = color,
            maxLine = maxLines,
            textAlign = textAlign,
            isLoading = isLoading
        )
    }

}

@Composable
public fun MiniTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = black3,
    maxLines: Int = 1,
    textAlign: TextAlign = TextAlign.Start,
    isLoading: Boolean = false
) {
    Title(
        title = text,
        modifier = modifier,
        fontSize = H7,
        color = color,
        maxLine = maxLines,
        textAlign = textAlign,
        isLoading = isLoading,
    )
}

@Composable
public fun Title(
    title: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit,
    color: Color = black3,
    fontWeight: FontWeight = FontWeight.Normal,
    maxLine: Int = 1,
    textAlign: TextAlign = TextAlign.Start,
    isLoading: Boolean = false
) {
    Text(
        text = title,
        modifier = modifier,
        fontSize = fontSize,
        color = color,
        maxLines = maxLine,
        overflow = TextOverflow.Ellipsis,
        textAlign = textAlign
    )
}