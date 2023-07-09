package com.gerwalex.mymonma.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

object Color {
    val appBarColor = Color(0xFF000000)
    val cashTrx = Color(0x0Fffff00)
    val importedCashTrx = Color(0x0F0fff00)
}

private val LightColors = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    errorContainer = md_theme_light_errorContainer,
    onError = md_theme_light_onError,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    outline = md_theme_light_outline,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inverseSurface = md_theme_light_inverseSurface,
    inversePrimary = md_theme_light_inversePrimary,
    surfaceTint = md_theme_light_surfaceTint,
    outlineVariant = md_theme_light_outlineVariant,
    scrim = md_theme_light_scrim,
)


private val DarkColors = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    errorContainer = md_theme_dark_errorContainer,
    onError = md_theme_dark_onError,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    surfaceTint = md_theme_dark_surfaceTint,
    outlineVariant = md_theme_dark_outlineVariant,
    scrim = md_theme_dark_scrim,
)
val OnLightCustomColorsPalette = CustomColorsPalette(
    Reports = Color(0xFF006A60),
    onReports = Color(0xFFFFFFFF),
    ReportsContainer = Color(0xFF74F8E5),
    onReportsContainer = Color(0xFF00201C),
    Wertpapier = Color(0xFF7948A0),
    onWertpapier = Color(0xFFFFFFFF),
    WertpapierContainer = Color(0xFFF2DAFF),
    onWertpapierContainer = Color(0xFF2E004E),
    Import = Color(0xFF6D5E00),
    onImport = Color(0xFFFFFFFF),
    ImportContainer = Color(0xFFFBE365),
    onImportContainer = Color(0xFF211B00)
)

val OnDarkCustomColorsPalette = CustomColorsPalette(
    Reports = Color(0xFF53DBC9),
    onReports = Color(0xFF003731),
    ReportsContainer = Color(0xFF005048),
    onReportsContainer = Color(0xFF74F8E5),
    Wertpapier = Color(0xFFE1B6FF),
    onWertpapier = Color(0xFF47146E),
    WertpapierContainer = Color(0xFF602F86),
    onWertpapierContainer = Color(0xFFF2DAFF),
    Import = Color(0xFFDEC74C),
    onImport = Color(0xFF393000),
    ImportContainer = Color(0xFF524700),
    onImportContainer = Color(0xFFFBE365)
)

@Composable
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (!useDarkTheme) {
        LightColors
    } else {
        DarkColors
    }

    val customColorsPalette =
        if (useDarkTheme) OnDarkCustomColorsPalette
        else OnLightCustomColorsPalette

    CompositionLocalProvider(
        LocalAppColors provides customColorsPalette
    ) {
        MaterialTheme(
            colorScheme = colors,
            content = content
        )
    }
}