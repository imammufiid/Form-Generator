package id.mufiid.formgenerator.formgenerator.views

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import id.mufiid.formgenerator.formgenerator.function.ViewChecker

class EditTextMultipleController(builder: Builder) {
    var context: Context? = null
    var activity: Activity? = null
    var formLayout: LinearLayout? = null
    var editTextList: HashMap<String, EditTextController>? = HashMap()
    var view: View? = null

    class Builder(activity: Activity, editTextList: HashMap<String, EditTextController>) {
        var context: Context? = null
        var activity: Activity? = null
        var formLayout: LinearLayout? = null
        var editTextList: HashMap<String, EditTextController>? = null
        var margin: Int? = null

        init {
            this.context = activity.baseContext
            this.activity = activity
            this.editTextList = editTextList
        }

        fun setActivity(activity: Activity): Builder {
            this.activity = activity
            context = activity.baseContext
            return this
        }

        fun setEdtList(editTextList: HashMap<String, EditTextController>): Builder {
            this.editTextList = editTextList
            return this
        }

        fun setFormLayout(formLayout: LinearLayout?): Builder {
            this.formLayout = formLayout
            return this
        }

        fun setMargin(margin: Int): Builder {
            this.margin = margin
            return this
        }

        fun create(): EditTextMultipleController {
            return EditTextMultipleController(this)
        }
    }

    init {
        this.context = builder.context
        this.activity = builder.activity
        this.editTextList = builder.editTextList

        val linearLayout = LinearLayout(builder.context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            orientation = LinearLayout.HORIZONTAL
        }

        val margin = builder.margin

        val iterator = editTextList?.entries?.iterator()
        var isFirst = true

        while (iterator?.hasNext() == true) {
            val (key, _) = iterator.next()
            val view = editTextList?.get(key)?.view
            val layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            ).apply {
                weight = 1F
            }

            /**
             * if it's not a first item, add left margin
             */
            if (!isFirst) {
                if (margin != null) layoutParams.leftMargin = margin / 2
            }

            /**
             * if it's not a last item, add right margin
             */
            if (iterator.hasNext()) {
                if (margin != null) layoutParams.rightMargin = margin / 2
            }

            view?.layoutParams = layoutParams
            linearLayout.addView(view)
            isFirst = false
        }

        this.view = linearLayout

        if (builder.formLayout != null) {
            this.formLayout = builder.formLayout
            this.formLayout?.addView(this.view)
        }
    }

    fun getValue(key: String): String? {
        if (editTextList?.get(key) == null) {
            return null
        }

        return editTextList?.get(key)?.value
    }

    @JvmName("getView1")
    fun getView() = this.view

    @JvmName("getEditTextList1")
    fun getEditTextList() = this.editTextList

    /**
     * Check all edittext is empty or not
     */
    fun checkMustFilled(): Boolean {
        if (this.context != null) {
            return ViewChecker.isFilled(this, this.context!!)
        }
        return false
    }

    fun attachView() {
        this.formLayout?.addView(view)
    }

    fun detachView() {
        this.formLayout?.removeView(view)
    }

}