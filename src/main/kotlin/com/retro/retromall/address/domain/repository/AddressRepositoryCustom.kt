package com.retro.retromall.address.domain.repository

import com.retro.retromall.address.domain.AddressEntity

interface AddressRepositoryCustom {

    fun findBySearchWordLike(searchWord : String) : List<AddressEntity>?
}