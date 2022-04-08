package id.mufiid.formgenerator.formgenerator.views

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import id.mufiid.formgenerator.formgenerator.R
import kotlin.jvm.Throws

class ButtonController(builder: Builder) {
    private var button: Button? = null
    private var view: View? = null

    class Builder(activity: Activity) : Cloneable {
        var ctx: Context? = null
        var text: String = ""
        var textColor: Int = -1
        var id: Int = 0
        var drawableResourceId: Int = -1
        var formLayout: ViewGroup? = null
        var layoutParams: ViewGroup.LayoutParams? = null
        var onClickListener: View.OnClickListener? = null
        var isAllCaps: Boolean = true

        init {
            this.ctx = activity.baseContext
        }

        fun setText(text: String): Builder {
            this.text = text
            return this
        }

        fun setId(id: Int): Builder {
            this.id = id
            return this
        }

        fun setTextColor(textColor: Int): Builder {
            this.textColor = textColor
            return this
        }

        fun setTextAllCaps(isAllCaps: Boolean): Builder {
            this.isAllCaps = isAllCaps
            return this
        }

        fun setLayoutParams(layoutParams: ViewGroup.LayoutParams): Builder {
            this.layoutParams = layoutParams
            return this
        }

        fun setOnClickListener(onClickListener: View.OnClickListener): Builder {
            this.onClickListener = onClickListener
            return this
        }

        fun setFormLayout(formLayout: ViewGroup): Builder {
            this.formLayout = formLayout
            return this
        }

        fun create(): ButtonController = ButtonController(this)

        @Throws(CloneNotSupportedException::class)
        override fun clone(): Any {
            return super.clone()
        }

    }

    init {
        /**
         * init button widget
         */
        val button = Button(builder.ctx)

        /**
         * set layout params
         */
        if (builder.layoutParams != null) {
            button.layoutParams = builder.layoutParams
        } else {
            button.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        /**
         * set text color
         */
        if (builder.textColor != -1) {
            button.setTextColor(builder.textColor)
        } else {
            button.setTextColor(Color.WHITE)
        }

        /**
         * set text button
         */
        button.text = builder.text

        /**
         * set id button
         */
        button.id = builder.id

        /**
         * set text all caps
         */
        button.isAllCaps = builder.isAllCaps

        /**
         * set drawable button
         */
        if (builder.drawableResourceId != -1) {
            button.setBackgroundResource(builder.drawableResourceId)
        } else {
            button.setBackgroundResource(R.drawable.default_button_controller)
        }

        /**
         * set form layout
         */
        if (builder.formLayout != null) {
            builder.formLayout?.addView(button)
        }

        /**
         * set on click listener
         */
        if (builder.onClickListener != null) {
            button.setOnClickListener(builder.onClickListener)
        }
        this.button = button
    }

    fun getView(): View? {
        this.view = this.button
        return this.button
    }
}