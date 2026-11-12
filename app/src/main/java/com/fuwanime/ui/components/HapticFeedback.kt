package com.fuwanime.ui.components

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext

/**
 * Haptic feedback helper for micro-interactions.
 * Provides tactile feedback on button clicks, long presses, and other interactions.
 *
 * Usage:
 *   val haptic = rememberFuwaHapticFeedback()
 *   Button(onClick = { haptic.performClick(); ... })
 */
@Composable
fun rememberFuwaHapticFeedback(): FuwaHapticFeedback {
    val context = LocalContext.current
    return remember(context) { FuwaHapticFeedbackImpl(context) }
}

/** Haptic feedback implementation using Android Vibrator API. */
class FuwaHapticFeedbackImpl(private val context: Context) {
    private val vibrator: Vibrator? = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator

    /** Light haptic feedback for button clicks. */
    fun performClick() {
        vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
    }

    /** Medium haptic feedback for long presses. */
    fun performLongPress() {
        vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK))
    }

    /** Success haptic feedback for successful actions (API 29+). */
    fun performSuccess() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_SUCCESS))
        } else {
            vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
        }
    }

    /** Error haptic feedback for failed actions (API 29+). */
    fun performError() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_FAILURE))
        } else {
            vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
        }
    }

    /** Custom haptic feedback with amplitude (0.0 to 1.0) and duration. */
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
val LocalFuwaHapticFeedback = staticCompositionLocalOf<FuwaHapticFeedback> {
    throw IllegalStateException("LocalFuwaHapticFeedback not provided. Use HapticFeedbackProvider.")
}

/** Provides [LocalFuwaHapticFeedback] to the composition tree. */
@Composable
fun HapticFeedbackProvider(hapticFeedback: FuwaHapticFeedback, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalFuwaHapticFeedback provides hapticFeedback) {
        content()
    }
}

/** Convenience extension to access [LocalFuwaHapticFeedback]. */
@Composable
fun LocalFuwaHapticFeedback(): FuwaHapticFeedback = LocalFuwaHapticFeedback.current

/** Extension on View for haptic feedback without Compose. */
fun android.view.View.hapticClick() {
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    vibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
}
