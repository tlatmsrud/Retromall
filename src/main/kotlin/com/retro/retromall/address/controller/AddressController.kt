package com.retro.retromall.address.controller

import com.retro.common.annotation.MemberAuthentication
import com.retro.retromall.address.dto.AddressResponse
import com.retro.retromall.address.service.AddressService
import com.retro.retromall.member.dto.AuthenticationAttributes
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/api/address")
class AddressController (
  private val addressService : AddressService
) {

    @GetMapping("/search")
    fun searchAddress(@MemberAuthentication authenticationAttributes: AuthenticationAttributes
                      , @RequestParam("searchWord") searchWord : String) : ResponseEntity<List<AddressResponse>> {
        val allAddressList = addressService.searchAllAddress()
        val findAddressList = addressService.searchAddress(allAddressList, searchWord)

        return ResponseEntity.ok(findAddressList)
    }

}