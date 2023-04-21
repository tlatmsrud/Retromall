package com.retro.retromall.address.domain.repository

import com.retro.retromall.address.domain.Address

interface AddressRepositoryCustom {

    fun findBySearchWordLike(searchWord : String) : List<Address>?
}