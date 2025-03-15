package com.example.customwidgets

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat

class CustomBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private var barStartColor = Color.BLUE // Start color for gradient
    private var barEndColor = Color.CYAN  // End color for gradient
    private var progress = 50  // Default progress (0 to 100)

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 50f
        textAlign = Paint.Align.CENTER
    }

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.LTGRAY // Background color for empty part
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val barWidth = width * (progress / 100f)
        val rect = RectF(0f, 0f, barWidth, height.toFloat())

        // Background color for unfilled part
        canvas.drawRoundRect(0f, 0f, width.toFloat(), height.toFloat(), 30f, 30f, backgroundPaint)

        // Create gradient shader
        val gradient = LinearGradient(
            0f, 0f, barWidth, height.toFloat(),
            barStartColor, barEndColor, Shader.TileMode.CLAMP
        )

        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = gradient
            style = Paint.Style.FILL
        }

        // Draw gradient progress bar
        canvas.drawRoundRect(rect, 30f, 30f, paint)

        // Draw progress text
        canvas.drawText("$progress%", width / 2f, height / 2f + 15, textPaint)
    }

    // ✅ Smooth Animation for Progress Change
    fun setProgressAnimated(targetProgress: Int) {
        val animator = ValueAnimator.ofInt(progress, targetProgress).apply {
            duration = 500 // Animation duration in milliseconds
            addUpdateListener { animation ->
                progress = animation.animatedValue as Int
                invalidate()
            }
        }
        animator.start()
    }

    // ✅ Allow external calls to set progress
    fun setProgress(value: Int) {
        progress = value.coerceIn(0, 100)
        invalidate()
    }

    // ✅ Retrieve the current progress
    fun getProgress(): Int = progress

    // ✅ Change the gradient colors
    fun setGradientColors(startColor: Int, endColor: Int) {
        barStartColor = startColor
        barEndColor = endColor
        invalidate()
    }

    // ✅ Handle user touch interactions
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val newProgress = ((event.x / width) * 100).toInt()
            setProgressAnimated(newProgress)
            return true
        }
        return super.onTouchEvent(event)
    }
}