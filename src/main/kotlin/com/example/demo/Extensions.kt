package com.example.demo

import com.example.demo.dto.ClientDto
import com.example.demo.dto.PersonDto
import com.example.demo.domain.Client
import com.example.demo.domain.Person
import java.util.*

fun String.toSlug() = lowercase(Locale.getDefault())
    .replace("\n", " ")
    .replace("[^a-z\\d\\s]".toRegex(), " ")
    .split(" ")
    .joinToString("-")
    .replace("-+".toRegex(), "-")

fun Client.toDto() = ClientDto(
    login = this.login,
    firstname = this.firstname,
    lastname = this.lastname,
    role = this.role,
    description = this.numTel,
    InitialDuMilieu = this.titre,
    genre = this.genre,
    pays = this.pays,
    adresse = this.adresse,
    adresseMail = this.adresseMail,
    dateNaissance = this.dateNaissance,
    age = this.age,
    CCType = this.CCType,
    CCNumber = this.CCNumber,
    CVV2 = this.CVV2,
    CCExpires = this.CCExpires,
    Occupation = this.Occupation,
    Company = this.Company,
    AnneeV = this.AnneeV,
    MarqueV = this.MarqueV,
    ModeleV = this.ModeleV,
    Pounds = this.Pounds,
    Kilograms = this.Kilograms,
    FeetInches = this.FeetInches,
    Centimeters = this.Centimeters,
    Latitude = this.Latitude,
    Longitude = this.Longitude,
    password = this.password,
    estValide = this.estValide,
    id = this.id

)

fun Person.toDto() = PersonDto(
    login = this.login,
    firstname = this.firstname,
    lastname = this.lastname,
    description = this.description,
    id = this.id
)

fun PersonDto.toPerson() = Person(
    login = this.login,
    firstname = this.firstname,
    lastname = this.lastname,
    role = "ROLE_USER",
    description = this.description,
    id = this.id,
    password = this.password
)
