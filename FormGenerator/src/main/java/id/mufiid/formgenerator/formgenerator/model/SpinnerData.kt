package id.mufiid.formgenerator.formgenerator.model

data class SpinnerData(
    var id: Int? = 0,
    var secondaryId: String? = "",
    var value: String? = "",
    var hidden: Boolean? = false
)

fun SpinnerData.isHidden() = hidden