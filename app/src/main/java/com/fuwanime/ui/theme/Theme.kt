package com.fuwanime.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.fuwanime.R
import com.fuwanime.data.local.AppSettings
import com.fuwanime.data.local.ThemeMode

// DropSauce (Kotatsu) design tokens, ported ke Compose untuk fuwastream.
// Light: biru primary #0059C8, surface #FBF8FD, background #FEFBFF.
// Dark: primary #ABC7FF, surface #121316, background #1A1B1F.
private val LightKotatsu = lightColorScheme(
    primary = Color(0xFF0059C8),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD9E2FF),
    onPrimaryContainer = Color(0xFF001944),
    inversePrimary = Color(0xFFAFC6FF),
    secondary = Color(0xFF575E71),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFDBE2F9),
    onSecondaryContainer = Color(0xFF141B2C),
    tertiary = Color(0xFF725573),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFDD7FB),
    onTertiaryContainer = Color(0xFF2A132C),
    background = Color(0xFFFEFBFF),
    onBackground = Color(0xFF1B1B1F),
    surface = Color(0xFFFBF8FD),
    onSurface = Color(0xFF1B1B1F),
    surfaceVariant = Color(0xFFE1E2EC),
    onSurfaceVariant = Color(0xFF44464F),
    surfaceTint = Color(0xFF0059C8),
    inverseSurface = Color(0xFF303034),
    inverseOnSurface = Color(0xFFF2F0F4),
    outline = Color(0xFF757780),
    outlineVariant = Color(0xFFC5C6D0),
    error = Color(0xFFBA1B1B),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD4),
    onErrorContainer = Color(0xFF410001),
    surfaceBright = Color(0xFFFBF8FD),
    surfaceDim = Color(0xFFDBD9DD),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF5F3F7),
    surfaceContainer = Color(0xFFEFEDF1),
    surfaceContainerHigh = Color(0xFFE9E7EC),
    surfaceContainerHighest = Color(0xFFE3E2E6),
)

private val DarkKotatsu = darkColorScheme(
    primary = Color(0xFFABC7FF),
    onPrimary = Color(0xFF002F65),
    primaryContainer = Color(0xFF00458F),
    onPrimaryContainer = Color(0xFFD7E3FF),
    inversePrimary = Color(0xFF205DAF),
    secondary = Color(0xFFBEC6DC),
    onSecondary = Color(0xFF283041),
    secondaryContainer = Color(0xFF3E4759),
    onSecondaryContainer = Color(0xFFDAE2F9),
    tertiary = Color(0xFFDDBCE0),
    onTertiary = Color(0xFF3F2844),
    tertiaryContainer = Color(0xFF573E5C),
    onTertiaryContainer = Color(0xFF000000),
    background = Color(0xFF1A1B1F),
    onBackground = Color(0xFFC7C6CA),
    surface = Color(0xFF121316),
    onSurface = Color(0xFFC7C6CA),
    surfaceVariant = Color(0xFF44474E),
    onSurfaceVariant = Color(0xFFC4C6D0),
    surfaceTint = Color(0xFFABC7FF),
    inverseSurface = Color(0xFFE3E2E6),
    inverseOnSurface = Color(0xFF1A1B1F),
    outline = Color(0xFF8E9099),
    outlineVariant = Color(0xFF44464F),
    error = Color(0xFFBA1B1B),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD4),
    onErrorContainer = Color(0xFF410001),
    surfaceBright = Color(0xFF38393C),
    surfaceDim = Color(0xFF121316),
    surfaceContainerLowest = Color(0xFF0D0E11),
    surfaceContainerLow = Color(0xFF1A1B1F),
    surfaceContainer = Color(0xFF1F1F23),
    surfaceContainerHigh = Color(0xFF292A2D),
    surfaceContainerHighest = Color(0xFF343538),
)

private fun ColorScheme.toAmoled() = copy(
    background = Color.Black,
    surface = Color.Black,
    surfaceDim = Color.Black,
    surfaceContainerLowest = Color.Black,
    surfaceContainerLow = Color(0xFF121316),
    surfaceContainer = Color(0xFF1F1F23),
    surfaceContainerHigh = Color(0xFF292A2D),
    surfaceContainerHighest = Color(0xFF343538),
)

