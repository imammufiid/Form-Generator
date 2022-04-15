package id.mufiid.formgenerator.formgenerator.views

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import id.mufiid.formgenerator.formgenerator.R
import id.mufiid.formgenerator.formgenerator.modules.GeneralBuilder

@SuppressLint("ViewConstructor", "InflateParams")
class RadioButtonController(builder: Builder) :
    LinearLayout(builder.context, null, builder.defStyleAttr) {
    var activity: Activity? = null
    var ctx: Context? = null
    var txtTitle: TextView? = null
    var formLayout: LinearLayout? = null
    var mainLayout: LinearLayout? = null
    var radioGroup: RadioGroup? = null
    var value: String? = null
    var view: View? = null
    var radioButtonList: HashMap<String?, RadioButton?> = HashMap()
    var selectedListener: OnSelectedListener? = null
    var nullable = false
    var title: String? = null
    var optionListString: Array<String>? = null

    class Builder(activity: Activity) : GeneralBuilder<Builder>, Cloneable {
        var activity: Activity? = null
        var context: Context? = null
        var title: String? = null
        var titleFont: String? = null
        var titleColorResource: Int = R.color.dark_grey
        var orientation = VERTICAL
        var optionList: Array<String>? = null
        var tintColorList: IntArray? = null
        var optionColorResource: Int = R.color.dark_grey
        var defStyleAttr: Int = R.style.Theme_FormGenerator
        var nullable = true
        var selected: String? = null
        var formLayout: LinearLayout? = null
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

        fun setOptionList(optionList: Array<String>?): Builder {
            this.optionList = optionList
            return this
        }

        fun setTintColorList(tintColorList: IntArray?): Builder {
            this.tintColorList = tintColorList
            return this
        }

        fun setOptionColorResource(optionColorResource: Int): Builder {
            this.optionColorResource = optionColorResource
            return this
        }

        fun setSelected(selected: String?): Builder {
            this.selected = selected
            return this
        }


        fun setFormViewResource(resource: Int): Builder {
            formViewResource = resource
            return this
        }

        fun create() = RadioButtonController(this)

        @Throws(CloneNotSupportedException::class)
        override fun clone(): Any {
            return super.clone() as Builder
        }
    }

    init {
        this.ctx = builder.context
        this.activity = builder.activity

        this.title = builder.title

        this.optionListString = builder.optionList

        if (builder.formViewResource != -1) {
            this.view = LayoutInflater.from(builder.context).inflate(builder.formViewResource, null)
        } else {
            this.view = LayoutInflater.from(builder.context).inflate(R.layout.form_radiobutton, null)
        }

        txtTitle = this.view?.findViewById(R.id.item_textview_title)
        txtTitle?.text = this.title

        radioGroup = this.view?.findViewById(R.id.radioGroup)
        if (optionListString != null) {
            for (option in optionListString!!) {
                val radioButton = RadioButton(this.ctx)
                radioButton.text = option

                if (builder.tintColorList == null) {
                    builder.tintColorList = intArrayOf(
                        ContextCompat.getColor(context, R.color.dark_grey),
                        ContextCompat.getColor(context, R.color.dark_grey)
                    )
                }

                val colorStateList = ColorStateList(
                    arrayOf(
                        intArrayOf(-android.R.attr.state_enabled),
                        intArrayOf(android.R.attr.state_enabled)
                    ),
                    builder.tintColorList
                )

                // set the color tint list
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    radioButton.buttonTintList = colorStateList
                }

                if (this.ctx != null) {
                    radioButton.setTextColor(ContextCompat.getColor(this.ctx!!, builder.optionColorResource))
                }

                radioButton.invalidate()
                radioGroup?.addView(radioButton)
                radioButtonList[option] = radioButton
            }

            if (builder.selected != null && radioButtonList[builder.selected] != null) {
                radioButtonList[builder.selected]?.isChecked = true
                this.value = builder.selected
            }

            radioGroup?.setOnCheckedChangeListener { radioGroup, i ->
                val radioButton = radioGroup.findViewById<RadioButton>(i)
                value = radioButton.text.toString()
                if (selectedListener != null) selectedListener?.selected(value)
            }

            if (builder.formLayout != null) {
                this.formLayout = builder.formLayout
                this.formLayout?.addView(this.view)
            }
        }
    }

    fun setSelected(selected: String?) {
        if (selected != null && radioButtonList.containsKey(selected)) {
            (radioButtonList[selected] as RadioButton).isChecked = true
            this.value = selected
        }
    }

    @JvmName("getView1")
    fun getView() = this.view

    @JvmName("getValue1")
    fun getValue(): String? {
        if (this.view?.visibility == View.GONE) {
            return null
        }

        return value
    }

    @JvmName("setValue1")
    fun setValue(value: String?) {
        if (value != null && radioButtonList.containsKey(value)) {
            (radioButtonList[value] as RadioButton).isChecked = true
        }
    }

    fun setOnSelectedListener(onSelectedListener: OnSelectedListener) {
        this.selectedListener = onSelectedListener
    }

    fun isNullable(): Boolean {
        return nullable
    }

    @JvmName("setNullable1")
    fun setNullable(nullable: Boolean) {
        this.nullable = nullable
    }

    fun isFilled(): Boolean {
        if (view!!.visibility == VISIBLE && nullable) {
            if (value == null || value?.isEmpty() == true) return false
        }
        return true
    }

    @JvmName("getTxtTitle1")
    fun getTxtTitle(): TextView? {
        return txtTitle
    }

    @JvmName("getRadioGroup1")
    fun getRadioGroup(): RadioGroup? {
        return radioGroup
    }

    fun attachView() {
        this.formLayout?.addView(view)
    }

    fun detachView() {
        this.formLayout?.removeView(view)
    }

    interface OnSelectedListener {
        fun selected(value: String?)
    }
}