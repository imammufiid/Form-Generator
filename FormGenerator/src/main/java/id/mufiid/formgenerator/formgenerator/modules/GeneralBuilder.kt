package id.mufiid.formgenerator.formgenerator.modules

import android.app.Activity
import android.view.ViewGroup
import android.widget.LinearLayout

interface GeneralBuilder<T> {
    fun setActivity(activity: Activity): T
    fun setTitle(title: String?): T
    fun setTitleFont(title: String?): T
    fun setTitleColorResource(titleColorResource: Int): T
    fun setOrientation(orientation: Int): T
    fun setNullable(nullable: Boolean): T
    fun setDefStyleAttr(defStyleAttr: Int): T
    fun setFormLayout(formLayout: ViewGroup?): T
}