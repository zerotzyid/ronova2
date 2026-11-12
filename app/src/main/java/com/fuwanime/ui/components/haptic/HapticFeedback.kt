package com.fuwanime.ui.components.haptic

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
typealias FuwaHapticFeedback = FuwaHapticFeedbackImpl

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

/** Extension on View for haptic feedback without Compose. */
fun android.view.View.hapticClick() {
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    vibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
}
