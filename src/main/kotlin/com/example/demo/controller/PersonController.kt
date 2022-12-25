package com.example.demo.controller

import com.example.demo.dto.PersonDto
import com.example.demo.repository.PersonRepository
import com.example.demo.toDto
import com.example.demo.toPerson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import javax.validation.Valid


@Controller
class PersonController @Autowired constructor(private val personRepository: PersonRepository){

    @GetMapping("/auteurs")
    fun index(model: Model): String {
        model["title"] = "Les auteurs"
//        model["auteurs"] = auteurRepository.findAllAuteurs(1).map { it.toDto() }
        model["auteurs"] = personRepository.findAllAuteursWhereDescriptionIsNotNull().map { it.toDto() }
        return "person/index"
    }


    @GetMapping("/addPerson")
    fun showForm(personDto: PersonDto): String {
        return "person/form"
    }

    @PostMapping("/addPerson")
    fun addPerson(@Valid personDto: PersonDto, bindingResult: BindingResult): String {

         bindingResult.reject("", "Le login est déjà pris !") // Global error
//         bindingResult.rejectValue("login", "", "Le login est déjà pris !") // Property scope

        return if (bindingResult.hasErrors()) {
            "person/form"
        } else {
            personRepository.save(personDto.toPerson())
            "redirect:/auteurs"
        }
    }
}