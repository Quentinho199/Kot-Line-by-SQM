package com.example.demo.service

import com.example.demo.domain.Person
import com.example.demo.repository.PersonRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class CustomUserServiceDetails @Autowired constructor(
  private val personRepository : PersonRepository):  UserDetailsService  {
    var logger : Logger = LoggerFactory.getLogger(CustomUserServiceDetails::class.java)

    @Transactional
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {

        val user = personRepository.findByLogin(username)
        if (user == null) {
             logger.info("user not found : $username")
             throw UsernameNotFoundException("Could not find user")
        }

        val authorities: List<GrantedAuthority> = listOf(SimpleGrantedAuthority(user.role))

        return buildUserForAuthentication(user, authorities)
    }

    private fun buildUserForAuthentication(user: Person, authorities: List<GrantedAuthority>): UserDetails {
        return org.springframework.security.core.userdetails.User(user.login, user.password,
                true, true, true, true, authorities)
    }

}
