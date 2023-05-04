package com.retro.retromall.address.domain.repository

import com.retro.retromall.address.domain.Address
import org.springframework.data.jpa.repository.JpaRepository

interface AddressRepository : JpaRepository<Address, Long>, AddressRepositoryCustom