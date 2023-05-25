package com.retro.retromall.address.domain

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name="tb_address")
class AddressEntity(

    @Id
    @Column(name = "id")
    val id : Long,

    @Column(name = "addr")
    val addr : String

)

