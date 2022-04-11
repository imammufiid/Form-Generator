package id.mufiid.formgenerator.formgenerator.function

import android.content.Context
import android.widget.Toast
import id.mufiid.formgenerator.formgenerator.views.EditTextController
import id.mufiid.formgenerator.formgenerator.views.EditTextMultipleController

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
                    context,
                    errorMessagePrefix + " " + editTextController.title + " " + errorMessagePostfix,
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
            return true
        }

        fun isFilled(
            editTextMultipleController: EditTextMultipleController,
            context: Context
        ): Boolean {
            for (key in editTextMultipleController.editTextList?.keys!!) {
                if (
                    editTextMultipleController.editTextList?.get(key)?.isFilled == false &&
                    editTextMultipleController.editTextList?.get(key)?.isNullable == false
                ) {
                    editTextMultipleController.editTextList?.get(key)?.view?.requestFocus()
                    Toast.makeText(
                        context, errorMessagePrefix
                                + " " + editTextMultipleController.editTextList?.get(key)?.title
                                + " " + errorMessagePostfix,
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
            }
            return true
        }
    }
}