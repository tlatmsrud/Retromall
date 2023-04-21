package com.retro.retromall.address.domain.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.retro.retromall.address.domain.Address
import com.retro.retromall.address.domain.QAddress.address
import org.springframework.stereotype.Repository

@Repository
class AddressRepositoryCustomImpl (
    private val jpaQueryFactory: JPAQueryFactory
): AddressRepositoryCustom{

    override fun findBySearchWordLike(searchWord: String): List<Address> {

       return jpaQueryFactory.selectFrom(address)
            .where(address.addr.contains(searchWord))
            .orderBy(address.addr.asc())
            .fetch()
    }
}