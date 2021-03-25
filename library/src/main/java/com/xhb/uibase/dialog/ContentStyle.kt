package com.xhb.uibase.dialog

import android.os.Parcel
import android.os.Parcelable

class ContentStyle : Parcelable {
    var text: String? = null
    var textColor: Int = 0
    var textSize: Float = 14f
    var isBold: Int = 0 //1：bold,0:normal
    var maxEms: Int = -1
    var gravity: Int = -1

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.text)
        dest.writeInt(this.textColor)
        dest.writeFloat(this.textSize)
        dest.writeInt(this.isBold)
        dest.writeInt(this.maxEms)
        dest.writeInt(this.gravity)
    }

    constructor()

    private constructor(parcel: Parcel) {
        this.text = parcel.readString()
        this.textColor = parcel.readInt()
        this.textSize = parcel.readFloat()
        this.isBold = parcel.readInt()
        this.maxEms = parcel.readInt()
        this.gravity = parcel.readInt()
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ContentStyle> = object : Parcelable.Creator<ContentStyle> {
            override fun createFromParcel(source: Parcel): ContentStyle {
                return ContentStyle(source)
            }

            override fun newArray(size: Int): Array<ContentStyle?> {
                return arrayOfNulls(size)
            }
        }
    }

    class Builder private constructor() {
        private var text: String? = null
        private var textColor: Int = 0
        private var textSize: Float = 18f
        private var isBold: Int = 0 //1：bold,0:normal
        private var maxEms: Int = 0
        private var gravity: Int = -1

        companion object {

            fun instance(): Builder {
                return Builder()
            }
        }

        fun withText(text: String): Builder {
            this.text = text
            return this
        }

        fun withTextColor(textColor: Int): Builder {
            this.textColor = textColor
            return this
        }

        fun withTextSize(textSize: Float): Builder {
            this.textSize = textSize
            return this
        }

        fun withBold(isBold: Boolean): Builder {
            this.isBold = if (isBold) 1 else 0
            return this
        }

        fun withMaxEms(maxEms: Int): Builder {
            this.maxEms = maxEms
            return this
        }

        fun withGravity(gravity: Int): Builder {
            this.gravity = gravity
            return this
        }

        fun build(): ContentStyle {
            val contentStyle = ContentStyle()
            contentStyle.text = this.text
            contentStyle.textColor = this.textColor
            contentStyle.textSize = this.textSize
            contentStyle.isBold = this.isBold
            contentStyle.maxEms = this.maxEms
            contentStyle.gravity = this.gravity
            return contentStyle
        }
    }
}
