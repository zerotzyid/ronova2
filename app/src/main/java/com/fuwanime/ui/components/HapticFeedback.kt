package com.fuwanime.ui.components

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.LocalDensity

/**
 * Haptic feedback helper for micro-interactions.
 * Provides tactile feedback on button presses, long presses, and other interactions.
 *
 * Usage:
 *   val haptic = rememberHapticFeedback()
 *   Button(onClick = { haptic.performClick(); ... })
 */
@Composable
fun rememberHapticFeedback(): HapticFeedback {
    val context = LocalContext.current
    return remember(context) { HapticFeedbackImpl(context) }
}

/** Haptic feedback implementation using Android Vibrator API. */
class HapticFeedbackImpl(private val context: Context) {
    private val vibrator: Vibrator? = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator

    /** Light haptic feedback for button clicks. */
    fun performClick() {
        vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
    }

    /** Medium haptic feedback for long presses. */
    fun performLongPress() {
        vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK))
    }

    /** Success haptic feedback for successful actions. */
    fun performSuccess() {
        vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_SUCCESS))
    }

    /** Error haptic feedback for failed actions. */
    fun performError() {
        vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_FAILURE))
    }

    /** Custom haptic feedback with amplitude (0.0 to 1.0). */
    fun performCustom(amplitude: Float, durationMs: Long) {
        val effect = VibrationEffect.createOneShot(durationMs, (amplitude * 255).toInt())
        vibrate(effect)
    }

    private fun vibrate(effect: VibrationEffect?) {
        if (vibrator?.hasVibrator() == true) {
            vibrator?.vibrate(effect)
        }
    }
}

/** CompositionLocal for haptic feedback - allows easy access from deep in the composition tree. */
val LocalHapticFeedback = androidx.compose.runtime.staticCompositionLocalOf<HapticFeedback> {
    throw IllegalStateException("LocalHapticFeedback not provided. Use HapticFeedbackProvider.")
}

/** Provides [LocalHapticFeedback] to the composition tree. */
@Composable
fun HapticFeedbackProvider(hapticFeedback: HapticFeedback, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalHapticFeedback provides hapticFeedback) {
        content()
    }
}

/** Convenience extension to access [LocalHapticFeedback]. */
@Composable
fun LocalHapticFeedback(): HapticFeedback = LocalHapticFeedback.current

/** Extension on View for haptic feedback without Compose. */
fun android.view.View.hapticClick() {
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    vibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
}
