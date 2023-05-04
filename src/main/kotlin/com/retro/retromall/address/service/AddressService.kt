package com.retro.retromall.address.service

import com.retro.retromall.address.domain.Address
import com.retro.retromall.address.domain.repository.AddressRepository
import com.retro.retromall.address.domain.repository.AddressRepositoryCustom
import org.springframework.stereotype.Service

@Service
class AddressService (
    private val addressRepository: AddressRepository
){

    fun searchAddress(searchWord : String) : List<Address>? {
        return addressRepository.findBySearchWordLike(searchWord)
    }

    fun findById(id : Long): Address{
        return addressRepository.findById(id).orElseThrow {
            IllegalArgumentException("유효하지 않는 주소 ID 입니다. 관리자에게 문의해주세요.")
        }
    }
}