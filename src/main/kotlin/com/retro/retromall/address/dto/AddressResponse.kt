package com.retro.retromall.address.dto

import com.fasterxml.jackson.annotation.JsonProperty


data class AddressResponse(
    @JsonProperty("id") val id : Long,
    @JsonProperty("name") val name : String
)