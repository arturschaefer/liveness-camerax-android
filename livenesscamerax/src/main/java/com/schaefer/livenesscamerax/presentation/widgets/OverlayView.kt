package com.schaefer.livenesscamerax.presentation.widgets

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.Nullable
import com.schaefer.livenesscamerax.R

private const val WIDTH_FACTOR = 2.9f
private const val HEIGHT_FACTOR = 12f
private const val ALPHA_VALUE = 200
private const val HORIZONTAL_MARGIN = 150f
private const val VERTICAL_MARGIN = 400f
private const val SWEEP_ANGLE = 360F
private const val STROKE_WIDTH = 15F

class OverlayView : View {
    private var transparentBackground: Paint? = null
    private var eraser: Paint? = null
    private var borderPaint: Paint? = null
    private var horizontalMargin = 0f
    private var verticalMargin = 0f


    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, @Nullable attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (horizontalMargin == 0f) {
            horizontalMargin = measuredWidth / WIDTH_FACTOR
        }
        if (verticalMargin == 0f) {
            verticalMargin = measuredHeight / HEIGHT_FACTOR
        }
        val rect = createRect()
        transparentBackground?.let { canvas.drawRect(rect, it) }
        borderPaint?.let {
            canvas.drawArc(
                createRectF(), 0F, SWEEP_ANGLE, true, it
            )
        }
        eraser?.let {
            canvas.drawArc(
                createRectF(), 0F, SWEEP_ANGLE, true, it
            )
        }
    }

    private fun createRect() = Rect(0, 0, measuredWidth, measuredHeight)

    private fun createRectF() = RectF(
        horizontalMargin,
        verticalMargin,
        measuredWidth - horizontalMargin,
        measuredHeight - verticalMargin
    )

    fun init(
        @ColorRes borderColor: Int = R.color.black,
        horizontalMargin: Float = HORIZONTAL_MARGIN,
        verticalMargin: Float = VERTICAL_MARGIN
    ) {
        this.horizontalMargin = horizontalMargin
        this.verticalMargin = verticalMargin

        transparentBackground = Paint().apply {
            color = Color.BLACK
            alpha = ALPHA_VALUE
            style = Paint.Style.FILL
        }

        borderPaint = Paint().apply {
            color = resources.getColor(borderColor)
            strokeWidth = STROKE_WIDTH
            style = Paint.Style.STROKE
        }

        eraser = Paint().apply {
            isAntiAlias = true
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            style = Paint.Style.FILL
        }
    }
}