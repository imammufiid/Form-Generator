package id.mufiid.formgenerator.formgenerator.function

import android.content.Context
import android.widget.Toast
import id.mufiid.formgenerator.formgenerator.views.EditTextController

class ViewChecker {
    companion object {
        private var errorMessagePrefix = "Please fill"
        private var errorMessagePostfix = "data."

        fun setErrorMessagePrefix(errorMessage: String) {
            errorMessagePrefix = errorMessage
        }

        fun setErrorMessagePostfix(errorMessage: String) {
            errorMessagePostfix = errorMessage
        }

        fun isFilled(): Boolean {
            return true
        }

        fun isFilled(editTextController: EditTextController, context: Context): Boolean {
            if (!editTextController.isFilled && !editTextController.isNullable) {
                editTextController.view?.requestFocus()
                Toast.makeText(
                    context, errorMessagePrefix + " " + editTextController.title + " " + errorMessagePostfix,
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
            return true
        }
    }
}