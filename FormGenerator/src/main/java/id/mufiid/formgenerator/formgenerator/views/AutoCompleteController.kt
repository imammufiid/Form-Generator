package id.mufiid.formgenerator.formgenerator.views

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import id.mufiid.formgenerator.formgenerator.R
import id.mufiid.formgenerator.formgenerator.adapter.AutocompleteAdapter
import id.mufiid.formgenerator.formgenerator.model.AutocompleteData
import id.mufiid.formgenerator.formgenerator.model.isHidden
import id.mufiid.formgenerator.formgenerator.modules.GeneralBuilder

@SuppressLint("InflateParams", "ViewConstructor")
class AutoCompleteController(builder: Builder) :
    LinearLayout(builder.context, null, builder.defStyleAttr) {
    var title: String? = null
    var ctx: Context? = null
    var formLayout: LinearLayout? = null
    var txtTitle: TextView? = null
    var autoCompleteTextView: AppCompatAutoCompleteTextView? = null
    var view: View? = null
    var items: ArrayList<AutocompleteData>? = arrayListOf()
    var itemsDropDown: ArrayList<AutocompleteData> = ArrayList()
    private var nullable: Boolean = false
    var onSelectedListener: OnSelectedListener? = null
    var autocompleteAdapter: AutocompleteAdapter? = null
    var autocompleteDataSelected: AutocompleteData? = null
    var activity: Activity? = null
    var defaultSelectedValue: String? = null
    var defaultSelectedId = -1
    var validValue = false

    class Builder(activity: Activity) : GeneralBuilder<Builder?>, Cloneable {
        var activity: Activity? = null
        var context: Context? = null
        var items: ArrayList<AutocompleteData>? = arrayListOf()
        var title: String? = null
        var titleFont: String? = null
        var titleColorResource = -1
        var nullable = false
        var backgroundDrawable: Drawable? = null
        var orientation = VERTICAL
        var defStyleAttr: Int = R.style.Theme_FormGenerator
        var defaultSelectedValue: String? = null
        var defaultSelectedId = -1
        var formLayout: LinearLayout? = null
        var formViewResource = -1


        init {
            this.activity = activity
            context = activity.baseContext
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

        fun setBackgroundDrawable(backgroundDrawable: Drawable): Builder {
            this.backgroundDrawable = backgroundDrawable
            return this
        }

        fun setFormViewResource(resource: Int): Builder {
            this.formViewResource = resource
            return this
        }

        fun setItems(items: ArrayList<AutocompleteData>?): Builder {
            this.items = items
            return this
        }

        fun setDefaultSelectedValue(defaultSelectedValue: String): Builder {
            this.defaultSelectedValue = defaultSelectedValue
            return this
        }

        fun setDefaultSelectedId(defaultSelectedId: Int): Builder {
            this.defaultSelectedId = defaultSelectedId
            return this
        }

        fun create() = AutoCompleteController(this)

        override fun clone(): Builder {
            return super.clone() as Builder
        }
    }

    init {
        this.title = builder.title

        this.ctx = builder.context
        this.activity = builder.activity

        if (builder.formViewResource != -1) {
            this.view = LayoutInflater.from(context).inflate(builder.formViewResource, null)
        } else {
            this.view = LayoutInflater.from(context).inflate(R.layout.form_autocomplete, null)
        }

        this.txtTitle = view?.findViewById(R.id.item_atc_title)
        this.txtTitle?.text = this.title

        this.autoCompleteTextView = view?.findViewById(R.id.item_atc_list)
        if (this.items != null && builder.items != null) {
            this.items?.addAll(builder.items!!)
        }

        if (builder.items != null) {
            for (item in builder.items!!) {
                if (item.isHidden() == false) this.itemsDropDown.add(item)
            }
        }

        if (builder.formLayout != null) {
            this.formLayout = builder.formLayout
            this.formLayout?.addView(this.view)
        }

        this.defaultSelectedId = builder.defaultSelectedId
        this.defaultSelectedValue = builder.defaultSelectedValue

        setAdapter()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setAdapter() {
        autocompleteAdapter =
            this.activity?.let {
                AutocompleteAdapter(
                    it,
                    R.layout.row_autocomplete,
                    this.itemsDropDown
                )
            }
        autoCompleteTextView?.apply {
            setAdapter(autocompleteAdapter)
            threshold = 1
        }
        autoCompleteTextView?.setOnItemClickListener { adapterView, _, position, _ ->
            this.autocompleteDataSelected = adapterView?.getItemAtPosition(position) as AutocompleteData
        }

        if (this.defaultSelectedValue != null) {
            setValue(this.defaultSelectedValue)
        }

        if (this.defaultSelectedId != -1) {
            id = this.defaultSelectedId
        }

        autoCompleteTextView?.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validValue = false
                if (this.autocompleteDataSelected == null) {
                    this.autoCompleteTextView?.setText("")
                    return@setOnFocusChangeListener
                }

                if (this.autoCompleteTextView?.text.toString().isNotEmpty()) {
                    for (item in this.items!!) {
                        if (item.value == this.autoCompleteTextView?.text.toString()) {
                            autocompleteDataSelected = item
                            validValue = true
                            break
                        }
                    }
                }

                if (!validValue) {
                    autocompleteDataSelected = null
                    autoCompleteTextView?.setText("")
                }
            }
        }

        this.autoCompleteTextView?.setOnTouchListener { _, _ ->
            this.autoCompleteTextView?.showDropDown()
            return@setOnTouchListener false
        }
    }

    fun setSpinnerOnSelectedListener(onSelectedListener: OnSelectedListener) {
        this.onSelectedListener = onSelectedListener
    }

    fun indexOfItem(value: AutocompleteData) = itemsDropDown.indexOf(value)

    @JvmName("getView1")
    fun getView() = this.view

    fun setValue(value: String?) {
        if (value != null) {
            for (data in itemsDropDown) {
                if (data.value == value) {
                    this.autoCompleteTextView?.setText(value)
                    this.autocompleteDataSelected = data
                    this.validValue = true
                    break
                }
            }
        }
    }

    override fun setId(id: Int) {
        super.setId(id)
        var autoCompleteData: AutocompleteData? = null
        for (data in itemsDropDown) {
            if (data.id == id) {
                autoCompleteData = data
                this.validValue = true
                break
            }
        }
        this.autoCompleteTextView?.setText(autoCompleteData?.value)
        this.autocompleteDataSelected = autoCompleteData
    }

    fun getSelectedData(): AutocompleteData? {
        return if (shouldGetDataSelected) {
            this.autocompleteDataSelected
        } else {
            null
        }
    }

    fun getSelectedValue(): String? {
        return if (shouldGetDataSelected) {
            autocompleteDataSelected?.value
        } else {
            null
        }
    }

    fun getSelectedId(): Int? {
        return if (shouldGetDataSelected) {
            autocompleteDataSelected?.id
        } else {
            0
        }
    }

    fun setItemDropDown(itemsDropDown: ArrayList<AutocompleteData>) {
        this.itemsDropDown = itemsDropDown
        if (this.autocompleteAdapter != null) this.autocompleteAdapter?.notifyDataSetChanged()
    }

    fun getSelectedSecondaryId(): String? {
        return if (this.view?.visibility == VISIBLE) {
            this.autocompleteDataSelected?.secondaryId
        } else {
            "0"
        }
    }

    fun attachView() {
        this.formLayout?.addView(view)
    }

    fun detachView() {
        this.formLayout?.removeView(view)
    }

    @JvmName("isNullable1")
    fun isNullable(): Boolean {
        return nullable
    }

    fun setNullable(nullable: Boolean) {
        this.nullable = nullable
    }

    fun getTextTitle(): TextView? = this.txtTitle

    fun hideItemById(id: Int) {
        for (item in this.items!!) {
            if (item.id == id) {
                item.hidden = true
                break
            }
        }
        reloadListDropdown()
    }

    fun hideItemByValue(value: String?) {
        for (item in this.items!!) {
            if (item.value.equals(value)) {
                item.hidden = true
                break
            }
        }
        reloadListDropdown()
    }

    fun showItemById(id: Int) {
        for (item in this.items!!) {
            if (item.id == id) {
                item.hidden = false
                break
            }
        }
        reloadListDropdown()
    }


    fun showItemByValue(value: String?) {
        for (item in this.items!!) {
            if (item.value == value) {
                item.hidden = false
                break
            }
        }
        reloadListDropdown()
    }

    private fun reloadListDropdown() {
        this.itemsDropDown.clear()
        for (item in this.items!!) {
            if (item.hidden == false) this.itemsDropDown.add(item)
        }
        this.autocompleteAdapter?.notifyDataSetChanged()
    }

    /**
     * Update list dropdown with new list
     * @param data
     */
    fun updateListDropdown(data: ArrayList<AutocompleteData>?) {
        if (data != null && this.autocompleteAdapter != null) {
            this.items?.clear()
            this.itemsDropDown.clear()
            this.items?.addAll(data)
            for (item in data) {
                if (item.isHidden() == false) this.itemsDropDown.add(item)
            }
            setAdapter()
        }
    }

    private val shouldGetDataSelected = this.view?.visibility == VISIBLE && this.autocompleteDataSelected != null

    interface OnSelectedListener {
        fun onSelectedData(autocompleteData: AutocompleteData?)
    }
}