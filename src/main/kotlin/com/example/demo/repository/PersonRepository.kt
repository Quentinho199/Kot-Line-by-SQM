package com.example.demo.repository


import com.example.demo.domain.Person
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface PersonRepository : CrudRepository<Person, Long> {
    fun findByLogin(login: String): Person?

    @Query(
        value = "SELECT * FROM person WHERE LENGTH(login) > :len", nativeQuery = true)
    fun findAllAuteurs(@Param("len") len: Int ): List<Person>

    @Query(
        value = "SELECT * FROM person WHERE description is not null", nativeQuery = true)
    fun findAllAuteursWhereDescriptionIsNotNull(): List<Person>

    fun existsPersonByFirstnameIgnoreCaseAndLastnameIgnoreCase(firstname: String, lastName: String):Boolean


}
