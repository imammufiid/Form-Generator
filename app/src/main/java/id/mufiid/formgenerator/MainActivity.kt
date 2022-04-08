package id.mufiid.formgenerator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import id.mufiid.formgenerator.databinding.ActivityMainBinding
import id.mufiid.formgenerator.formgenerator.utils.Mode
import id.mufiid.formgenerator.formgenerator.views.EditTextController

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
        val editTextId = editTextBuilder.clone().apply {
            title = "ID"
            mode = Mode.SEARCH
        }.create()

        editTextId.setOnClickSearchListener {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        _bind?.btnSubmit?.setOnClickListener {
            Toast.makeText(this, editTextId.value, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _bind = null
    }
}