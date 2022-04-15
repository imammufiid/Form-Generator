package id.mufiid.formgenerator.formgenerator.views

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.mufiid.formgenerator.formgenerator.R
import id.mufiid.formgenerator.formgenerator.adapter.CheckboxAdapter
import id.mufiid.formgenerator.formgenerator.model.CheckboxData
import id.mufiid.formgenerator.formgenerator.modules.GeneralBuilder

@SuppressLint("ViewConstructor", "InflateParams")
class MultipleCheckboxController(builder: Builder) :
    LinearLayout(builder.context, null, builder.defStyleAttr) {
    enum class SelectedBy {
        ID, VALUE
    }

    var ctx: Context? = null
    var activity: Activity? = null
    var formLayout: LinearLayout? = null
    var txtTitle: TextView? = null
    var editTextContent: EditText? = null
    var view: View? = null
    var nullable = false
    var title: String? = null
    var listItems: ArrayList<CheckboxData>? = null
    var dialogCheckbox: AlertDialog? = null
    var listSelected: ArrayList<CheckboxData> = ArrayList()
    var listViewCheckbox: RecyclerView? = null
    var selectedValue: StringBuilder? = null
    var selectedId: StringBuilder? = null
    var adapter: CheckboxAdapter? = null

    class Builder(activity: Activity) : GeneralBuilder<Builder>, Cloneable {
        var activity: Activity? = null
        var context: Context? = null
        var formLayout: LinearLayout? = null
        var title: String? = null
        var minLines = 0
        var dateformat: String? = null
        var backgroundDrawable: Drawable? = null
        var titleFont: String? = null
        var nullable = false
        var orientation = VERTICAL
        var inputType = -1
        var titleColorResource = -1
        var defStyleAttr: Int = R.style.Theme_FormGenerator
        var formViewResource = -1
        var listItems: ArrayList<CheckboxData>? = null

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

        fun setItems(listItems: ArrayList<CheckboxData>): Builder {
            this.listItems = listItems
            return this
        }

        fun setDateFormat(dateFormat: String): Builder {
            this.dateformat = dateFormat
            return this
        }

        fun setMinLines(minLines: Int): Builder {
            this.minLines = minLines
            return this
        }

        fun setInputType(inputType: Int): Builder {
            this.inputType = inputType
            return this
        }

        fun setFormViewResource(resource: Int): Builder {
            this.formViewResource = resource
            return this
        }

        fun setBackgroundDrawable(backgroundDrawable: Drawable?): Builder {
            this.backgroundDrawable = backgroundDrawable
            return this
        }

        fun create() = MultipleCheckboxController(this)

        override fun clone(): Builder {
            return super.clone() as Builder
        }
    }

    init {
        this.ctx = builder.context
        this.activity = builder.activity
        this.nullable = builder.nullable
        this.title = builder.title
        this.listItems = builder.listItems

        /**
         * Change view resource
         */
        this.view = if (builder.formViewResource != -1) {
            LayoutInflater.from(builder.context).inflate(builder.formViewResource, null)
        } else {
            LayoutInflater.from(builder.context).inflate(R.layout.form_edittext, null)
        }

        /**
         * Instance view layout
         */
        this.txtTitle = this.view?.findViewById(R.id.item_edittext_title)
        this.editTextContent = this.view?.findViewById(R.id.item_edittext_value)

        /**
         * setup value property view layout
         */
        this.editTextContent?.minLines = builder.minLines
        this.editTextContent?.tag = builder.title

        /**
         * setup optional value
         */
        if (builder.title != null) {
            this.txtTitle?.text = builder.title
        }

        /**
         * setup background field
         */
        if (builder.backgroundDrawable != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.editTextContent?.background = builder.backgroundDrawable
        }

        /**
         * setup title font
         */
        if (builder.titleFont != null) {
            val typeFace = Typeface.createFromAsset(this.ctx?.assets, builder.titleFont)
            this.txtTitle?.typeface = typeFace
        }

        /**
         * setup title color from resource
         */
        if (builder.titleColorResource != -1) {
            this.txtTitle?.setTextColor(ContextCompat.getColor(context, builder.titleColorResource))
        }

        /**
         * setup form layout parent
         */
        if (builder.formLayout != null) {
            this.formLayout = builder.formLayout
            this.formLayout?.addView(this.view)
        }

        createDialog()
        this.editTextContent?.setOnClickListener { dialogCheckbox?.show() }
        this.editTextContent?.keyListener = null
    }

    override fun setBackground(background: Drawable?) {
        super.setBackground(background)
        this.editTextContent?.background = background
    }

    /**
     * setup value edit text content
     */
    fun setValue(text: String?) {
        if (text != null) {
            this.editTextContent?.setText(text)
        }
    }

    /**
     * setup handle item selected
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setSelected(listItems: ArrayList<String>?, selectedBy: SelectedBy) {
        if (listItems != null && this.adapter != null && this.listItems != null) {
            var counter = 0
            this.selectedId = StringBuilder()
            this.selectedValue = StringBuilder()

            for (data in listItems) {
                for (checkBoxData in this.listItems!!) {
                    if ((selectedBy == SelectedBy.ID && checkBoxData.id?.equals(data) == true) ||
                        (selectedBy == SelectedBy.VALUE && checkBoxData.value?.equals(data) == true)
                    ) {
                        checkBoxData.checked = true
                        this.selectedId?.append(if (counter > 0) ", " else "")
                        this.selectedValue?.append(if (counter > 0) ", " else "")

                        counter++
                        break
                    }
                }
            }
            this.editTextContent?.setText(this.selectedValue.toString())
            this.adapter?.notifyDataSetChanged()
        }
    }

    /**
     * setup update list item checkbox
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updateItemsList(items: ArrayList<CheckboxData>) {
        this.listItems?.apply {
            clear()
            addAll(items)
        }

        /**
         * update adapter
         */
        if (this.adapter != null) this.adapter?.notifyDataSetChanged()
    }

    /**
     * setup get text title
     */
    fun getTextTitle() = this.txtTitle

    /**
     * setup get edit text content
     */
    @JvmName("getEditTextContent1")
    fun getEditTextContent() = this.editTextContent

    /**
     * setup get view
     */
    @JvmName("getView1")
    fun getView() = this.view

    /**
     * setup get value
     */
    fun getValue(): String? {
        return if (this.view?.visibility == View.GONE) {
            null
        } else {
            this.editTextContent?.text.toString()
        }
    }

    /**
     * setup set nullable
     */
    fun isNullable(): Boolean {
        return nullable
    }

    /**
     * setup get nullable
     */
    @JvmName("setNullable1")
    fun setNullable(nullable: Boolean) {
        this.nullable = nullable
    }

    fun isFilled(): Boolean {
        if (!nullable) {
            if (this.editTextContent?.text.toString().isEmpty()) return false
        }
        return true
    }

    @JvmName("getTitle1")
    fun getTitle() = this.title

    fun attachView() {
        this.formLayout?.addView(view)
    }

    fun detachView() {
        this.formLayout?.removeView(view)
    }

    fun getSelectedValue(): String? {
        return try {
            selectedValue.toString()
        } catch (e: NullPointerException) {
            null
        }
    }

    fun getSelectedId(): String? {
        return try {
            selectedId.toString()
        } catch (e: NullPointerException) {
            null
        }
    }

    fun getSelectedCheckbox(): ArrayList<CheckboxData> {
        return listSelected
    }

    private fun createDialog() {
        if (this.dialogCheckbox == null) {
            val dialogBuilder = AlertDialog.Builder(this.activity)
            val dialogView =
                this.activity?.layoutInflater?.inflate(R.layout.dialog_multiple_checkbox, null)
            dialogBuilder.setView(dialogView)
            val textTitle = dialogView?.findViewById<TextView>(R.id.txtFormTitle)
            this.listViewCheckbox = dialogView?.findViewById(R.id.listCheckbox)

            if (this.ctx != null && this.listItems != null) {
                this.adapter = CheckboxAdapter(this.ctx!!, listItems!!)
            }

            this.listViewCheckbox?.adapter = this.adapter
            this.listViewCheckbox?.layoutManager = LinearLayoutManager(this.activity?.baseContext)

            textTitle?.text = this.title
            dialogBuilder.setPositiveButton("Save") { dialog, _ ->
                this.listSelected.clear()
                this.selectedValue = StringBuilder()
                this.selectedId = StringBuilder()

                var count = 0
                for (checkboxData in this.adapter?.getCheckboxListChecked()!!) {
                    if (checkboxData.checked == true) {
                        this.selectedId?.append(if (count > 0) ", " else "")?.append(checkboxData.id)
                        this.selectedValue?.append(if (count > 0) ", " else "")?.append(checkboxData.value)
                        count++
                    }
                }

                this.listSelected.addAll(this.adapter?.getCheckboxListChecked()!!)
                this.editTextContent?.setText(this.selectedValue.toString())
                dialog.dismiss()
            }

            dialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

            this.dialogCheckbox = dialogBuilder.create()
        }
    }
}