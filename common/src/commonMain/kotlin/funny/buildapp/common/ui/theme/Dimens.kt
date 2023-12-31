package funny.buildapp.common.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


public val ToolBarHeight: Dp = 48.dp
public val TabBarHeight: Dp = 48.dp
public val SearchBarHeight: Dp = 42.dp
public val BottomNavBarHeight: Dp = 56.dp
public val ListTitleHeight: Dp = 30.dp

public val PrimaryButtonHeight: Dp = 36.dp
public val MediumButtonHeight: Dp = 28.dp
public val SmallButtonHeight: Dp = 28.dp


public val H1: TextUnit = 48.sp  //超大号标题
public val H2: TextUnit = 36.sp  //大号标题
public val H3: TextUnit = 24.sp  //主标题
public val H4: TextUnit = 20.sp  //普通标题
public val H5: TextUnit = 16.sp  //内容文本
public val H6: TextUnit = 14.sp  //普通文字尺寸
public val H7: TextUnit = 12.sp  //提示语尺寸

public val ToolBarTitleSize: TextUnit = 18.sp

public val PaddingBorder: Dp = 12.dp

public val cardCorner: Dp = 5.dp   //卡片的圆角
public val buttonCorner: Dp = 3.dp //按钮的圆角
public val buttonHeight: Dp = 36.dp //按钮的高度

public val backgroundGradient: Brush =
    Brush.verticalGradient(listOf(themeColor.copy(0.2f), Color.White))
public val backgroundGradient2: Brush =
    Brush.linearGradient(listOf(themeColor.copy(0.4f), themeColor.copy(0.2f)))