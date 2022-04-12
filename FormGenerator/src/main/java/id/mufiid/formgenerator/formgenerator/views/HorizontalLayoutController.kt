package id.mufiid.formgenerator.formgenerator.views

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout

class HorizontalLayoutController(
    context: Context,
    verticalMargin: Int,
    horizontalMargin: Int
) {
    private var horizontalLayout: LinearLayout? = null
    private var listView: ArrayList<View> = arrayListOf()
    private var horMargin: Int = 0
    private var verMargin: Int = 0
    private var wrapContentHeightExist = false

    init {
        this.horizontalLayout = LinearLayout(context)
        this.horizontalLayout?.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        this.horizontalLayout?.orientation = LinearLayout.HORIZONTAL
        this.verMargin = verticalMargin
        this.horMargin = horizontalMargin
    }

    fun setFormLayout(formLayout: LinearLayout?) {
        formLayout?.addView(this.getView())
    }

    fun addView(view: View?, setHeightWrapContent: Boolean) {
        if (view != null) {
            this.listView.add(view)
        }

        if (setHeightWrapContent) {
            wrapContentHeightExist = true
        }

        if (listView.size > 1) {
            this.horizontalLayout?.removeAllViews()

            for (item in 0 until listView.size) {
                val itemView = listView[item]
                if (item == 0) {
                    /**
                     * set margin of first item
                     * */
                    val layoutParamsFirstItem = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1.0F
                    )
                    layoutParamsFirstItem.setMargins(0, verMargin, horMargin, verMargin)
                    itemView.layoutParams = layoutParamsFirstItem
                } else {
                    /**
                     * set margin on new item
                     * */
                    val layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        if (wrapContentHeightExist) LinearLayout.LayoutParams.WRAP_CONTENT else LinearLayout.LayoutParams.MATCH_PARENT,
                        1.0F
                    )
                    layoutParams.setMargins(horMargin, verMargin, 0, verMargin)
                    if (wrapContentHeightExist) layoutParams.gravity = Gravity.CENTER_VERTICAL
                    itemView.layoutParams = layoutParams
                }
                this.horizontalLayout?.addView(itemView)
            }
        } else {
            this.horizontalLayout?.addView(view)
        }
    }

    fun getView() = this.horizontalLayout

}