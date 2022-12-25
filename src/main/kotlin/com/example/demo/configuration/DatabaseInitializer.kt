package com.example.demo.configuration

import com.example.demo.domain.Client
import com.example.demo.domain.Person
import com.example.demo.repository.ClientRepository
import com.example.demo.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.annotation.Order
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Order(value = 1)
@Component
class DatabaseInitializer @Autowired constructor(
    val personRepository: PersonRepository,
    val clientRepository: ClientRepository,
    val bCryptPasswordEncoder: BCryptPasswordEncoder
) : ApplicationRunner {

    @Throws(Exception::class)
    override fun run(args: ApplicationArguments) {
        databaseInitializer()
    }

    fun databaseInitializer() {
        val countUser = clientRepository.count();
        if (countUser > 0) return
        val moi = clientRepository.save(Client("oui","Quentin","Ayral", "ROLE_VIP"
        ,"06.06.06.06.06","Maître"," / ","France","199 avenue de la république","quentin.ayral@hotmail.com","01/01/1970","","",
        "","","","","","","X","Tesla","","69",
            "","","","","oui",true))

        val bob = personRepository.save(Person("solid", "Bob", "Martin",
            "ROLE_VIP",  bCryptPasswordEncoder.encode("password"),
            "Un référence pour les développeurs. est un ingénieur logiciel et auteur américain. Robert Cecil Martin (familièrement connu sous le nom Uncle Bob1) est un ingénieur logiciel et auteur américain. Il est co-auteur du Manifeste Agile2,3. Il dirige maintenant la société de conseil Uncle Bob Consulting LLC et le site web Clean Coders, qui héberge des vidéos basées sur son expérience et ses publications.",3001))

        val meyer = personRepository.save(Person("meyer", "Bertrand", "Meyer",
            "ROLE_VIP",  bCryptPasswordEncoder.encode("password"),
                "Bertrand Meyer publie, durant sa carrière, plusieurs ouvrages consacrés à l'informatique théorique ainsi qu'au langage Eiffel et des articles dans de nombreuses revues. Entre autres activités, il est professeur de génie logiciel à l'École polytechnique fédérale de Zurich de 2001 à 2016",3002
        ))

        val barbara = personRepository.save(Person("barbara", "Barbara", "Liskov",
            "ROLE_USER",  bCryptPasswordEncoder.encode("password"),
        "Barbara Liskov est une informaticienne américaine. Elle a reçu en 2004 la médaille John von Neumann1 pour « ses contributions fondamentales aux langages de programmation, à la méthodologie de la programmation et aux systèmes distribués » et le Prix Turing en 2008.",3003))

        val gof = personRepository.save(Person("gof", "GoF", "Design Patterns",
            "ROLE_USER",  bCryptPasswordEncoder.encode("password"),
        "Gang of Four (GoF), les quatre informaticiens Erich Gamma, Richard Helm, Ralph Johnson et John Vlissides, auteurs de l'ouvrage de référence en programmation orientée objet Design Patterns : Catalogue de modèles de conception réutilisables.",3004))
    }
}


