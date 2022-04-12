package id.mufiid.formgenerator.formgenerator.views

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.InputType
import android.text.method.KeyListener
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import id.mufiid.formgenerator.formgenerator.R
import id.mufiid.formgenerator.formgenerator.function.ViewChecker
import id.mufiid.formgenerator.formgenerator.modules.GeneralBuilder
import id.mufiid.formgenerator.formgenerator.utils.Mode
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Imam Mufiid
 */
@SuppressLint("ValidFragment", "InflateParams", "ViewConstructor")
class EditTextController(builder: Builder) :
    LinearLayout(builder.context, null, builder.defStyleAttr) {
    private var activity: Activity? = null
    private var formLayout: LinearLayout? = null
    private var textTitle: TextView
    private var editTextContent: EditText? = null
    var view: View? = null
    private var myCalendar = Calendar.getInstance()
    private var myDateSetListener: DateSetListener? = null
    var isNullable: Boolean
    var title: String
    private var dateFormat: String? = "yyyy-MM-dd"
    private var datePickerDialog: DatePickerDialog? = null
    private var timePickerDialog: TimePickerDialog? = null

    //search mode
    private var onClickSearchListener: OnClickSearchListener? = null
    private var btnSearch: ImageButton? = null

    class Builder(activity: Activity) : GeneralBuilder<Builder?>, Cloneable {
        //required
        var activity: Activity? = activity
        var context: Context? = null

        //optional
        var formLayout: LinearLayout? = null
        var title: String? = null
        var minLines = 0
        var dateFormat: String? = null
        var backgroundDrawable: Drawable? = null
        var titleFont: String? = null
        var nullable = false
        var mode: Mode? = null
        var orientasion = VERTICAL
        var inputType = -1
        var titleColorResource = -1
        var defStyleAttr: Int = R.style.Theme_FormGenerator
        var formViewResource = -1
        var length: Int = Int.MAX_VALUE
        var isEnabled: Boolean = true
        var hint: String = ""

        init {
            context = activity.baseContext
        }

        /**
         * Set parent activity
         */
        override fun setActivity(activity: Activity): Builder {
            this.activity = activity
            context = activity.baseContext
            return this
        }

        /**
         * Set title edit text
         */
        override fun setTitle(title: String?): Builder {
            this.title = title
            return this
        }

        /**
         * Set font type title edit text
         */
        override fun setTitleFont(title: String?): Builder {
            this.titleFont = title
            return this
        }

        /**
         * Set orientation
         */
        override fun setOrientation(orientation: Int): Builder {
            this.orientasion = orientation
            return this
        }

        /**
         * Set title color
         */
        override fun setTitleColorResource(titleColorResource: Int): Builder {
            this.titleColorResource = titleColorResource
            return this
        }

        /**
         * Set nullable
         */
        override fun setNullable(nullable: Boolean): Builder {
            this.nullable = nullable
            return this
        }

        /**
         * Set style attribute
         */
        override fun setDefStyleAttr(defStyleAttr: Int): Builder {
            this.defStyleAttr = defStyleAttr
            return this
        }

        /**
         * Set view group
         */
        override fun setFormLayout(formLayout: LinearLayout?): Builder {
            this.formLayout = formLayout
            return this
        }

        /**
         * Set date format, if input type is DateFormat
         */
        fun setDateFormat(dateFormat: String?): Builder {
            this.dateFormat = dateFormat
            return this
        }

        /**
         * Set Mode edit text
         * Mode.GENERAL: Default edit text
         * Mode.SEARCH: Edit text with search listener
         */
        fun setMode(mode: Mode?): Builder {
            this.mode = mode
            return this
        }

        /**
         * Set min lines
         */
        fun setMinLines(minLines: Int): Builder {
            this.minLines = minLines
            return this
        }

        /**
         * Set input type
         */
        fun setInputType(inputType: Int): Builder {
            this.inputType = inputType
            return this
        }

        /**
         * Set view source
         */
        fun setFormViewResource(resource: Int): Builder {
            formViewResource = resource
            return this
        }

        /**
         * Set background edit text from drawable
         */
        fun setBackgroundDrawable(backgroundDrawable: Drawable?): Builder {
            this.backgroundDrawable = backgroundDrawable
            return this
        }

        /**
         * Set how length edit text, by default length edit text is Int.MAX_VALUE
         */
        fun setLength(length: Int): Builder {
            this.length = length
            return this
        }

        /**
         * set enable or disable edit text
         */
        fun setEnabled(isEnabled: Boolean): Builder {
            this.isEnabled = isEnabled
            return this
        }

        /**
         * set hint edit text
         */
        fun setHint(hint: String): Builder {
            this.hint = hint
            return this
        }

        /**
         * Create Builder edit text
         */
        fun create(): EditTextController {
            return EditTextController(this)
        }

        /**
         * Clone builder
         */
        @Throws(CloneNotSupportedException::class)
        public override fun clone(): Builder {
            return super.clone() as Builder
        }
    }

    @SuppressLint("NewApi")
    override fun setBackground(drawable: Drawable) {
        editTextContent?.background = drawable
    }

    var dateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar[Calendar.YEAR] = year
            myCalendar[Calendar.MONTH] = monthOfYear
            myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            if (dateFormat == null) {
                dateFormat = "yyyy-MM-dd"
            }
            val sdf = SimpleDateFormat(dateFormat, Locale.US)
            this.editTextContent?.setText(sdf.format(myCalendar.time))
            myDateSetListener?.onDateSet(sdf.format(myCalendar.time))
        }

    @SuppressLint("SetTextI18n")
    var timeSetListener =
        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute -> editTextContent?.setText("$hourOfDay:$minute") }
    var value: String?
        get() {
            return if (view?.visibility == GONE) {
                null
            } else {
                editTextContent?.text.toString()
            }
        }
        set(text) {
            if (text != null) editTextContent?.setText(text)
        }

    interface DateSetListener {
        fun onDateSet(date: String?)
    }

    @JvmName("setMyDateSetListener1")
    fun setMyDateSetListener(myDateSetListener: DateSetListener?) {
        this.myDateSetListener = myDateSetListener
    }

    val keyListener: KeyListener? = editTextContent?.keyListener

    fun setEditable(editable: Boolean, keyListener: KeyListener?) {
        if (editable) {
            editTextContent?.keyListener = keyListener
        } else {
            editTextContent?.keyListener = null
        }
    }

    val isFilled: Boolean
        get() {
            if (!isNullable) {
                if (editTextContent?.text.toString().isEmpty()) return false
            }
            return true
        }

    /**
     * contract on click search listener
     */
    fun interface OnClickSearchListener {
        fun onClickSearch(keyword: String?)
    }

    fun setOnClickSearchListener(onClickSearchListener: OnClickSearchListener) {
        this.onClickSearchListener = onClickSearchListener
    }

    fun attachView() {
        formLayout?.addView(view)
    }

    fun detachView() {
        formLayout?.removeView(view)
    }

    /**
     * Check edittext is empty or not
     */
    fun checkMustFilled(): Boolean {
        return ViewChecker.isFilled(this, this.context)
    }

    init {
        this.activity = builder.activity
        this.isNullable = builder.nullable
        this.title = builder.title.toString()

        /**
         * set layout view
         */
        view = if (builder.formViewResource != -1) {
            LayoutInflater.from(builder.context).inflate(builder.formViewResource, null)
        } else {
            LayoutInflater.from(builder.context).inflate(R.layout.form_edittext, null)
        }

        /**
         * Checking mode edit text
         */
        if (builder.mode == Mode.SEARCH) {
            btnSearch = view?.findViewById<ImageButton>(R.id.btnSearch)
            btnSearch?.visibility = VISIBLE
            btnSearch?.setOnClickListener {
                onClickSearchListener?.onClickSearch(
                    editTextContent?.text.toString()
                )
            }
        }

        /**
         * Binding view
         */
        textTitle = view?.findViewById(R.id.item_edittext_title) as TextView
        editTextContent = view?.findViewById(R.id.item_edittext_value)

        /**
         * set min lines edit text
         */
        editTextContent?.minLines = builder.minLines

        /**
         * set tag edit text with title value
         */
        editTextContent?.tag = builder.title

        /**
         * checking input type
         */
        when (builder.inputType) {
            InputType.TYPE_CLASS_DATETIME -> {
                if (dateFormat != null) {
                    dateFormat = builder.dateFormat
                }
                datePickerDialog = this.activity?.let {
                    DatePickerDialog(
                        it, dateSetListener,
                        myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
                        myCalendar[Calendar.DAY_OF_MONTH]
                    )
                }
                editTextContent?.isFocusableInTouchMode = false
                editTextContent?.setOnClickListener { _ -> datePickerDialog?.show() }
                editTextContent?.keyListener = null
                editTextContent?.inputType = InputType.TYPE_NULL
            }
            InputType.TYPE_DATETIME_VARIATION_TIME -> {
                if (dateFormat != null) {
                    dateFormat = builder.dateFormat
                }
                val mCurrentTime = Calendar.getInstance()
                val hour = mCurrentTime[Calendar.HOUR_OF_DAY]
                val minute = mCurrentTime[Calendar.MINUTE]
                timePickerDialog = TimePickerDialog(this.activity, timeSetListener, hour, minute, true)
                editTextContent?.isFocusableInTouchMode = false
                editTextContent?.setOnClickListener { _ -> timePickerDialog?.show() }
                editTextContent?.keyListener = null
                editTextContent?.inputType = InputType.TYPE_NULL
            }
            else -> {
                if (builder.inputType != -1) editTextContent?.inputType = builder.inputType
            }
        }

        /**
         * set title edit text
         */
        if (builder.title != null) {
            textTitle.text = builder.title
        }

        /**
         * set background edit text from drawable
         */
        if (builder.backgroundDrawable != null) {
            editTextContent?.background = builder.backgroundDrawable
        }

        /**
         * set title font
         */
        if (builder.titleFont != null) {
            val face = Typeface.createFromAsset(
                this.context?.assets,
                builder.titleFont
            )
            textTitle.typeface = face
        }

        /**
         * set title color resource
         */
        if (builder.titleColorResource != -1) {
            if (this.context != null) {
                textTitle.setTextColor(
                    ContextCompat.getColor(
                        this.context!!,
                        builder.titleColorResource
                    )
                )
            }
        }

        /**
         * Handle on text changed listener
         */
        editTextContent?.addTextChangedListener {
            if (it.toString().length > builder.length) {
                editTextContent?.error = "${builder.title} max length ${builder.length}"
            }
        }

        /**
         * set hint edit text
         */
        editTextContent?.hint = builder.hint

        /**
         * set enabled edit text
         */
        editTextContent?.isEnabled = builder.isEnabled

        /**
         * Inflate to parent view
         */
        if (builder.formLayout != null) {
            formLayout = builder.formLayout
            formLayout?.addView(view)
        }
    }
}