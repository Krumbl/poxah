package poxah.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.exists

class JsonFileService {

    companion object {
        const val FILE_DIR = "out"
    }

    init {
        if (!Path(FILE_DIR).exists())
            Path(FILE_DIR).createDirectory()
    }

    val objectMapper = jacksonObjectMapper()
//        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    // TODO move files to a temporary run directory
    inline fun <reified T> read(
        fileName: String,
        path: String = FILE_DIR,
    ): T? =
        if (File("$FILE_DIR/$fileName").exists()) {
            objectMapper
                .readValue(
                    File("$FILE_DIR/$fileName"),
//                    JsonFileService::class.java.classLoader.getResource(fileName),
                    T::class.java
                )
        } else {
            null
        }

    fun write(
        fileName: String,
        data: Any,
        path: String = FILE_DIR,
    ) =
        File("$FILE_DIR/$fileName").writeText(
            objectMapper.writeValueAsString(data)
        )

}