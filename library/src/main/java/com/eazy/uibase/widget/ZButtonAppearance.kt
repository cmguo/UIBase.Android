package com.eazy.uibase.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import androidx.annotation.StyleRes
import com.eazy.uibase.R

data class ZButtonAppearance(var textColorId : Int = 0, var backgroundColorId : Int = 0,
    var textColor: ColorStateList? = null, var iconColor: ColorStateList? = null, var backgroundColor: ColorStateList? = null,
    var iconPosition: ZButton.IconPosition = ZButton.IconPosition.Left,
    var minHeight: Int = 0, var cornerRadius: Float = 0f, var paddingX: Int = 0, var paddingY: Int = 0,
    var textSize: Float = 0f, var lineHeight: Float = 0f, var iconSize: Int = 0, var iconPadding: Int = 0) {

    private var _dftTypeSizeApplied = false
    private var _inited = false
    private var _sizeOverride = 0


    @StyleRes
    private var _styleId = 0
    private var _buttonType = ZButton.ButtonType.Primitive
    private var _buttonSize = ZButton.ButtonSize.Large

    fun buttonType() : ZButton.ButtonType {
        return _buttonType
    }

    fun buttonSize() : ZButton.ButtonSize {
        return _buttonSize
    }

    fun buttonApperanceId() : Int {
        return _styleId
    }

    fun setButtonType(context: Context, type: ZButton.ButtonType) : Int {
        if (_buttonType == type || _styleId != 0)
            return 0
        _buttonType = type
        return readAppearance(context, type.resId, R.attr.buttonType)
    }

    fun setButtonSize(context: Context, size: ZButton.ButtonSize) : Int {
        if (_buttonSize == size || _styleId != 0)
            return 0
        _buttonSize = size
        return readAppearance(context, size.resId, R.attr.buttonType)
    }

    fun setButtonApperanceId(context: Context, id : Int) : Int {
        if (_styleId == id)
            return 0
        _styleId = id
        return readAppearance(context, _styleId, R.attr.buttonAppearance)
    }

    fun readAppearance(context: Context, @StyleRes id: Int, from: Int = 0) : Int {
        val aa = context.obtainStyledAttributes(id, R.styleable.ZButton_Appearance)
        if (from == R.attr.buttonAppearance) {
            _sizeOverride = 0
        }
        var sizeOverride = _sizeOverride
        if (from == R.attr.buttonType) {
            _sizeOverride = 0
        }
        var changed = readAppearance(context, aa)
        if (from == R.attr.buttonType) {
            val newSizeOverride = _sizeOverride and SizeChanges
            sizeOverride = sizeOverride and newSizeOverride.inv()
            if (sizeOverride != 0) {
                _sizeOverride = sizeOverride.inv()
                changed = changed or readAppearance(context, _buttonSize.resId)
            }
            _sizeOverride = newSizeOverride
        } else {
            _sizeOverride = sizeOverride
        }
        aa.recycle()
        return changed
    }

    fun readAppearance(context: Context, a: TypedArray, styleArray: Boolean = false) : Int {
        var changed = 0
        val n: Int = a.indexCount
        var type = if (_dftTypeSizeApplied) -1 else _buttonType.ordinal
        var size = if (_dftTypeSizeApplied) -1 else _buttonSize.ordinal
        _dftTypeSizeApplied = true
        for (i in 0 until n) {
            val attr: Int = a.getIndex(i)
            var index = attr
            if (styleArray) {
                index = sAppearanceValues[attr] ?: -1
                if (index == -1) {
                    continue
                }
            }
            when (index) {
                R.styleable.ZButton_Appearance_buttonType -> type = a.getInt(attr, type)
                R.styleable.ZButton_Appearance_buttonSize -> size = a.getInt(attr, size)
            }
        }
        // Always apply ButtonType & ButtonSize, because it's parts of ButtonAppearance
        if (size >= 0) {
            val bs = ZButton.ButtonSize.values()[size]
            if (styleArray && !_inited)
                _buttonSize = bs
            changed = changed or readAppearance(context, bs.resId)
        }
        if (type >= 0) {
            val bt = ZButton.ButtonType.values()[type]
            if (styleArray && !_inited)
                _buttonType = bt
            changed = changed or readAppearance(context, bt.resId)
        }
        if (styleArray)
            _inited = true
        for (i in 0 until n) {
            val attr: Int = a.getIndex(i)
            var index = attr
            if (styleArray) {
                index = sAppearanceValues[attr] ?: -1
                if (index == -1) {
                    continue
                }
            }
            val c = 1 shl index
            if ((_sizeOverride and c) != 0)
                continue
            _sizeOverride = _sizeOverride or c
            when (index) {
                R.styleable.ZButton_Appearance_android_textColor -> {
                    textColorId = a.getResourceId(attr, 0)
                    textColor = a.getColorStateList(attr)
                }
                R.styleable.ZButton_Appearance_backgroundColor -> {
                    backgroundColorId = a.getResourceId(attr, 0)
                    backgroundColor = a.getColorStateList(attr)
                }
                R.styleable.ZButton_Appearance_iconPosition -> {
                    val p = a.getInt(attr, iconPosition.ordinal)
                    if (p != iconPosition.ordinal) {
                        iconPosition = ZButton.IconPosition.values()[p]
                    } else {
                        index = -1 // not changed
                    }
                }
                R.styleable.ZButton_Appearance_android_minHeight -> {
                    val h = a.getDimensionPixelSize(attr, minHeight)
                    if (h == minHeight) {
                        index = -1
                    } else {
                        minHeight = h
                    }
                }
                R.styleable.ZButton_Appearance_cornerRadius -> {
                    val d = a.getDimension(attr, cornerRadius)
                    if (d == cornerRadius) {
                        index = -1
                    } else {
                        cornerRadius = d
                    }
                }
                R.styleable.ZButton_Appearance_paddingX -> {
                    val d = a.getDimensionPixelSize(attr, paddingX)
                    if (d == paddingX) {
                        index = -1
                    } else {
                        paddingX = d
                    }
                }
                R.styleable.ZButton_Appearance_paddingY -> {
                    val d = a.getDimensionPixelSize(attr, paddingX)
                    if (d == paddingY) {
                        index = -1
                    } else {
                        paddingY = d
                    }
                }
                R.styleable.ZButton_Appearance_android_textSize -> {
                    val d = a.getDimension(attr, textSize)
                    if (d == textSize) {
                        index = -1
                    } else {
                        textSize = d
                    }
                }
                R.styleable.ZButton_Appearance_lineHeight -> {
                    val d = a.getDimension(attr, lineHeight)
                    if (d == lineHeight) {
                        index = -1
                    } else {
                        lineHeight = d
                    }
                }
                R.styleable.ZButton_Appearance_iconSize -> {
                    val d = a.getDimensionPixelSize(attr, iconSize)
                    if (d == iconSize) {
                        index = -1
                    } else {
                        iconSize = d
                    }
                }
                R.styleable.ZButton_Appearance_iconPadding -> {
                    val d = a.getDimensionPixelSize(attr, iconPadding)
                    if (d == iconPadding) {
                        index = -1
                    } else {
                        iconPadding = d
                    }
                }
            } // when
            if (index != -1) {
                changed = changed or c
            }
        } // for
        return changed
    }

    companion object {

        private val sAppearanceValues = mapOf(
            R.styleable.ZButton_buttonType to R.styleable.ZButton_Appearance_buttonType,
            R.styleable.ZButton_buttonSize to R.styleable.ZButton_Appearance_buttonSize,
            R.styleable.ZButton_iconPosition to R.styleable.ZButton_Appearance_iconPosition,
            R.styleable.ZButton_backgroundColor to R.styleable.ZButton_Appearance_backgroundColor,
            R.styleable.ZButton_android_textColor to R.styleable.ZButton_Appearance_android_textColor,
            R.styleable.ZButton_android_textSize to R.styleable.ZButton_Appearance_android_textSize,
            R.styleable.ZButton_lineHeight to R.styleable.ZButton_Appearance_lineHeight,
            R.styleable.ZButton_paddingX to R.styleable.ZButton_Appearance_paddingX,
            R.styleable.ZButton_paddingY to R.styleable.ZButton_Appearance_paddingY,
            R.styleable.ZButton_android_minHeight to R.styleable.ZButton_Appearance_android_minHeight,
            R.styleable.ZButton_cornerRadius to R.styleable.ZButton_Appearance_cornerRadius,
            R.styleable.ZButton_iconSize to R.styleable.ZButton_Appearance_iconSize,
            R.styleable.ZButton_iconColor to R.styleable.ZButton_Appearance_iconColor,
            R.styleable.ZButton_iconPadding to R.styleable.ZButton_Appearance_iconPadding
        )

        private val SizeChanges = makeChange(
            R.styleable.ZButton_Appearance_lineHeight,
            R.styleable.ZButton_Appearance_paddingX,
            R.styleable.ZButton_Appearance_paddingY,
            R.styleable.ZButton_Appearance_android_minHeight,
            R.styleable.ZButton_Appearance_cornerRadius,
            R.styleable.ZButton_Appearance_iconSize,
            R.styleable.ZButton_Appearance_iconPadding)

        fun makeChange(vararg indexes : Int) : Int {
            var c = 0
            for (f in indexes) {
                c = c or (1 shl f)
            }
            return c
        }

        fun hasChanged(changed : Int, vararg indexes : Int) : Boolean {
            return (changed and makeChange(*indexes)) != 0
        }
    }
}