val GflexBody = FontFamily(
    Font(R.font.gflex_body, FontWeight.Normal),
    Font(R.font.gflex_body, FontWeight.Bold),
)

val GflexMedium = FontFamily(
    Font(R.font.gflex_medium, FontWeight.Normal),
    Font(R.font.gflex_medium, FontWeight.Bold),
)

val GflexHeading = FontFamily(
    Font(R.font.gflex_heading, FontWeight.Normal),
    Font(R.font.gflex_heading, FontWeight.Bold),
)

val GflexBold = FontFamily(
    Font(R.font.gflex_bold, FontWeight.Normal),
    Font(R.font.gflex_bold, FontWeight.Bold),
)

private val DropSauceTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = GflexBold, fontSize = 48.sp, lineHeight = 56.sp, fontWeight = FontWeight.W800, letterSpacing = 0.em,
    ),
    displayMedium = TextStyle(
        fontFamily = GflexBold, fontSize = 36.sp, lineHeight = 44.sp, fontWeight = FontWeight.W800, letterSpacing = 0.em,
    ),
    displaySmall = TextStyle(
        fontFamily = GflexBody, fontSize = 30.sp, lineHeight = 38.sp, fontWeight = FontWeight.W500, letterSpacing = 0.em,
    ),
    headlineLarge = TextStyle(
        fontFamily = GflexHeading, fontSize = 32.sp, lineHeight = 40.sp, fontWeight = FontWeight.W700, letterSpacing = 0.em,
    ),
    headlineMedium = TextStyle(
        fontFamily = GflexHeading, fontSize = 28.sp, lineHeight = 36.sp, fontWeight = FontWeight.W700, letterSpacing = 0.em,
    ),
    headlineSmall = TextStyle(
        fontFamily = GflexHeading, fontSize = 24.sp, lineHeight = 32.sp, fontWeight = FontWeight.W700, letterSpacing = 0.em,
    ),
    titleLarge = TextStyle(
        fontFamily = GflexBody, fontSize = 22.sp, lineHeight = 28.sp, fontWeight = FontWeight.W500, letterSpacing = 0.em,
    ),
    titleMedium = TextStyle(
        fontFamily = GflexMedium, fontSize = 18.sp, lineHeight = 24.sp, fontWeight = FontWeight.W600, letterSpacing = 0.00833.em,
    ),
    titleSmall = TextStyle(
        fontFamily = GflexMedium, fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.W600, letterSpacing = 0.00714.em,
    ),
    bodyLarge = TextStyle(
        fontFamily = GflexBody, fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.W500, letterSpacing = 0.03125.em,
    ),
    bodyMedium = TextStyle(
        fontFamily = GflexBody, fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.W500, letterSpacing = 0.01786.em,
    ),
    bodySmall = TextStyle(
        fontFamily = GflexBody, fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.W500, letterSpacing = 0.03333.em,
    ),
    labelLarge = TextStyle(
        fontFamily = GflexMedium, fontSize = 16.sp, lineHeight = 20.sp, fontWeight = FontWeight.W600, letterSpacing = 0.00625.em,
    ),
    labelMedium = TextStyle(
        fontFamily = GflexMedium, fontSize = 14.sp, lineHeight = 16.sp, fontWeight = FontWeight.W600, letterSpacing = 0.03571.em,
    ),
    labelSmall = TextStyle(
        fontFamily = GflexMedium, fontSize = 11.sp, lineHeight = 16.sp, fontWeight = FontWeight.W600, letterSpacing = 0.04545.em,
    ),
)

private val DropSauceShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(24.dp),
)

@Composable
fun FuwaTheme(settings: AppSettings = AppSettings(), content: @Composable () -> Unit) {
    val dark = when (settings.themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
    }
    val context = LocalContext.current
    val base = when {
        settings.dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
            if (dark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        dark -> DarkKotatsu
        else -> LightKotatsu
    }
    val scheme = if (dark && settings.amoledBlack) base.toAmoled() else base

    MaterialTheme(colorScheme = scheme, typography = DropSauceTypography, shapes = DropSauceShapes, content = content)
}
