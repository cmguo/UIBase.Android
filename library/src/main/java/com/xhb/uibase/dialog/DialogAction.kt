package com.xhb.uibase.dialog

import android.os.Parcel
import android.os.Parcelable

open class DialogAction : Parcelable {
    var actionText: String? = null
    var contentStyle: ContentStyle? = null
    var componentKey: String? = null

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.actionText)
        dest.writeParcelable(this.contentStyle, flags)
        dest.writeString(this.componentKey)
    }

    protected constructor(`in`: Parcel) {
        this.actionText = `in`.readString()
        this.contentStyle = `in`.readParcelable(ContentStyle::class.java.classLoader)
        this.componentKey = `in`.readString()
    }

    constructor(actionText: String?, contentStyle: ContentStyle?, componentKey: String?) {
        this.actionText = actionText
        this.contentStyle = contentStyle
        this.componentKey = componentKey
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<DialogAction> = object : Parcelable.Creator<DialogAction> {
            override fun createFromParcel(source: Parcel): DialogAction {
                return DialogAction(source)
            }

            override fun newArray(size: Int): Array<DialogAction?> {
                return arrayOfNulls(size)
            }
        }
    }
}
