package com.gerwalex.mymonma.ui

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val md_theme_light_primary = Color(0xFF0056D0)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer = Color(0xFFDAE2FF)
val md_theme_light_onPrimaryContainer = Color(0xFF001847)
val md_theme_light_secondary = Color(0xFF585E71)
val md_theme_light_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer = Color(0xFFDCE2F9)
val md_theme_light_onSecondaryContainer = Color(0xFF151B2C)
val md_theme_light_tertiary = Color(0xFF006497)
val md_theme_light_onTertiary = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer = Color(0xFFCCE5FF)
val md_theme_light_onTertiaryContainer = Color(0xFF001E31)
val md_theme_light_error = Color(0xFFBA1A1A)
val md_theme_light_errorContainer = Color(0xFFFFDAD6)
val md_theme_light_onError = Color(0xFFFFFFFF)
val md_theme_light_onErrorContainer = Color(0xFF410002)
val md_theme_light_background = Color(0xFFFEFBFF)
val md_theme_light_onBackground = Color(0xFF1B1B1F)
val md_theme_light_surface = Color(0xFFFEFBFF)
val md_theme_light_onSurface = Color(0xFF1B1B1F)
val md_theme_light_surfaceVariant = Color(0xFFE1E2EC)
val md_theme_light_onSurfaceVariant = Color(0xFF44464F)
val md_theme_light_outline = Color(0xFF757780)
val md_theme_light_inverseOnSurface = Color(0xFFF2F0F4)
val md_theme_light_inverseSurface = Color(0xFF303034)
val md_theme_light_inversePrimary = Color(0xFFB1C5FF)
val md_theme_light_shadow = Color(0xFF000000)
val md_theme_light_surfaceTint = Color(0xFF0056D0)
val md_theme_light_outlineVariant = Color(0xFFC5C6D0)
val md_theme_light_scrim = Color(0xFF000000)

val md_theme_dark_primary = Color(0xFFB1C5FF)
val md_theme_dark_onPrimary = Color(0xFF002C71)
val md_theme_dark_primaryContainer = Color(0xFF00409F)
val md_theme_dark_onPrimaryContainer = Color(0xFFDAE2FF)
val md_theme_dark_secondary = Color(0xFFC0C6DD)
val md_theme_dark_onSecondary = Color(0xFF2A3042)
val md_theme_dark_secondaryContainer = Color(0xFF404659)
val md_theme_dark_onSecondaryContainer = Color(0xFFDCE2F9)
val md_theme_dark_tertiary = Color(0xFF92CCFF)
val md_theme_dark_onTertiary = Color(0xFF003351)
val md_theme_dark_tertiaryContainer = Color(0xFF004B73)
val md_theme_dark_onTertiaryContainer = Color(0xFFCCE5FF)
val md_theme_dark_error = Color(0xFFFFB4AB)
val md_theme_dark_errorContainer = Color(0xFF93000A)
val md_theme_dark_onError = Color(0xFF690005)
val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)
val md_theme_dark_background = Color(0xFF1B1B1F)
val md_theme_dark_onBackground = Color(0xFFE4E2E6)
val md_theme_dark_surface = Color(0xFF1B1B1F)
val md_theme_dark_onSurface = Color(0xFFE4E2E6)
val md_theme_dark_surfaceVariant = Color(0xFF44464F)
val md_theme_dark_onSurfaceVariant = Color(0xFFC5C6D0)
val md_theme_dark_outline = Color(0xFF8F9099)
val md_theme_dark_inverseOnSurface = Color(0xFF1B1B1F)
val md_theme_dark_inverseSurface = Color(0xFFE4E2E6)
val md_theme_dark_inversePrimary = Color(0xFF0056D0)
val md_theme_dark_shadow = Color(0xFF000000)
val md_theme_dark_surfaceTint = Color(0xFFB1C5FF)
val md_theme_dark_outlineVariant = Color(0xFF44464F)
val md_theme_dark_scrim = Color(0xFF000000)

val seed = Color(0xFF126EFF)
val Reports = Color(0xFF9FD7CD)
val Wertpapier = Color(0xFFD6A0FF)
val Import = Color(0xFFFFF1B7)
val light_Reports = Color(0xFF006A60)
val light_onReports = Color(0xFFFFFFFF)
val light_ReportsContainer = Color(0xFF74F8E5)
val light_onReportsContainer = Color(0xFF00201C)
val light_Wertpapier = Color(0xFF7948A0)
val light_onWertpapier = Color(0xFFFFFFFF)
val light_WertpapierContainer = Color(0xFFF2DAFF)
val light_onWertpapierContainer = Color(0xFF2E004E)
val light_Import = Color(0xFF6D5E00)
val light_onImport = Color(0xFFFFFFFF)
val light_ImportContainer = Color(0xFFFBE365)
val light_onImportContainer = Color(0xFF211B00)
val dark_Reports = Color(0xFF53DBC9)
val dark_onReports = Color(0xFF003731)
val dark_ReportsContainer = Color(0xFF005048)
val dark_onReportsContainer = Color(0xFF74F8E5)
val dark_Wertpapier = Color(0xFFE1B6FF)
val dark_onWertpapier = Color(0xFF47146E)
val dark_WertpapierContainer = Color(0xFF602F86)
val dark_onWertpapierContainer = Color(0xFFF2DAFF)
val dark_Import = Color(0xFFDEC74C)
val dark_onImport = Color(0xFF393000)
val dark_ImportContainer = Color(0xFF524700)
val dark_onImportContainer = Color(0xFFFBE365)


// see https://stackoverflow.com/a/75557863/792632
@Immutable
data class CustomColorsPalette(
    val Reports: Color = Color.Unspecified,
    val onReports: Color = Color.Unspecified,
    val ReportsContainer: Color = Color.Unspecified,
    val onReportsContainer: Color = Color.Unspecified,
    val Wertpapier: Color = Color.Unspecified,
    val onWertpapier: Color = Color.Unspecified,
    val WertpapierContainer: Color = Color.Unspecified,
    val onWertpapierContainer: Color = Color.Unspecified,
    val Import: Color = Color.Unspecified,
    val onImport: Color = Color.Unspecified,
    val ImportContainer: Color = Color.Unspecified,
    val onImportContainer: Color = Color.Unspecified
)

val LocalAppColors = staticCompositionLocalOf { CustomColorsPalette() }

