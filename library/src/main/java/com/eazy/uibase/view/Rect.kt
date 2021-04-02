package com.eazy.uibase.view

import android.graphics.Rect
import android.util.Size

val Rect.size get() = Size(right - left, bottom - top)
