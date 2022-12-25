package com.example.demo.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class PersonDto(

    @field:NotBlank
    @Size(min=3, max=15)
    val login: String = "",

    @field:NotBlank
    @field:Size(min=3, max=30)
    val firstname: String = "",

    @field:NotBlank
    @field:Size(min=3, max=30)
    val lastname: String = "",


    @field:NotBlank
    @field:Size(min=8, max=30)
    val password: String = "",


    @field:Size(min=0, max=500)
    val description: String? = null,

    @field:NotBlank
    val id: Long?,
)
