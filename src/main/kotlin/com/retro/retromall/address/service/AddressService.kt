package com.retro.retromall.address.service

import com.retro.config.RedisCacheConfig
import com.retro.retromall.address.domain.AddressEntity
import com.retro.retromall.address.domain.repository.AddressRepository
import com.retro.retromall.address.dto.AddressResponse
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class AddressService (
    private val addressRepository: AddressRepository
){

    /*@Cacheable(value = ["Address"], key = "#searchWord", cacheManager = "redisCacheManager")
    fun searchAddress(searchWord : String) : List<AddressResponse> {
        return addressRepository.findBySearchWordLike(searchWord)
    }
*/
    fun findById(id : Long): AddressEntity{
        return addressRepository.findById(id).orElseThrow {
            IllegalArgumentException("유효하지 않는 주소 ID 입니다. 관리자에게 문의해주세요.")
        }
    }

    @Cacheable(value = [RedisCacheConfig.ADDRESS_LIST], key = "", cacheManager = "redisCacheManager")
    fun searchAllAddress() : List<AddressResponse> {
        val list = addressRepository.findAll()
        return list.stream()
            .map { e -> AddressResponse(e.id, e.addr) }
            .collect(Collectors.toList())

    }

    fun searchAddress(list : List<AddressResponse>, searchWord: String): List<AddressResponse> {
        return list.stream()
            .filter { address -> address.name.contains(searchWord) }
            .collect(Collectors.toList())
    }
}