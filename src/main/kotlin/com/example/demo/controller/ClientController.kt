package com.example.demo.controller

import com.example.demo.domain.Client
import com.example.demo.repository.ClientRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.server.ResponseStatusException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Controller
class ClientController @Autowired constructor(private val ClientRepository: ClientRepository){

    @GetMapping("/clients")
    fun index(model: Model): String {
        model["title"] = "Les clients"
//        model["auteurs"] = auteurRepository.findAllAuteurs(1).map { it.toDto() }
        model["clients"] = ClientRepository.findAllClient()
        return "client/index"
    }

    @GetMapping("/delete/{id}")
    fun deleteClient (@PathVariable id: Long?, requete : HttpServletRequest, reponse: HttpServletResponse): String {
        var id = requete.getParameter("id")
        var one = id.toLong()
        ClientRepository.deleteById(one)
        return "redirect:/client"
    }

    @GetMapping("/client/detail/{id}")
    fun detail(@PathVariable id: Long, model: Model): String {
        println("id = ${id}")
        val client : Client = ClientRepository.findByIdOrNull(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "clientRepository not found")

        model["title"] = "Les clients"
        model["client"] = client
        return "client/detail"
    }

}
