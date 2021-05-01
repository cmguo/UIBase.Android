package com.eazy.uibase.view

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View

/**
 * @param initialInterval The interval after first click event
 * @param normalInterval The interval after second and subsequent click
 * events
 * @param clickListener The OnClickListener, that will be called
 * periodically
 */
class RepeatListener(private val clickListener: View.OnClickListener,
    private val initialInterval: Long = 200, private val normalInterval: Long = 50) : View.OnTouchListener {

    private val handler: Handler = Handler(Looper.getMainLooper())
    private var touchedView: View? = null

    private val handlerRunnable: Runnable = object : Runnable {
        override fun run() {
            val view = touchedView ?: return
            if (view.isEnabled) {
                handler.postDelayed(this, normalInterval)
                clickListener.onClick(view)
            } else {
                // if the view was disabled by the clickListener, remove the callback
                handler.removeCallbacks(this)
                view.isPressed = false
                touchedView = null
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        if (clickListener is View.OnTouchListener) {
            clickListener.onTouch(view, motionEvent)
        }
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                handler.removeCallbacks(handlerRunnable)
                handler.postDelayed(handlerRunnable, initialInterval)
                touchedView = view
                view.isPressed = true
                clickListener.onClick(view)
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                handler.removeCallbacks(handlerRunnable)
                view.isPressed = false
                touchedView = null
                return true
            }
        }
        if (clickListener is View.OnTouchListener) {
            clickListener.onTouch(view, motionEvent)
        }
        return false
    }

    init {
        require(!(initialInterval < 0 || normalInterval < 0)) { "negative interval" }
    }
}