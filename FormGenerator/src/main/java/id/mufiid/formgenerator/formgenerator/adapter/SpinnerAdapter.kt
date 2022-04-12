package id.mufiid.formgenerator.formgenerator.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import id.mufiid.formgenerator.formgenerator.model.SpinnerData

class SpinnerAdapter(context: Context, resource: Int, data: ArrayList<SpinnerData>) :
    ArrayAdapter<SpinnerData>(context, resource, data) {
        private var dataSet: ArrayList<SpinnerData> = data

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val txtItem = super.getView(position, convertView, parent) as TextView
        txtItem.text = dataSet[position].value
        return txtItem
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val txtItem = super.getView(position, convertView, parent) as TextView
        txtItem.text = dataSet[position].value
        return txtItem
    }

    fun updateListDropdown(dataList: ArrayList<SpinnerData>) {
        dataSet = dataList
        notifyDataSetChanged()
    }
}