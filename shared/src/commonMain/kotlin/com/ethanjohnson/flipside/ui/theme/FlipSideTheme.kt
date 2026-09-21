package com.ethanjohnson.flipside.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val FlipSideLightColors = lightColorScheme(

    // Main brand color
    primary = Charcoal,
    onPrimary = WarmWhite,

    primaryContainer = AgedPaper,
    onPrimaryContainer = Charcoal,

    // Secondary brand accent
    secondary = MutedOlive,
    onSecondary = WarmWhite,

    secondaryContainer = ColorTokens.OliveContainer,
    onSecondaryContainer = DarkOlive,

    // Third accent
    tertiary = DustyRed,
    onTertiary = WarmWhite,

    tertiaryContainer = ColorTokens.RedContainer,
    onTertiaryContainer = DarkDustyRed,

    // Page
    background = PaperCream,
    onBackground = Charcoal,

    // Cards / sheets
    surface = WarmWhite,
    onSurface = Charcoal,

    surfaceVariant = AgedPaper,
    onSurfaceVariant = WarmBrown,

    // Borders
    outline = WarmGray,
    outlineVariant = DividerBrown,

    // Errors
    error = ErrorBrick,
    onError = WarmWhite,

    errorContainer = ColorTokens.ErrorContainer,
    onErrorContainer = DarkDustyRed
)

private val FlipSideDarkColors = darkColorScheme(

    primary = DarkTextPrimary,
    onPrimary = Charcoal,

    primaryContainer = SoftCharcoal,
    onPrimaryContainer = DarkTextPrimary,

    secondary = MutedOlive,
    onSecondary = DarkBackground,

    secondaryContainer = DarkOlive,
    onSecondaryContainer = DarkTextPrimary,

    tertiary = DustyRed,
    onTertiary = DarkBackground,

    tertiaryContainer = DarkDustyRed,
    onTertiaryContainer = DarkTextPrimary,

    background = DarkBackground,
    onBackground = DarkTextPrimary,

    surface = DarkSurface,
    onSurface = DarkTextPrimary,

    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,

    outline = WarmGray,
    outlineVariant = DarkBorder,

    error = ErrorBrick,
    onError = DarkTextPrimary
)

/**
 * Colors that don't need to be exposed as full Material color roles.
 */
private object ColorTokens {
    val OliveContainer = androidx.compose.ui.graphics.Color(0xFFE1E5D6)
    val RedContainer = androidx.compose.ui.graphics.Color(0xFFF0DAD7)
    val ErrorContainer = androidx.compose.ui.graphics.Color(0xFFF2D9D6)
}

@Composable
fun FlipSideTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) {
            FlipSideDarkColors
        } else {
            FlipSideLightColors
        },
        typography = FlipSideTypography,
        shapes = FlipSideShapes,
        content = content
    )
}