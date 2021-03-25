package com.jelly.thor.dragfloatingactionbutton

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.ImageView
import kotlin.math.sqrt

/**
 * 类描述：可以拖拽的FloatingActionButton <br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2020/12/16 18:24 <br/>
 */
class DragFloatingActionButton : ImageView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var isDrag = false
    private var lastX = 0F
    private var lastY = 0F

    private var mParent: ViewGroup? = null
    private var mParentWidth = 0
    private var mParentHeight = 0

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        val rawX = ev.rawX
        val rawY = ev.rawY
        //event.getAction() & MotionEvent.ACTION_MASK
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                isDrag = false
                parent.requestDisallowInterceptTouchEvent(true)
                lastX = rawX
                lastY = rawY

                if (parent != null) {
                    mParent = parent as ViewGroup
                    mParentWidth = mParent!!.width
                    mParentHeight = mParent!!.height
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (mParentWidth < 0.2 || mParentHeight < 0.2) {
                    return super.onTouchEvent(ev)
                }
                isDrag = true
                val dx = rawX - lastX
                val dy = rawY - lastY
                //Log.e("123===", "lastX -------------$lastX")
                //Log.e("123===", "rawX -------------$rawX")
                //Log.e("123===", "x -------------$x")
                //Log.e("123===", "dx +++++++++++++$dx")
                val distance = sqrt((dx * dx + dy * dy).toDouble())
                val isMove = distance >= 3.0
                if (!isMove) {
                    return super.onTouchEvent(ev)
                }
                var x = x + dx
                var y = y + dy
                //检测是否到达边缘 左上右下
                x = when {
                    x < 0 -> {
                        //Log.e("123===", "x<0 -------------$x")
                        0F
                    }
                    x > mParentWidth - width -> {
                        //Log.e(
                        //    "123===",
                        //    "x > mParentWidth - width-------------${x}--${mParentWidth}--${width}==${mParentWidth - width}}} )"
                        //)
                        (mParentWidth - width).toFloat()
                    }
                    else -> {
                        x
                    }
                }
                y = when {
                    y < 0 -> {
                        0F
                    }
                    y > mParentHeight - height -> {
                        (mParentHeight - height).toFloat()
                    }
                    else -> {
                        y
                    }
                }

                setX(x)
                setY(y)

                lastX = rawX
                lastY = rawY
            }
            MotionEvent.ACTION_UP -> {
                if (isDrag) {
                    isDrag = false
                } else {
                    performClick()
                }
            }
        }

        return !isDrag || super.onTouchEvent(ev)
    }
}