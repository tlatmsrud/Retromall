package com.retro.retromall.autocomplete.controller

import com.retro.retromall.autocomplete.dto.AutocompleteResponse
import com.retro.retromall.autocomplete.service.AutocompleteService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
@RestController
@RequestMapping("/api/autocomplete")
class AutocompleteController (
    private val autocompleteService : AutocompleteService
){

    @GetMapping("/{searchWord}")
    @ResponseBody
    fun getAutocompleteList(@PathVariable searchWord : String) : ResponseEntity<AutocompleteResponse> {

        return ResponseEntity.ok(autocompleteService.getAutocomplete(searchWord))
    }
}