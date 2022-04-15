package id.mufiid.formgenerator.formgenerator.views

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import id.mufiid.formgenerator.formgenerator.R
import id.mufiid.formgenerator.formgenerator.modules.GeneralBuilder

@SuppressLint("ViewConstructor", "InflateParams")
class CheckBoxController(builder: Builder) :
    LinearLayout(builder.context, null, builder.defStyleAttr) {

    var title: String? = null
    var activity: Activity? = null
    var ctx: Context? = null
    var view: View? = null
    var nullable = false
    var checkBoxItem: java.util.ArrayList<String>? = null
    var checkBoxItemSelected = HashMap<String, CheckBox>()
    var txtTitle: TextView? = null
    var formLayout: LinearLayout? = null

    class Builder(activity: Activity) : GeneralBuilder<Builder>, Cloneable {
        var context: Context? = null
        var activity: Activity? = null
        var formLayout: LinearLayout? = null

        //optional
        var title: String? = null
        var backgroundDrawable: Drawable? = null
        var titleFont: String? = null
        var nullable = false
        var orientation = VERTICAL
        var titleColorResource = -1
        var checkBoxItem = ArrayList<String>()
        var defStyleAttr: Int = R.style.Theme_FormGenerator
        var onCheckedListener: OnCheckedListener? = null
        var formViewResource = -1

        init {
            this.activity = activity
            this.context = activity.baseContext
        }

        override fun setActivity(activity: Activity): Builder {
            this.activity = activity
            this.context = activity.baseContext
            return this
        }

        override fun setTitle(title: String?): Builder {
            this.title = title
            return this
        }

        override fun setTitleFont(title: String?): Builder {
            this.titleFont = title
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

        fun setCheckBoxItem(checkBoxItem: ArrayList<String>): Builder {
            this.checkBoxItem = checkBoxItem
            return this
        }

        fun setOnCheckedListener(onCheckedListener: OnCheckedListener?): Builder {
            this.onCheckedListener = onCheckedListener
            return this
        }


        fun setFormViewResource(resource: Int): Builder {
            formViewResource = resource
            return this
        }

        fun create(): CheckBoxController {
            return CheckBoxController(this)
        }

        @Throws(CloneNotSupportedException::class)
        override fun clone(): Any {
            return super.clone()
        }
    }

    init {
        this.ctx = builder.context
        this.activity = builder.activity
        this.nullable = builder.nullable
        this.title = builder.title
        this.checkBoxItem = builder.checkBoxItem

        this.view = LayoutInflater.from(this.ctx).inflate(R.layout.form_base_layout, null)
        val baseLayout = this.view?.findViewById<LinearLayout>(R.id.baseLayout)

        baseLayout?.orientation = builder.orientation

        txtTitle = this.view?.findViewById(R.id.item_title)
        txtTitle?.text = builder.title

        if (checkBoxItem != null) {
            for (title in checkBoxItem!!) {
                val itemView = if (builder.formViewResource != -1) {
                    LayoutInflater.from(this.ctx).inflate(builder.formViewResource, null)
                } else {
                    LayoutInflater.from(this.ctx).inflate(R.layout.form_checkbox, null)
                }

                val textViewQuestion = itemView?.findViewById<TextView>(R.id.item_checkbox_title)
                textViewQuestion?.text = title

                val checkBoxAnswer = itemView?.findViewById<CheckBox>(R.id.item_checkbox_value)
                if (checkBoxAnswer != null) {
                    checkBoxItemSelected[title] = checkBoxAnswer
                }

                checkBoxAnswer?.setOnCheckedChangeListener { _, selected ->
                    if (selected) builder.onCheckedListener?.onChecked(title)
                }

                baseLayout?.addView(itemView)
            }

            if (builder.formLayout != null) {
                this.formLayout = builder.formLayout
                this.formLayout?.addView(this.view)
            }
        }
    }

    fun setValue(key: String, value: Boolean) {
        if (checkBoxItemSelected[key] != null) {
            checkBoxItemSelected[key]?.isChecked = value
        }
    }

    @JvmName("getView1")
    fun getView() = this.view

    fun isChecked(key: String): Boolean? {
        if (checkBoxItemSelected[key] != null) {
            return checkBoxItemSelected[key]?.isChecked
        }
        return false
    }

    fun getAllChecked(): ArrayList<String> {
        val checkedList = ArrayList<String>()
        for (key in checkBoxItemSelected.keys) {
            if (checkBoxItemSelected[key]?.isChecked == true) {
                checkedList.add(key)
            }
        }
        return checkedList
    }

    fun isAllChecked(): Boolean {
        return if (this.view?.visibility == View.GONE) {
            false
        } else {
            var allChecked = true
            for (key in checkBoxItemSelected.keys) {
                if (checkBoxItemSelected[key]?.isChecked == false) {
                    allChecked = false
                }
                break
            }
            allChecked
        }
    }

    @JvmName("getTitle1")
    fun getTitle(): String? = this.title

    @JvmName("setTitle1")
    fun setTitle(title: String) {
        this.title = title
    }

    fun getTextTitle() = this.txtTitle

    fun attachView() {
        this.formLayout?.addView(view)
    }

    fun detachView() {
        this.formLayout?.removeView(view)
    }

    interface CheckListener {
        fun isChecked(): Boolean
    }

    interface OnCheckedListener {
        fun onChecked(checkboxName: String?)
    }
}