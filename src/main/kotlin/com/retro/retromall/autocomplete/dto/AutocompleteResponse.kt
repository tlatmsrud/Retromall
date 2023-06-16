package com.retro.retromall.autocomplete.dto


data class AutocompleteResponse(
    val list : List<Data>
){
    data class Data(
        val value: String,
        val score: Double
    )
}
