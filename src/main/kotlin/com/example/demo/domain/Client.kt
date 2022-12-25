package com.example.demo.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "Client")
class Client(
        var login: String,
        var firstname: String,
        var lastname: String,
        var role: String,
        var numTel: String,
        var titre: String,
        var genre: String,
        var pays: String,
        var adresse: String,
        var adresseMail: String,
        var dateNaissance: String,
        var age: String,
        var CCType: String,
        var CCNumber: String,
        var CVV2: String,
        var CCExpires: String,
        var Occupation: String,
        var Company: String,
        var AnneeV: String?,
        var ModeleV: String?,
        var MarqueV: String?,
        var Pounds: String,
        var Kilograms: String,
        var FeetInches: String,
        var Centimeters: String,
        var Latitude: String,
        var Longitude: String,
        var password: String,
        var estValide: Boolean,

//    @ManyToOne var creditedTo: Person,

        @Id @GeneratedValue var id: Long? = null
)
