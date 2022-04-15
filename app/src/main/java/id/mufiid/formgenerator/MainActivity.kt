package id.mufiid.formgenerator

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import id.mufiid.formgenerator.databinding.ActivityMainBinding
import id.mufiid.formgenerator.formgenerator.model.AutocompleteData
import id.mufiid.formgenerator.formgenerator.model.CheckboxData
import id.mufiid.formgenerator.formgenerator.model.SpinnerData
import id.mufiid.formgenerator.formgenerator.utils.Mode
import id.mufiid.formgenerator.formgenerator.views.*

class MainActivity : AppCompatActivity() {
    private var _bind: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_bind?.root)

        setupFormGenerator()
    }

    private fun setupFormGenerator() {
        /**
         * Make template and set default value
         */
        val editTextBuilder = EditTextController.Builder(this)
            .setFormLayout(_bind?.formLayout)

        /**
         * Attach Edit Text View
         */
        val searchEditText = editTextBuilder.clone().apply {
            title = "Edit Text Search"
            mode = Mode.SEARCH
        }.create()

        /**
         * Attach Edit Text View Mode.GENERAL
         */
        editTextBuilder.clone().apply {
            title = "Edit Text General"
            mode = Mode.GENERAL
        }.create()

        /**
         * Attach Edit Text View Mode.GENERAL
         */
        editTextBuilder.clone().apply {
            title = "Edit Text Min Lines"
            mode = Mode.GENERAL
            setMinLines(4)
        }.create()

        /**
         * Attach Edit Text Date Picker
         */
        editTextBuilder.clone().setInputType(InputType.TYPE_CLASS_DATETIME)
            .setTitle("Edit Text Date Picker").setDateFormat("dd-MM-yyyy")
            .setTitleColorResource(id.mufiid.formgenerator.formgenerator.R.color.dark_grey)
            .create()

        /**
         * Attach Edit Text Time Picker
         */
        editTextBuilder.clone().setInputType(InputType.TYPE_DATETIME_VARIATION_TIME)
            .setTitle("Edit Text Time Picker")
            .setDateFormat("HH:ii")
            .setTitleColorResource(id.mufiid.formgenerator.formgenerator.R.color.dark_grey)
            .create()

        /**
         * Attach Multiple Edit Text
         */
        val edtList: HashMap<String?, EditTextController?> = LinkedHashMap()
        val edtItemBuilder = EditTextController.Builder(this)
        edtList["No"] =
            edtItemBuilder.clone().setTitle("No").setInputType(InputType.TYPE_CLASS_NUMBER).create()
        edtList["Zip"] =
            edtItemBuilder.clone().setTitle("Zip").setInputType(InputType.TYPE_CLASS_NUMBER)
                .create()
        EditTextMultipleController.Builder(this, edtList)
            .setFormLayout(_bind?.formLayout).setMargin(50).create()

        /**
         * Attach Spinner
         */
        val cityList: ArrayList<SpinnerData> = ArrayList()
        cityList.add(SpinnerData(1, "1", "Malang"))
        cityList.add(SpinnerData(2, "2", "Surabaya", true))
        cityList.add(SpinnerData(3, "3", "Jakarta"))
        SpinnerController.Builder(this).apply {
            title = "City"
            setItem(cityList)
            setDefaultSelectedValue("Jakarta")
        }.setFormLayout(_bind?.formLayout).create()

        /**
         * Attach Autocomplete
         */
        val atcList: ArrayList<AutocompleteData> = ArrayList()
        atcList.add(AutocompleteData(1, "1", "Satu"))
        atcList.add(AutocompleteData(2, "1", "Satu Dua"))
        atcList.add(AutocompleteData(3, "3", "Tiga"))
        AutoCompleteController.Builder(this)
            .setTitle("Select").setItems(atcList).setFormLayout(_bind?.formLayout).create()

        /**
         * Attach multiple checkbox
         */

        //create multiple checkbox
        val atcListChk= ArrayList<CheckboxData>()
        for (i in 1..10) {
            atcListChk.add(CheckboxData(i, "1", "data $i", false))
        }
        val multipleCheckBox = MultipleCheckboxController.Builder(this)
            .setTitle("Select Checkbox")
            .setItems(arrayListOf())
            .setFormLayout(_bind?.formLayout)
            .create()
        multipleCheckBox.updateItemsList(atcListChk)
        val idSelected = java.util.ArrayList<String>()
        idSelected.add("1")
        idSelected.add("3")
        multipleCheckBox.setSelected(idSelected, MultipleCheckboxController.SelectedBy.ID)


        /**
         * Attach Text View
         */
        TextViewController.Builder(this)
            .apply {
                title = "Text View Title"
                content = "Text View Content"
            }
            .setFormLayout(_bind?.formLayout)
            .create()

        /**
         * Attach Checkbox
         */
        CheckBoxController.Builder(this).apply {
            title = "Checkbox"
            checkBoxItem = arrayListOf("1", "2", "3")
            formLayout = _bind?.formLayout
        }.create()

        /**
         * Attach Radio Button
         */
        RadioButtonController.Builder(this)
            .setTitle("Radio Button").setOptionList(arrayOf("Male", "Female"))
            .setFormLayout(_bind?.formLayout).setSelected("Female").create()

        /**
         * Attach Horizontal Layout
         */
        val rtEditText = EditTextController.Builder(this).apply {
            title = "RT"
        }.create()
        val rwEditText = EditTextController.Builder(this).apply {
            title = "RW"
        }.create()
        val button = ButtonController.Builder(this).setText("Check").create()
        val rsEditText = EditTextController.Builder(this).apply {
            title = "RW"
        }.create()
        val horizontalController = HorizontalLayoutController(this, 0, 20)
        horizontalController.addView(rtEditText.view, false)
        horizontalController.addView(rwEditText.view, false)
        horizontalController.addView(button.getView(), true)
        horizontalController.addView(rsEditText.view, false)
        horizontalController.setFormLayout(_bind?.formLayout)

        EditTextController.Builder(this).apply {
            title = "RW"
        }.setFormLayout(_bind?.formLayout).create()


        /**
         * Attach Button View
         */
        _bind?.formLayout?.let {
            ButtonController.Builder(this)
                .setFormLayout(it).apply {
                    text = "My Button"
                }
                .setOnClickListener {
                    startActivity(Intent(this, FormGeneratorActivity::class.java))
                }
                .create()
        }

        searchEditText.setOnClickSearchListener {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        _bind?.btnSubmit?.setOnClickListener {
            Toast.makeText(this, searchEditText.value, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _bind = null
    }
}