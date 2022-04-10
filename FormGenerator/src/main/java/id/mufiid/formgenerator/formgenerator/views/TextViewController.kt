package id.mufiid.formgenerator.formgenerator.views

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import id.mufiid.formgenerator.formgenerator.R
import id.mufiid.formgenerator.formgenerator.modules.GeneralBuilder

@SuppressLint("InflateParams", "ViewConstructor")
class TextViewController(builder: Builder) :
    LinearLayout(builder.context, null, builder.defStyleAttr) {

    private var title: String = ""
    private var ctx: Context? = null
    private var formLayout: LinearLayout? = null
    private var activity: Activity? = null
    private var textViewTitle: TextView? = null
    private var textViewContent: TextView? = null
    private var view: View? = null

    class Builder(activity: Activity) : GeneralBuilder<Builder>, Cloneable {
        var activity: Activity? = null
        var context: Context? = null
        var formLayout: LinearLayout? = null

        var title: String = ""
        var titleFont: String? = ""
        var content: String = ""
        var contentFont: String = ""
        var nullable: Boolean = false
        var orientation = VERTICAL
        var titleColorResource: Int = -1
        var defStyleAttr = R.style.Theme_FormGenerator
        var formViewResource: Int = -1

        init {
            this.activity = activity
            this.context = activity.baseContext
        }

        override fun setActivity(activity: Activity): Builder {
            this.activity = activity
            return this
        }

        override fun setTitle(title: String?): Builder {
            if (title != null) {
                this.title = title
            }
            return this
        }

        override fun setTitleFont(title: String?): Builder {
            if (title != null) {
                this.titleFont = title
            }
            return this
        }

        override fun setTitleColorResource(titleColorResource: Int): Builder {
            this.titleColorResource = titleColorResource
            return this
        }

        override fun setOrientation(orientation: Int): Builder {
            this.orientation = orientation
            return this
        }

        override fun setNullable(nullable: Boolean): Builder {
            this.nullable = nullable
            return this
        }

        override fun setDefStyleAttr(defStyleAttr: Int): Builder {
            this.defStyleAttr = defStyleAttr
            return this
        }

        override fun setFormLayout(formLayout: LinearLayout?): Builder {
            this.formLayout = formLayout
            return this
        }

        fun setContent(content: String): Builder {
            this.content = content
            return this
        }

        fun setFormViewResource(resource: Int): Builder {
            this.formViewResource = resource
            return this
        }

        fun setContentFont(contentFont: String): Builder {
            this.contentFont = contentFont
            return this
        }

        fun create(): TextViewController = TextViewController(this)

        override fun clone(): Builder {
            return super.clone() as (Builder)
        }
    }

    init {
        this.ctx = builder.context
        this.activity = builder.activity

        this.view = LayoutInflater.from(context).inflate(R.layout.form_textview, null)

        this.title = builder.title

        textViewTitle = view?.findViewById(R.id.item_textview_title)
        textViewContent = view?.findViewById(R.id.item_textview_value)

        textViewTitle?.text = builder.title
        textViewContent?.text = builder.content

        if (builder.titleFont != null) {
            val typeFace = Typeface.createFromAsset(context.assets, builder.titleFont)
            textViewContent?.typeface = typeFace
        }

        if (builder.titleColorResource != -1) {
            textViewTitle?.setTextColor(ContextCompat.getColor(context, builder.titleColorResource))
        }

        if (builder.formLayout != null) {
            formLayout = builder.formLayout
            formLayout?.addView(view)
        }

        fun setContent(text: String?) {
            textViewContent!!.text = text
        }

        fun getView(): View? {
            return view
        }

        fun getContent(): String? {
            return if (view?.visibility == GONE) null else textViewContent!!.text.toString()
        }

        fun getTitle(): String {
            return title
        }

        fun setTitle(title: String?) {
            this.title = title!!
        }

        fun getTextViewTitle(): TextView? {
            return textViewTitle
        }

        fun getTextViewContent(): TextView? {
            return textViewContent
        }

        fun attachView() {
            formLayout!!.addView(view)
        }

        fun detachView() {
            formLayout!!.removeView(view)
        }

    }
}