package com.retro.retromall.address.domain.repository

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import com.retro.retromall.address.domain.QAddressEntity.addressEntity
import com.retro.retromall.address.dto.AddressResponse
import org.springframework.stereotype.Repository

@Repository
class AddressRepositoryCustomImpl (
    private val jpaQueryFactory: JPAQueryFactory
): AddressRepositoryCustom{

    override fun findBySearchWordLike(searchWord: String): List<AddressResponse> {

       return jpaQueryFactory.select(
           Projections.constructor(
               AddressResponse::class.java
               ,addressEntity.id
               , addressEntity.addr
           )
       )
           .from(addressEntity)
           .where(addressEntity.addr.contains(searchWord))
           .orderBy(addressEntity.addr.asc())
           .fetch()
    }
}