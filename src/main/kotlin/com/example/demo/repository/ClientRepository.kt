package com.example.demo.repository


import com.example.demo.domain.Client
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface ClientRepository : CrudRepository<Client, Long> {
    // ---------------------------------------------------------------
    // Version Native SQL (dépendance avec le schéma relationnel)
    // ---------------------------------------------------------------




    // ---------------------------------------------------------------
    // Version DSL by Spring Boot (ne dépend que du modèle)
    // ---------------------------------------------------------------

    override fun deleteById(id: Long)

    fun findByLogin(login: String): Client
    fun existsClientByLoginIgnoreCase(login: String): Boolean
    fun deleteAllById(id: Long): Client

    @Query(
        value = "select * from Client", nativeQuery = true)
    fun findAllClient(): Iterable<Client>


}
