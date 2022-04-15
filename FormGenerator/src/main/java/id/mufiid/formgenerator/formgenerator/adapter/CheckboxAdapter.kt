package id.mufiid.formgenerator.formgenerator.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.mufiid.formgenerator.formgenerator.R
import id.mufiid.formgenerator.formgenerator.model.CheckboxData

class CheckboxAdapter(context: Context, checkboxDataList: List<CheckboxData?>): RecyclerView.Adapter<CheckboxAdapter.CheckboxViewHolder>() {
    var context: Context? = null
    var checkboxDataList: List<CheckboxData?>? = null

    init {
        this.context = context
        this.checkboxDataList = checkboxDataList
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CheckboxViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.row_checkbox,
            viewGroup, false
        )
        return CheckboxViewHolder(view)
    }

    override fun onBindViewHolder(checkboxViewHolder: CheckboxViewHolder, i: Int) {
        val checkboxData: CheckboxData = checkboxDataList?.get(i) ?: CheckboxData()
        checkboxViewHolder.rowTitle.text = checkboxData.value
        checkboxData.checked?.let { checkboxViewHolder.checkBox.isChecked = it }
        checkboxViewHolder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            checkboxData.checked = isChecked
        }
    }

    fun getCheckboxListChecked(): List<CheckboxData> {
        val listChecked: MutableList<CheckboxData> = ArrayList()
        for (checkboxData in this.checkboxDataList!!) {
            if (checkboxData?.checked == true) {
                listChecked.add(checkboxData)
            }
        }
        return listChecked
    }

    override fun getItemCount() = checkboxDataList?.size ?: 0

    class CheckboxViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var rowTitle: TextView = itemView.findViewById(R.id.txtRowCheckboxTitle)
        var checkBox: CheckBox = itemView.findViewById(R.id.checkboxSelected)

        init {
            setIsRecyclable(false)
        }
    }
}