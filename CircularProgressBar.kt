package com.example.customwidgets

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.compose.animation.core.animateDecay
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat


class CircularProgressBar @JvmOverloads constructor(context: Context, attrs : AttributeSet? = null) : View(context,attrs
) {

    private var strokeWith = 40f
    private val rect = RectF()

    private var barStartColor = android.graphics.Color.GREEN // Start color for gradient
    private var barEndColor = android.graphics.Color.MAGENTA  // End color for gradient
    private var progress = 50

    private val paintBackground = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE

        strokeWith = this@CircularProgressBar.strokeWith
    }
    private val paintProgress = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        strokeWith = this@CircularProgressBar.strokeWith
        strokeCap = Paint.Cap.ROUND
    }

    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context,android.R.color.black)
        textSize = 60f
        textAlign = Paint.Align.CENTER
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width/2f
        val centerY = height/2f
        val radius = (width.coerceAtMost((height/2f).toInt())-strokeWith)

        rect.set(centerX-radius,centerY-radius,centerX+radius,centerY+radius)
        canvas.drawCircle(centerX,centerY,radius,paintBackground)

        val sweepAngle = 360*(progress/100f)
        canvas.drawArc(rect,-90f,sweepAngle,false,paintProgress)

        canvas.drawText("${progress.toInt()}%",centerX,centerY+20f,paintText)
    }
    fun setProgressWithAnimation(targetProcess: Int){
        val animator = ValueAnimator.ofInt(progress,targetProcess.toInt())
        animator.duration = 500
        animator.addUpdateListener { animation->
            progress = animation.animatedValue as Int
            invalidate()
        }
        animator.start()
    }

    fun setGradientColors(startColor: Int, endColor: Int) {
        barStartColor = startColor
        barEndColor = endColor
        invalidate()
    }

    // ✅ Handle user touch interactions
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val newProgress = ((event.x / width) * 100).toInt()
            setProgressWithAnimation(newProgress)
            return true
        }
        return super.onTouchEvent(event)
    }
}