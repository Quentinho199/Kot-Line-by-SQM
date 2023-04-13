package com.example.demo.controller

import com.example.demo.domain.Client
import com.example.demo.repository.ClientRepository
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.time.LocalDate
import java.time.Period
import java.util.regex.Pattern

@Controller
class ImportController @Autowired constructor(private val clientRepository: ClientRepository) {
    companion object {
        const val MARGEDERREUR = 1.5
        const val CONVERSIONPOUCESENCM = 2.54
    }
    // un simple logger pour tracer des opérations ayant lieu dans cette classe
    // (basé sur la classe java qui sera construite à la compilation).
    var logger: Logger = LoggerFactory.getLogger(ImportController::class.java)

    @GetMapping("/import")
    fun index(model: Model): String {
        model["title"] = "Import de personnalités"
        return "import/index"
    }

    fun hashPassword(password: String): String {
        val passwordEncoder = BCryptPasswordEncoder()
        return passwordEncoder.encode(password)
    }

    fun getAge(bday : String): Int {
        val dateN = bday.split('/').toTypedArray()
        val moisN = dateN[0].toInt()
        val jourN = dateN[1].toInt()
        val anneeN = dateN[2].toInt()
        return Period.between(
            LocalDate.of(anneeN, moisN, jourN), LocalDate.now()
        ).years
    }
    fun correspondanceTaille(tailleCM: String, tailleInch: String): Boolean {

        val feet = tailleInch.split(' ').toTypedArray()
        fun poucesTotaux(): Int{
            for(i in feet.indices){
                val pieds = feet[0].subSequence(0, feet[0].length - 1).toString().toInt()
                val pouces = feet[1].subSequence(0, feet[1].length - 1).toString().toInt()
                return pieds * 12 + pouces
            }
            return 0
        }
        val tailleCMInches = poucesTotaux() * CONVERSIONPOUCESENCM
        return !(tailleCMInches >= tailleCM.toDouble() + MARGEDERREUR || tailleCMInches<= tailleCM.toDouble() - MARGEDERREUR)

    }
    val mapLoginCCNumber: MutableMap<String,String> = mutableMapOf()
    fun estValide(bday : String, tCM: String, tInch: String): Boolean{
        return if(getAge(bday)<18 || getAge(bday)>88) false else correspondanceTaille(tCM,tInch)
    }


    @PostMapping("/import") // //new annotation since 4.3
    fun import(
        @RequestParam("file") file: MultipartFile,
        redirectAttributes: RedirectAttributes,
    ): String? {
        if (file.isEmpty) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload")
            return "redirect:import/uploadStatus"
        }

        try {
            val inputStream: InputStreamReader = InputStreamReader(file.inputStream)
            val bufferedReader = BufferedReader(inputStream)
            val aFormat = CSVFormat.DEFAULT.builder()
                // liste des colonnes existantes du fichier CSV
                .setHeader("Number","Gender","NameSet","Title","GivenName","MiddleInitial","Surname",
                    "StreetAddress","City","State","StateFull","ZipCode","Country","CountryFull",
                    "EmailAddress","Username","Password","BrowserUserAgent","TelephoneNumber",
                    "TelephoneCountryCode","MothersMaiden","Birthday","TropicalZodiac",
                    "CCType","CCNumber","CVV2","CCExpires","NationalID","UPS","WesternUnionMTCN","MoneyGramMTCN",
                    "Color","Occupation","Company","Vehicle","Domain",
                    "BloodType","Pounds","Kilograms","FeetInches","Centimeters","Latitude","Longitude")
                .setIgnoreHeaderCase(true)
                .setSkipHeaderRecord(true)
                .setTrim(true)
                .build()

            val csvParser = CSVParser(bufferedReader, aFormat)

            var cptImportedPersons: Int = 0

            for (csvRecord in csvParser) {
                var estValide: Boolean = estValide(csvRecord.get("birthday"),csvRecord.get("Centimeters"),csvRecord.get("FeetInches"))
                for (entry in mapLoginCCNumber) {
                    if (entry.value == csvRecord.get("CCNumber")) {
                        estValide = false
                        clientRepository.findByLogin(entry.key).estValide = false
                    }
                }

                if (estValide) {
                    mapLoginCCNumber[csvRecord.get("Username")] = csvRecord.get("CCNumber")
                }

                val adresse = csvRecord.get("Streetaddress")+','+ csvRecord.get("ZipCode")+ ' ' + csvRecord.get("City")
                val age = getAge(csvRecord.get("birthday"))
//                logger.info("csvRecord : " + csvRecord)
                val mdp = hashPassword(csvRecord.get("Password"))
                val vehicle = csvRecord.get("Vehicle")
                val regex = "(\\d{4})\\s+(\\S.*?)\\s+(\\S.*)"
                val pattern = Pattern.compile(regex)
                val matcher = pattern.matcher(vehicle)
                var year: String? = null
                var brand: String? = null
                var model: String? = null
                if (matcher.find()){
                    year = matcher.group(1)
                    brand = matcher.group(2)
                    model = matcher.group(3)
                }
                val p: Client = Client(
                    login = csvRecord.get("Username"),
                    firstname = csvRecord.get("GivenName"),
                    titre = csvRecord.get("Title"),
                    lastname = csvRecord.get("Surname"),
                    role = "ROLE_USER",
                    numTel = csvRecord.get("TelephoneNumber"),
                    genre = csvRecord.get("Gender"),
                    pays = csvRecord.get("Country"),
                    adresse = adresse,
                    adresseMail = csvRecord.get("EmailAddress"),
                    dateNaissance = csvRecord.get("birthday"),
                    age = age.toString(),
                    CCType = csvRecord.get("CCType"),
                    CCNumber = csvRecord.get("CCNumber"),
                    CVV2 = csvRecord.get("CVV2"),
                    CCExpires = csvRecord.get("CCExpires"),
                    Occupation = csvRecord.get("Occupation"),
                    Company = csvRecord.get("Company"),
                    AnneeV = year,
                    ModeleV = model,
                    MarqueV = brand,
                    Pounds = csvRecord.get("Pounds"),
                    Kilograms = csvRecord.get("Kilograms"),
                    FeetInches = csvRecord.get("FeetInches"),
                    Centimeters = csvRecord.get("Centimeters"),
                    Latitude = csvRecord.get("Latitude"),
                    Longitude = csvRecord.get("Longitude"),
                    password = mdp,
                    estValide = estValide
                )
                if (!clientRepository.existsClientByLoginIgnoreCase(p.login)) {
                    cptImportedPersons++
                    clientRepository.save(p)
                }

                logger.info(p.toString())
            }

            redirectAttributes.addFlashAttribute(
                "stats",
                "Importation réussie '" + file.originalFilename + "' (" + cptImportedPersons + " records)"
            )
        } catch (e: IOException) {
            e.printStackTrace()
            redirectAttributes.addFlashAttribute(
                "message",
                "Oups!"
            )
        } finally {
            //
        }

        return "redirect:/import/uploadStatus"
    }


    @GetMapping("/import/uploadStatus")
    fun uploadStatus(model: Model): String? {
        model["title"] = "Résultat de l'upload"
        return "import/stats"
    }

}

