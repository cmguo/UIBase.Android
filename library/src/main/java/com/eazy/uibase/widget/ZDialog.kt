package com.eazy.uibase.widget

import android.content.Context
import android.content.res.Configuration
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eazy.uibase.R
import com.eazy.uibase.dialog.MaskDialog
import com.eazy.uibase.resources.Drawables
import com.eazy.uibase.resources.RoundDrawable

class ZDialog @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.dialogStyle
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    @DrawableRes
    var image: Int = 0
        set(value) {
            field = value
            syncImage()
        }

    var title: CharSequence?
        get() = _textView.text
        set(value) {
            _textView.text = value
            if (value == null || value.isEmpty()) {
                _textView.visibility = View.GONE
            } else {
                _textView.visibility = View.VISIBLE
            }
        }

    var subTitle: CharSequence?
        get() = _textView2.text
        set(value) {
            _textView2.text = value
            if (value == null || value.isEmpty()) {
                _textView2.visibility = View.GONE
            } else {
                _textView2.visibility = View.VISIBLE
            }
        }

    @RawRes
    var content: Int = 0
        set(value) {
            if (field == value)
                return
            field = value
            syncContent()
        }

    @RawRes
    var confirmButton: Int = 0
        set(value) {
            if (field == value)
                return
            field = value
            syncButton(_confirmButton, value)
        }

    @RawRes
    var cancelButton: Int = 0
        set(value) {
            if (field == value)
                return
            field = value
            syncButton(_cancelButton, value)
        }

    var moreButtons: Iterable<Any?>? = null
        set(value) {
            field = value
            if (value == null || value.count() == 0) {
                _listView.visibility = View.GONE
            } else {
                _listView.visibility = View.VISIBLE
            }
            _adapter.notifyDataSetChanged()
        }

    @StringRes
    var checkBox: Int = 0
        set(value) {
            if (field == value)
                return
            field = value
            if (value == 0) {
                _checkBox.visibility = View.GONE
            } else {
                _checkBox.text = context.resources.getText(value)
                _checkBox.visibility = View.VISIBLE
            }
        }

    @ColorRes
    var closeIconColor = 0
        set(value) {
            field = value
            if (value == 0) {
                _closeIcon.visibility = View.GONE
            } else {
                _closeIcon.imageTintList = ContextCompat.getColorStateList(context, value)
                _closeIcon.visibility = View.VISIBLE
            }
        }

    val body get() = _content

    val checkedState get() = _checkBox.checkedState

    interface DialogListener {
        fun buttonClicked(dialog: ZDialog, btnId: Int)
        fun dialogDismissed(dialog: ZDialog) {}
    }

    var listener: DialogListener? = null

    private val _imageView: ZAvatarView
    private val _closeIcon: ImageView
    private val _textView: TextView
    private val _textView2: TextView
    private var _content: View? = null
    private val _confirmButton: ZButton
    private val _cancelButton: ZButton
    private val _listView: RecyclerView
    private val _checkBox: ZCheckBox

    private val _adapter = ButtonAdapter(this)

    private var _inited = false

    init {
        LayoutInflater.from(context).inflate(R.layout.dialog, this)
        _imageView = findViewById(R.id.imageView)
        _closeIcon = findViewById(R.id.closeIcon)
        _textView = findViewById(R.id.textView)
        _textView2 = findViewById(R.id.textView2)
        _confirmButton = findViewById(R.id.confirmButton)
        _cancelButton = findViewById(R.id.cancelButton)
        _listView = findViewById(R.id.listView)
        _checkBox = findViewById(R.id.checkBox)

        _closeIcon.setOnClickListener { dismiss() }

        _listView.adapter = _adapter
        _listView.layoutManager = LinearLayoutManager(context)

        val a = context.obtainStyledAttributes(attrs, R.styleable.ZDialog, defStyleAttr, 0)
        readStyle(a)
        a.recycle()

        _inited = true

        syncBackground()
        val radius = (background as RoundDrawable).cornerRadius
        _imageView.cornerRadii = floatArrayOf(radius, radius, radius, radius, 0f, 0f, 0f, 0f)
    }

    fun popUp(fragmentManager: FragmentManager, buttonClicked: ((dialog: ZDialog, btnId: Int) -> Unit)? = null) {
        val lp = FrameLayout.LayoutParams(0, 0)
        lp.width = minimumWidth
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        layoutParams = lp
        if (buttonClicked != null) {
            listener = object : DialogListener {
                override fun buttonClicked(dialog: ZDialog, btnId: Int) {
                    buttonClicked(dialog, btnId)
                    dialog.dismiss()
                }
            }
        }
        MaskDialog(this).show(fragmentManager, "")
    }

    fun dismiss() {
        MaskDialog.dismiss(this)
    }

    override fun addView(child: View) {
        addView(child, child.layoutParams)
    }

    override fun addView(child: View, params: ViewGroup.LayoutParams?) {
        if (!_inited)
            return super.addView(child, params)
        if (_content != null) {
            throw RuntimeException("Already has a body!")
        }
        val lp = params as? LayoutParams ?: LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        lp.gravity = Gravity.CENTER_HORIZONTAL
        lp.weight=1f
        val parent = _confirmButton.parent as View
        val parent2 = parent.parent as ViewGroup
        parent2.addView(child, parent2.indexOfChild(parent), lp)
        _content = child
    }

    override fun removeView(view: View) {
        if (view == _content) {
            val parent = view.parent as ViewGroup
            parent.removeView(view)
            _content = null
            return
        }
        super.removeView(view)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        listener?.dialogDismissed(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        syncBackground()
    }

    companion object {
        private const val TAG = "ZDialog"
    }

    /* private */

    private fun readStyle(a: TypedArray) {
        content = a.getResourceId(R.styleable.ZDialog_content, 0)
        title = a.getText(R.styleable.ZDialog_title)
        subTitle = a.getText(R.styleable.ZDialog_subTitle)
        image = a.getResourceId(R.styleable.ZDialog_image, 0)
        confirmButton = a.getResourceId(R.styleable.ZDialog_confirmButton, 0)
        cancelButton = a.getResourceId(R.styleable.ZDialog_cancelButton, 0)
        val buttonIds = a.getResourceId(R.styleable.ZDialog_moreButtons, 0)

        if (buttonIds > 0) {
            val aa = resources.obtainTypedArray(buttonIds)
            val v = TypedValue()
            val ids = Array<Any?>(aa.length()) { null }
            for (i in 0 until aa.length()) {
                if (aa.getValue(i, v)) {
                    if (v.resourceId != 0)
                        ids[i] = v.resourceId
                    else if (v.string.isNotEmpty())
                        ids[i] = v.string
                }
            }
            aa.recycle()
            moreButtons = listOf(ids)
        }
    }

    private fun syncBackground() {
        background = RoundDrawable(context, R.style.RoundDrawable_Dialog)
    }

    private fun syncImage() {
        if (image == 0) {
            _imageView.setImageDrawable(null)
            _imageView.visibility = View.GONE
        } else {
            _imageView.setImageDrawable(Drawables.getDrawable(context, image))
            _imageView.visibility = View.VISIBLE
        }
    }

    private fun syncButton(button: ZButton, content: Int) {
        if (content == 0) {
            button.visibility = GONE
        } else {
            button.content = content
            button.visibility = VISIBLE
            button.setOnClickListener {
                listener?.buttonClicked(this, it.id)
            }
        }
    }

    private fun syncContent() {
        if (_content != null) {
            removeView(_content!!)
            _content = null
        }
        if (content == 0)
            return
        val type = resources.getResourceTypeName(content)
        if (type == "layout") {
            val view = LayoutInflater.from(context).inflate(content, this, false)
            addView(view)
        } else if (type == "style") {
            val typedArray = context.obtainStyledAttributes(content, R.styleable.ZDialog)
            readStyle(typedArray)
            typedArray.recycle()
        }
    }

    /* private */

    private class ButtonHolder(view: View)
        : RecyclerView.ViewHolder(view) {

        private val button = view.findViewById<ZButton>(R.id.button)!!

        fun bind(content: Any?) {
            if (content is String) {
                button.text = content
            } else if (content is Int) {
                button.content = content
            }
        }
    }

    private class ButtonAdapter(private val outer: ZDialog) : RecyclerView.Adapter<ButtonHolder>(), OnClickListener {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.action_item, parent, false)
            view.setOnClickListener(this)
            return ButtonHolder(view)
        }

        override fun getItemCount(): Int {
            return outer.moreButtons?.count() ?: 0
        }

        override fun onBindViewHolder(holder: ButtonHolder, position: Int) {
            holder.bind(outer.moreButtons?.elementAt(position))
        }

        override fun onClick(view: View) {
            outer.listener?.buttonClicked(outer, outer._listView.getChildAdapterPosition(view))
        }
    }

}


