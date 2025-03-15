package com.example.customwidgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.compose.animation.core.animateDecay
import androidx.core.content.ContextCompat

class CustomSwitch @JvmOverloads constructor(context: Context, attrs : AttributeSet) : View(context,attrs){
    private var isOn = false
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rect = RectF()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.color = if(isOn) ContextCompat.getColor(context, android.R.color.holo_purple)
        else ContextCompat.getColor(context,android.R.color.darker_gray)

        rect.set(0f,0f,width.toFloat(),height.toFloat())
        canvas.drawRoundRect(rect,height/2f,height/2f,paint)

        paint.color = ContextCompat.getColor(context,android.R.color.white)
        val circleX = if(isOn) width-height/2f else height/2f
        canvas.drawCircle(circleX,height/2f,height/2.5f,paint)

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            isOn = !isOn
            invalidate()
        }
        return  true
    }
    fun isSwitchOn() : Boolean = isOn
}