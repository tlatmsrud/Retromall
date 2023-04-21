package com.retro.retromall.address.service

import com.retro.retromall.address.domain.Address
import com.retro.retromall.address.domain.repository.AddressRepositoryCustom
import org.springframework.stereotype.Service

@Service
class AddressService (
    private val addressRepositoryCustom: AddressRepositoryCustom
){

    fun searchAddress(searchWord : String) : List<Address>? {
        return addressRepositoryCustom.findBySearchWordLike(searchWord)
    }
}