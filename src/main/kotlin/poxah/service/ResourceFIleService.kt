package poxah.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import poxah.Main
import poxah.config.Config

class ResourceFileService {

    inline fun <reified T> read(fileName: String) =
        jacksonObjectMapper().readValue(
            Main::class.java.classLoader.getResource(fileName),
            T::class.java)!!

}