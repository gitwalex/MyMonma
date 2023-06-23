package com.gerwalex.mymonma.ext

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer

/**
 * Scaling Animation onPress
 * see https://medium.com/@alohaabhi/beautiful-way-to-access-touch-interactions-in-jetpack-compose-c4b8444b5c95
 */
fun Modifier.scaleOnPress(
    interactionSource: InteractionSource
) = composed {
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        if (isPressed) {
            0.95f
        } else {
            1f
        }
    )
    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
}