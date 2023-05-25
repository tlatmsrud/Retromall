package com.retro.retromall.address.domain.repository

import com.retro.retromall.address.domain.AddressEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AddressRepository : JpaRepository<AddressEntity, Long>, AddressRepositoryCustom