package id.mufiid.formgenerator.formgenerator.adapter

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import id.mufiid.formgenerator.formgenerator.R
import id.mufiid.formgenerator.formgenerator.model.AutocompleteData
import java.util.*
import kotlin.collections.ArrayList

class AutocompleteAdapter(
    private val activity: Activity,
    private val resourceId: Int,
    dataList: List<AutocompleteData>
) :
    ArrayAdapter<AutocompleteData?>(activity.baseContext, resourceId, dataList) {
    private var dataList: ArrayList<AutocompleteData> = arrayListOf()
    private var tempDataList: ArrayList<AutocompleteData> = arrayListOf()
    private var suggestions: ArrayList<AutocompleteData> = arrayListOf()

    fun updateListDropdown(dataList: ArrayList<AutocompleteData>) {
        this.dataList = dataList
        tempDataList.clear()
        tempDataList.addAll(dataList)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        try {
            if (convertView == null) {
                val inflater = activity.layoutInflater
                view = inflater.inflate(resourceId, parent, false)
            }
            val autocompleteData = getItem(position)
            val itemTitle = view?.findViewById<TextView>(R.id.txtRowAtcTitle)
            itemTitle?.text = autocompleteData.value
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return view!!
    }

    override fun getItem(position: Int): AutocompleteData {
        return dataList[position]
    }

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getFilter(): Filter {
        return dataFilter
    }

    private val dataFilter: Filter = object : Filter() {
        override fun convertResultToString(resultValue: Any): String? {
            val autocompleteData: AutocompleteData = resultValue as AutocompleteData
            return autocompleteData.value
        }

        override fun performFiltering(charSequence: CharSequence?): FilterResults {
            return if (charSequence != null && charSequence.toString().isNotEmpty()) {
                suggestions.clear()
                for (autocompleteData in tempDataList) {
                    if (autocompleteData.value?.contains(" ") == true) {
                        val split =
                            autocompleteData.value?.lowercase()?.split(" ")
                        if (split != null) {
                            for (sub in split) {
                                if (sub.startsWith(
                                        charSequence.toString().lowercase(Locale.getDefault())
                                    )
                                ) {
                                    suggestions.add(autocompleteData) //find for each word
                                }
                            }
                        }
                    } else if (autocompleteData.value?.lowercase()
                            ?.startsWith(charSequence.toString().lowercase()) == true
                    ) {
                        suggestions.add(autocompleteData)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = suggestions
                filterResults.count = suggestions.size
                filterResults
            } else {
                suggestions.clear()
                suggestions.addAll(tempDataList)
                val filterResults = FilterResults()
                filterResults.values = suggestions
                filterResults.count = suggestions.size
                filterResults
            }
        }

        override fun publishResults(charSequence: CharSequence?, results: FilterResults) {
            val tempValues: ArrayList<AutocompleteData> =
                results.values as ArrayList<AutocompleteData>
            Log.d("RESULT1", results.values.toString())
            Log.d("RESULT2", tempValues.toString())
            clear()
            if (results.count > 0) {
                for (autocompleteData in tempValues) {
                    add(autocompleteData)
                    notifyDataSetChanged()
                }
            } else {
                notifyDataSetChanged()
            }
        }
    }

    init {
        this.dataList = dataList as ArrayList<AutocompleteData>
        tempDataList = ArrayList(dataList)
        suggestions = ArrayList()
    }
}