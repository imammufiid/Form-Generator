package id.mufiid.formgenerator.formgenerator.views

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import id.mufiid.formgenerator.formgenerator.R
import id.mufiid.formgenerator.formgenerator.adapter.SpinnerAdapter
import id.mufiid.formgenerator.formgenerator.model.SpinnerData
import id.mufiid.formgenerator.formgenerator.model.isHidden
import id.mufiid.formgenerator.formgenerator.modules.GeneralBuilder

@SuppressLint("ViewConstructor", "InflateParams")
class SpinnerController(builder: Builder) :
    LinearLayout(builder.context, null, builder.defStyleAttr) {

    var title: String? = null
    var ctx: Context? = null
    var formLayout: LinearLayout? = null
    var mainLayout: LinearLayout? = null
    var txtTitle: TextView? = null
    var spinnerAnswer: Spinner? = null
    var view: View? = null
    var items = ArrayList<SpinnerData>() //full item

    var itemDropDown = java.util.ArrayList<SpinnerData>()
    var nullable = false
    var onSelectedListener: OnSelectedListener? = null
    var spinnerAdapter: SpinnerAdapter? = null

    class Builder(activity: Activity) : GeneralBuilder<Builder>, Cloneable {
        var context: Context? = null
        var activity: Activity? = null
        var items: ArrayList<SpinnerData>? = null
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

        fun setBackgroundDrawable(backgroundDrawable: Drawable): Builder {
            this.backgroundDrawable = backgroundDrawable
            return this
        }

        fun setFormResource(resource: Int): Builder {
            this.formViewResource = resource
            return this
        }

        fun setItem(items: ArrayList<SpinnerData>): Builder {
            this.items = items
            return this
        }

        fun setDefaultSelectedValue(defaultSelectedValue: String?): Builder {
            this.defaultSelectedValue = defaultSelectedValue
            return this
        }

        fun setDefaultSelectedId(defaultSelectedId: Int): Builder {
            this.defaultSelectedId = defaultSelectedId
            return this
        }

        fun create(): SpinnerController {
            return SpinnerController(this)
        }

        @Throws(CloneNotSupportedException::class)
        override fun clone(): Builder {
            return super.clone() as Builder
        }
    }

    init {
        this.title = builder.title
        this.ctx = builder.context

        if (builder.formViewResource != -1) {
            this.view = LayoutInflater.from(this.ctx).inflate(builder.formViewResource, null)
        } else {
            this.view = LayoutInflater.from(this.ctx).inflate(R.layout.form_dropdown, null)
        }

        txtTitle = view?.findViewById(R.id.item_spinner_title)
        txtTitle?.text = this.title

        spinnerAnswer = view?.findViewById(R.id.item_spinner_list)

        if (builder.items != null) {
            this.items.addAll(builder.items!!)

            for (item in builder.items!!) { //show all but hidden item
                if (item.isHidden() == false) this.itemDropDown.add(item)
            }
        }

        if (this.ctx != null) {
            spinnerAdapter = SpinnerAdapter(this.ctx!!, R.layout.row_spinner, this.itemDropDown)
        }
        spinnerAnswer?.adapter = spinnerAdapter
        spinnerAnswer?.onItemSelectedListener = object: OnItemSelectedListener {
            override fun onItemSelected(av: AdapterView<*>?, v: View?, i: Int, l: Long) {
                if (onSelectedListener != null) {
                    onSelectedListener?.onSelectedData(items[i])
                    onSelectedListener?.onSelectedPosition(i)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

        if (builder.defaultSelectedValue != null) {
            setValue(builder.defaultSelectedValue)
        }

        if (builder.defaultSelectedId != -1) {
            setIdItem(builder.defaultSelectedId)
        }

        if (builder.formLayout != null) {
            this.formLayout = builder.formLayout
            this.formLayout?.addView(this.view)
        }
    }

    fun setSpinnerOnSelectedListener(onSelectedListener: OnSelectedListener?) {
        this.onSelectedListener = onSelectedListener
    }

    fun setPosition(position: Int) {
        spinnerAnswer?.setSelection(position)
    }

    fun getSelectedValue(): String? {
        return if (this.view?.visibility == View.VISIBLE) {
            val spinnerDataSelected = spinnerAnswer?.selectedItem as SpinnerData
            spinnerDataSelected.value
        } else {
            null
        }
    }

    fun getSelectedId(): Int? {
        return if (this.view?.visibility == View.VISIBLE) {
            val spinnerDataSelected = spinnerAnswer?.selectedItem as SpinnerData
            spinnerDataSelected.id
        } else {
            0
        }
    }

    fun getSelectedSecondaryId(): String? {
        return if (view!!.visibility == VISIBLE) {
            val spinnerDataSelected = spinnerAnswer!!.selectedItem as SpinnerData
            spinnerDataSelected.secondaryId
        } else "0"
    }

    fun getIndexSelected(): Int {
        return spinnerAnswer!!.selectedItemPosition
    }

    fun isNullable(): Boolean {
        return nullable
    }

    @JvmName("setNullable1")
    fun setNullable(nullable: Boolean) {
        this.nullable = nullable
    }

    fun getTextTitle(): TextView? {
        return txtTitle
    }

    private fun setIdItem(id: Int) {
        if (id != -1) {
            var spinnerData = SpinnerData()
            for (data in itemDropDown) {
                if (data.id == id) {
                    spinnerData = data
                    break
                }
            }
            spinnerAnswer?.setSelection(indexOfItem(spinnerData))
        }
    }

    fun setValue(value: String?) {
        if (value != null) {
            for (data in itemDropDown) {
                if (data.value == value) {
                    spinnerAnswer?.setSelection(indexOfItem(data))
                }
            }
        }
    }

    /**
     * hide dropdown list by id
     */
    fun hideItemById(id: Int) {
        for (items in items) {
            if (items.id == id) {
                items.hidden = true
                break
            }
        }
        reloadListDropdown()
    }

    /**
     * hide dropdown list by value
     */
    fun hideItemByValue(value: String?) {
        for (items in items) {
            if (items.value.equals(value)) {
                items.hidden = true
                break
            }
        }
        reloadListDropdown()
    }

    /**
     * Select dropdown list by id
     */
    fun showItemById(id: Int) {
        for (items in items) {
            if (items.id == id) {
                items.hidden = false
                break
            }
        }
        reloadListDropdown()
    }


    /**
     * Select dropdown list by value
     * @param value
     */
    fun showItemByValue(value: String?) {
        for (items in items) {
            if (items.value.equals(value)) {
                items.hidden = false
                break
            }
        }
        reloadListDropdown()
    }

    private fun reloadListDropdown() {
        itemDropDown.clear()
        for (item in items) {
            if (item.isHidden() == false) itemDropDown.add(item)
        }
    }

    /**
     * Update list dropdown with new list
     * @param data
     */
    private fun updateListDropDown(data: ArrayList<SpinnerData>?) {
        if (data != null && spinnerAdapter != null) {
            this.items.clear()
            this.itemDropDown.clear()
            this.items.addAll(data)
            for (item in items) {
                if (item.isHidden() == false) this.itemDropDown.add(item)
            }
            spinnerAdapter?.updateListDropdown(this.itemDropDown)
            spinnerAnswer?.setSelection(0)
        }
    }

    private fun indexOfItem(value: SpinnerData?): Int {
        return itemDropDown.indexOf(value)
    }

    fun attachView() {
        this.formLayout?.addView(view)
    }

    fun detachView() {
        this.formLayout?.removeView(view)
    }

    interface OnSelectedListener {
        fun onSelectedData(spinnerData: SpinnerData?)
        fun onSelectedPosition(position: Int)
    }
}