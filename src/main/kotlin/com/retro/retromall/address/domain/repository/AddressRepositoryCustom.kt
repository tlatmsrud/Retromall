package com.retro.retromall.address.domain.repository

import com.retro.retromall.address.dto.AddressResponse

interface AddressRepositoryCustom {

    fun findBySearchWordLike(searchWord : String) : List<AddressResponse>
}