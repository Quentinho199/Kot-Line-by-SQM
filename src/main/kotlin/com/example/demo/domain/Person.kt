package com.example.demo.domain

import javax.persistence.*

@Entity
@Table(name = "person")
class Person(
    var login: String,
    var firstname: String,
    var lastname: String,

    // add for security role (or ManyToMAny vers Entité Role)
    var role: String,
    var password: String,

    @Column(columnDefinition = "TEXT")
    var description: String? = null,

    @Id
    var id: Long?,


    ) {
    override fun toString(): String {
        return "Person(login='$login', firstname='$firstname', lastname='$lastname', description=$description, id=$id)"
    }
}
