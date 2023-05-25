package com.retro.retromall.address.service

import com.retro.retromall.address.domain.AddressEntity
import com.retro.retromall.address.domain.repository.AddressRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class AddressService (
    private val addressRepository: AddressRepository
){

    @Cacheable(value = ["Address"], key = "#searchWord", cacheManager = "redisCacheManager")
    fun searchAddress(searchWord : String) : List<AddressEntity>? {
        return addressRepository.findBySearchWordLike(searchWord)
    }

    fun findById(id : Long): AddressEntity{
        return addressRepository.findById(id).orElseThrow {
            IllegalArgumentException("유효하지 않는 주소 ID 입니다. 관리자에게 문의해주세요.")
        }
    }
}