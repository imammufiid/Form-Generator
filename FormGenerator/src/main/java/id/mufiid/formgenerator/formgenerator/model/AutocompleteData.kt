package id.mufiid.formgenerator.formgenerator.model

data class AutocompleteData(
    var id: Int? = 0,
    var secondaryId: String? = "",
    var value: String? = "",
    var hidden: Boolean? = false
) {
    override fun toString(): String {
        return """AutocompleteData { 
            id = $id, 
            secondaryId = '$secondaryId', 
            value = '$value', 
            hidden = $hidden
            }
        """.trimIndent()
    }
}

fun AutocompleteData.isHidden() = hidden