package com.retro.retromall.address.domain.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.retro.retromall.address.domain.AddressEntity
import com.retro.retromall.address.domain.QAddressEntity.addressEntity
import org.springframework.stereotype.Repository

@Repository
class AddressRepositoryCustomImpl (
    private val jpaQueryFactory: JPAQueryFactory
): AddressRepositoryCustom{

    override fun findBySearchWordLike(searchWord: String): List<AddressEntity> {

       return jpaQueryFactory.selectFrom(addressEntity)
            .where(addressEntity.addr.contains(searchWord))
            .orderBy(addressEntity.addr.asc())
            .fetch()
    }
}