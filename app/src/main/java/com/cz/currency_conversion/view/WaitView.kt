package com.cz.currency_conversion.view

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.cz.currency_conversion.R

class WaitView : View {
    companion object {
        private const val ANIMATION_FRAME = 40
        private const val DOT_COUNT = 5
    }

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mArgbEvaluator = ArgbEvaluator()
    private var mFrame = 0

    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val radius = width / 18.0f
        val cy = height / 2.0f
        mPaint.color = mArgbEvaluator.evaluate(Math.abs(mFrame - ANIMATION_FRAME).toFloat() / ANIMATION_FRAME, resources.getColor(R.color.purple_200), resources.getColor(R.color.teal_200)) as Int
        for (i in 0 until DOT_COUNT) {
            val cx = radius + i * radius * 4
            canvas?.drawCircle(cx, cy, getFrameRadius(i, radius), mPaint)
        }
        mFrame++
        if (mFrame == ANIMATION_FRAME * 2) {
            mFrame = 0
        }
        invalidate()
    }

    private fun getFrameRadius(index: Int, radius: Float): Float {
        var frame = mFrame + ANIMATION_FRAME * index / (DOT_COUNT - 1)
        if (frame > ANIMATION_FRAME * 2) {
            frame -= ANIMATION_FRAME * 2
        }
        return radius * Math.abs(frame - ANIMATION_FRAME) / ANIMATION_FRAME
    }
}